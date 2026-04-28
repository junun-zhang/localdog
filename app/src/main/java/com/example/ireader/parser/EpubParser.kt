package com.example.ireader.parser

import timber.log.Timber
import java.io.File
import java.util.zip.ZipFile

data class EpubChapter(
    val title: String,
    val content: String,
    val index: Int
)

data class EpubBookInfo(
    val title: String,
    val authors: List<String>,
    val description: String?,
    val language: String?,
    val coverImage: ByteArray?,
    val chapters: List<EpubChapter>
)

class EpubParser {

    fun parse(file: File): EpubBookInfo? {
        return try {
            val zipFile = ZipFile(file)
            
            val opfPath = findOpfPath(zipFile)
            if (opfPath == null) {
                Timber.e("找不到OPF文件")
                zipFile.close()
                return null
            }

            val opfEntry = zipFile.getEntry(opfPath)
            val opfContent = zipFile.getInputStream(opfEntry).readBytes().toString(Charsets.UTF_8)
            
            val title = extractSimpleTag(opfContent, "dc:title") ?: file.nameWithoutExtension
            val authors = extractAllTags(opfContent, "dc:creator")
            val description = extractSimpleTag(opfContent, "dc:description")
            val language = extractSimpleTag(opfContent, "dc:language")
            
            val coverImage = extractCoverImage(zipFile, opfContent, opfPath)
            val chapters = extractChapters(zipFile, opfContent, opfPath)
            
            zipFile.close()
            
            EpubBookInfo(
                title = title,
                authors = authors,
                description = description,
                language = language,
                coverImage = coverImage,
                chapters = chapters
            )
        } catch (e: Exception) {
            Timber.e(e, "EPUB解析失败: ${file.absolutePath}")
            null
        }
    }

    private fun findOpfPath(zipFile: ZipFile): String? {
        return try {
            val containerEntry = zipFile.getEntry("META-INF/container.xml")
            if (containerEntry == null) {
                return "OEBPS/content.opf"
            }
            val containerContent = zipFile.getInputStream(containerEntry).readBytes().toString(Charsets.UTF_8)
            val fullPathMatch = Regex("full-path=\"([^\"]+)\"").find(containerContent)
            fullPathMatch?.groupValues?.get(1)
        } catch (e: Exception) {
            Timber.e(e, "解析container.xml失败")
            null
        }
    }

    private fun extractSimpleTag(content: String, tagName: String): String? {
        val pattern = Regex("<$tagName[^>]*>([^<]+)</$tagName>")
        return pattern.find(content)?.groupValues?.get(1)?.trim()
    }

    private fun extractAllTags(content: String, tagName: String): List<String> {
        val pattern = Regex("<$tagName[^>]*>([^<]+)</$tagName>")
        return pattern.findAll(content).map { it.groupValues[1].trim() }.filter { it.isNotBlank() }.toList()
    }

    private fun extractCoverImage(zipFile: ZipFile, opfContent: String, opfPath: String): ByteArray? {
        return try {
            val opfDir = opfPath.substringBeforeLast("/", "")
            val lines = opfContent.split("\n")
            for (line in lines) {
                if (line.contains("cover") && (line.contains("image") || line.contains("png") || line.contains("jpeg") || line.contains("jpg"))) {
                    val hrefMatch = Regex("href=\"([^\"]+)\"").find(line)
                    if (hrefMatch != null) {
                        val coverPath = hrefMatch.groupValues[1]
                        val fullPath = if (opfDir.isNotEmpty()) "$opfDir/$coverPath" else coverPath
                        val entry = zipFile.getEntry(fullPath)
                        if (entry != null) {
                            return zipFile.getInputStream(entry).readBytes()
                        }
                    }
                }
            }
            null
        } catch (e: Exception) {
            null
        }
    }

    private fun extractChapters(zipFile: ZipFile, opfContent: String, opfPath: String): List<EpubChapter> {
        val chapters = mutableListOf<EpubChapter>()
        val opfDir = opfPath.substringBeforeLast("/", "")
        
        try {
            // 解析manifest获取所有item的id到href映射
            val manifestMatch = Regex("<manifest[^>]*>(.*?)</manifest>", RegexOption.DOT_MATCHES_ALL).find(opfContent)
            val manifestContent = manifestMatch?.groupValues?.get(1) ?: ""
            
            val itemMap = mutableMapOf<String, String>()
            Regex("<item\\s+[^>]*id=\"([^\"]+)\"\\s+[^>]*href=\"([^\"]+)\"").findAll(manifestContent).forEach { match ->
                itemMap[match.groupValues[1]] = match.groupValues[2]
            }
            Regex("<item\\s+[^>]*href=\"([^\"]+)\"\\s+[^>]*id=\"([^\"]+)\"").findAll(manifestContent).forEach { match ->
                itemMap[match.groupValues[2]] = match.groupValues[1]
            }
            
            // 解析spine获取阅读顺序
            val spineMatch = Regex("<spine[^>]*>(.*?)</spine>", RegexOption.DOT_MATCHES_ALL).find(opfContent)
            val spineContent = spineMatch?.groupValues?.get(1) ?: ""
            
            Regex("idref=\"([^\"]+)\"").findAll(spineContent).forEach { match ->
                val idref = match.groupValues[1]
                val href = itemMap[idref]
                if (href != null) {
                    val fullPath = if (opfDir.isNotEmpty()) "$opfDir/$href" else href
                    val entry = zipFile.getEntry(fullPath)
                    if (entry != null) {
                        val content = zipFile.getInputStream(entry).readBytes().toString(Charsets.UTF_8)
                        val chapterTitle = extractTitleFromHtml(content) ?: "第${chapters.size + 1}章"
                        chapters.add(
                            EpubChapter(
                                title = chapterTitle,
                                content = content,
                                index = chapters.size
                            )
                        )
                    }
                }
            }
        } catch (e: Exception) {
            Timber.e(e, "解析章节失败")
        }
        
        return chapters
    }

    private fun extractTitleFromHtml(htmlContent: String): String? {
        return try {
            val meaninglessTitles = listOf("未知", "Cover", "cover", "Untitled")
            var title = Regex("<title[^>]*>([^<]+)</title>").find(htmlContent)?.groupValues?.get(1)?.trim()
            if (title.isNullOrBlank() || title in meaninglessTitles) {
                title = Regex("<h1[^>]*>([^<]*)</h1>").find(htmlContent)?.groupValues?.get(1)?.trim()
            }
            if (title.isNullOrBlank() || title in meaninglessTitles) {
                title = Regex("<h2[^>]*>([^<]*)</h2>").find(htmlContent)?.groupValues?.get(1)?.trim()
            }
            if (title.isNullOrBlank() || title in meaninglessTitles) {
                title = Regex("title=\"([^\"]+)\"").find(htmlContent)?.groupValues?.get(1)?.trim()
            }
            if (title.isNullOrBlank() || title in meaninglessTitles) {
                val divMatch = Regex("<div[^>]*class=\"[^\"]*calibre2[^\"]*\"[^>]*>([^<]+)</div>").find(htmlContent)
                title = divMatch?.groupValues?.get(1)?.trim()
            }
            if (title.isNullOrBlank() || title in meaninglessTitles) null else title
        } catch (e: Exception) {
            null
        }
    }
}
