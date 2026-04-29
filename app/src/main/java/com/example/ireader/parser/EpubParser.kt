package com.example.ireader.parser

import android.content.Context
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
    /** File path to extracted cover image for Compose display */
    val coverImagePath: String? = null,
    val chapters: List<EpubChapter>,
    /** Path to the merged HTML file for continuous scrolling */
    val mergedHtmlPath: String? = null,
    /** Path to the extracted EPUB directory for WebView resource loading */
    val extractDirPath: String? = null,
    /** Map of chapter title -> anchor ID for TOC navigation */
    val chapterAnchors: List<Pair<String, String>> = emptyList()
)

class EpubParser(private val context: Context) {

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

            // Extract entire EPUB to cache directory so WebView can resolve relative paths
            val extractDir = extractEpubToCache(file, zipFile)

            // Extract cover image to a file for Compose display
            val coverImagePath = extractCoverImageToFile(zipFile, opfContent, opfPath, extractDir)

            // Create merged HTML file for continuous scrolling
            val mergedHtmlPath = createMergedHtml(chapters, extractDir)

            zipFile.close()

            val chapterAnchors = chapters.map { ch ->
                ch.title to "chapter_${ch.index}"
            }

            EpubBookInfo(
                title = title,
                authors = authors,
                description = description,
                language = language,
                coverImage = coverImage,
                coverImagePath = coverImagePath,
                chapters = chapters,
                mergedHtmlPath = mergedHtmlPath,
                extractDirPath = extractDir.absolutePath,
                chapterAnchors = chapterAnchors
            )
        } catch (e: Exception) {
            Timber.e(e, "EPUB解析失败: ${file.absolutePath}")
            null
        }
    }

    /**
     * Extract the entire EPUB zip to app cache directory.
     * This allows WebView to resolve all relative paths (images, CSS, fonts).
     */
    private fun extractEpubToCache(epubFile: File, zipFile: ZipFile): File {
        val epubName = epubFile.nameWithoutExtension
        val extractDir = File(context.cacheDir, "epub_$epubName")

        // Clean up old extraction if exists
        if (extractDir.exists()) {
            extractDir.deleteRecursively()
        }
        extractDir.mkdirs()

        zipFile.entries().asSequence().forEach { entry ->
            val destFile = File(extractDir, entry.name)
            if (entry.isDirectory) {
                destFile.mkdirs()
            } else {
                // Ensure parent directory exists
                destFile.parentFile?.mkdirs()
                zipFile.getInputStream(entry).use { input ->
                    destFile.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
            }
        }

        return extractDir
    }

    /**
     * Create a single merged HTML file from all chapters.
     * Each chapter gets an anchor ID for TOC navigation.
     * Image paths are rewritten to absolute file:// paths.
     */
    private fun createMergedHtml(chapters: List<EpubChapter>, extractDir: File): String? {
        if (chapters.isEmpty()) return null

        val mergedFile = File(extractDir, "_merged_ireader.html")

        val baseUri = "file://${extractDir.absolutePath}/"

        val sb = StringBuilder()
        sb.appendLine("<!DOCTYPE html>")
        sb.appendLine("<html>")
        sb.appendLine("<head>")
        sb.appendLine("""<meta charset="UTF-8">""")
        sb.appendLine("""<meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=no">""")
        sb.appendLine("<style>")
        sb.appendLine("html, body { margin: 0; padding: 0; width: 100%; height: auto; }")
        sb.appendLine("body { font-family: sans-serif; font-size: 18px; line-height: 1.8; padding: 16px; color: #333333; background: #ffffff; text-align: justify; -webkit-hyphens: auto; word-wrap: break-word; }")
        sb.appendLine("img { max-width: 100%; height: auto; display: block; margin: 1em auto; }")
        sb.appendLine("h1, h2, h3, h4, h5, h6 { margin-top: 1.5em; margin-bottom: 0.5em; line-height: 1.3; }")
        sb.appendLine("p { margin-bottom: 0.5em; margin-top: 0; text-indent: 2em; }")
        sb.appendLine("a { color: #1976D2; text-decoration: none; }")
        sb.appendLine("div, span { font-family: inherit; font-size: inherit; line-height: inherit; color: inherit; }")
        sb.appendLine(""".chapter-divider { height: 2px; background: linear-gradient(to right, transparent, #ccc, transparent); margin: 2em 0; border: none; }""")
        sb.appendLine("</style>")
        sb.appendLine("</head>")
        sb.appendLine("<body>")

        chapters.forEach { chapter ->
            // Add anchor for chapter navigation
            sb.appendLine("<a id=\"chapter_${chapter.index}\"></a>")
            sb.appendLine("<h2 style=\"text-align: center; text-indent: 0; margin: 1em 0;\">${escapeHtml(chapter.title)}</h2>")
            sb.appendLine("<hr class=\"chapter-divider\">")

            // Fix relative image paths to absolute file:// paths
            var content = chapter.content
            // Match src="..." (any image path, not just those starting with non-h)
            content = content.replace(Regex("""src="([^"]+)"""")) { match ->
                val path = match.groupValues[1]
                if (path.startsWith("http") || path.startsWith("file:") || path.startsWith("data:")) {
                    match.value
                } else {
                    val resolvedPath = resolveRelativePath(path, extractDir)
                    """src="$resolvedPath""""
                }
            }
            content = content.replace(Regex("""src='([^']+)'""")) { match ->
                val path = match.groupValues[1]
                if (path.startsWith("http") || path.startsWith("file:") || path.startsWith("data:")) {
                    match.value
                } else {
                    val resolvedPath = resolveRelativePath(path, extractDir)
                    """src='$resolvedPath'"""
                }
            }
            // Also fix background-image: url(...) in CSS - handle paths with spaces and special chars
            content = content.replace(Regex("""url\(\s*(['"]?)([^)]+?)\1\s*\)""")) { match ->
                val path = match.groupValues[2].trim()
                if (path.startsWith("http") || path.startsWith("file:") || path.startsWith("data:") || path.startsWith("#")) {
                    match.value
                } else {
                    val resolvedPath = resolveRelativePath(path, extractDir)
                    "url('$resolvedPath')"
                }
            }
            // Fix CSS href and @import paths
            content = content.replace(Regex("""href="([^"]+\.css[^"]*)" """)) { match ->
                val path = match.groupValues[1]
                if (path.startsWith("http") || path.startsWith("file:")) {
                    match.value
                } else {
                    val resolvedPath = resolveRelativePath(path, extractDir)
                    """href="$resolvedPath" """
                }
            }

            // Strip <html>, <head>, <body> tags since we're embedding in our own page
            content = content.replace(Regex("""(?s)<html[^>]*>"""), "")
            content = content.replace(Regex("""(?s)</html>"""), "")
            content = content.replace(Regex("""(?s)<head>.*?</head>"""), "")
            content = content.replace(Regex("""(?s)<body[^>]*>"""), "")
            content = content.replace(Regex("""(?s)</body>"""), "")
            // Remove DOCTYPE
            content = content.replace(Regex("""(?i)<!DOCTYPE[^>]*>"""), "")

            sb.appendLine(content)
            sb.appendLine("<hr class=\"chapter-divider\">")
        }

        sb.appendLine("</body>")
        sb.appendLine("</html>")

        mergedFile.writeText(sb.toString(), Charsets.UTF_8)

        return "file://${mergedFile.absolutePath}"
    }

    /**
     * Resolve a relative path to an absolute file:// path within the extract directory.
     */
    private fun resolveRelativePath(relativePath: String, extractDir: File): String {
        // Normalize the path (handle ../ etc.)
        val targetFile = File(extractDir, relativePath).canonicalFile
        return if (targetFile.exists()) {
            "file://${targetFile.absolutePath}"
        } else {
            // File doesn't exist yet, still return the resolved path
            "file://${targetFile.absolutePath}"
        }
    }

    private fun findOpfPath(zipFile: ZipFile): String? {
        return try {
            val containerEntry = zipFile.getEntry("META-INF/container.xml")
            if (containerEntry == null) {
                return "OEBPS/content.opf"
            }
            val containerContent = zipFile.getInputStream(containerEntry).readBytes().toString(Charsets.UTF_8)
            val fullPathMatch = Regex("""full-path="([^"]+)"""").find(containerContent)
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
                    val hrefMatch = Regex("""href="([^"]+)"""").find(line)
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

    /**
     * Extract cover image to a file in the extract directory.
     * This allows both Compose (via file path) and WebView (via file:// URI) to display it.
     */
    private fun extractCoverImageToFile(zipFile: ZipFile, opfContent: String, opfPath: String, extractDir: File): String? {
        return try {
            val opfDir = opfPath.substringBeforeLast("/", "")
            val lines = opfContent.split("\n")
            for (line in lines) {
                if (line.contains("cover") && (line.contains("image") || line.contains("png") || line.contains("jpeg") || line.contains("jpg") || line.contains("gif"))) {
                    val hrefMatch = Regex("""href="([^"]+)"""").find(line)
                    if (hrefMatch != null) {
                        val coverPath = hrefMatch.groupValues[1]
                        val fullPath = if (opfDir.isNotEmpty()) "$opfDir/$coverPath" else coverPath
                        val entry = zipFile.getEntry(fullPath)
                        if (entry != null) {
                            // Copy to a known location
                            val coverFile = File(extractDir, "_ireader_cover.jpg")
                            zipFile.getInputStream(entry).use { input ->
                                coverFile.outputStream().use { output ->
                                    input.copyTo(output)
                                }
                            }
                            Timber.d("Cover image extracted to: ${coverFile.absolutePath}")
                            return coverFile.absolutePath
                        }
                    }
                }
            }
            // Also try common cover filenames as fallback
            val commonCoverNames = listOf("cover.jpg", "cover.png", "Cover.jpg", "Cover.png",
                "OEBPS/cover.jpg", "OEBPS/cover.png", "OEBPS/Cover.jpg", "OEBPS/Cover.png",
                "Images/cover.jpg", "Images/cover.png", "images/cover.jpg", "images/cover.png")
            for (name in commonCoverNames) {
                val entry = zipFile.getEntry(name)
                if (entry != null) {
                    val coverFile = File(extractDir, "_ireader_cover.jpg")
                    zipFile.getInputStream(entry).use { input ->
                        coverFile.outputStream().use { output ->
                            input.copyTo(output)
                        }
                    }
                    Timber.d("Cover image extracted (fallback) to: ${coverFile.absolutePath}")
                    return coverFile.absolutePath
                }
            }
            null
        } catch (e: Exception) {
            Timber.e(e, "Cover extraction failed")
            null
        }
    }

    private fun extractChapters(zipFile: ZipFile, opfContent: String, opfPath: String): List<EpubChapter> {
        val chapters = mutableListOf<EpubChapter>()
        val opfDir = opfPath.substringBeforeLast("/", "")

        try {
            // Parse manifest to get id -> href mapping
            val manifestMatch = Regex("<manifest[^>]*>(.*?)</manifest>", RegexOption.DOT_MATCHES_ALL).find(opfContent)
            val manifestContent = manifestMatch?.groupValues?.get(1) ?: ""

            val itemMap = mutableMapOf<String, String>()
            Regex("""<item\s+[^>]*id="([^"]+)"\s+[^>]*href="([^"]+)"""").findAll(manifestContent).forEach { match ->
                itemMap[match.groupValues[1]] = match.groupValues[2]
            }
            Regex("""<item\s+[^>]*href="([^"]+)"\s+[^>]*id="([^"]+)"""").findAll(manifestContent).forEach { match ->
                itemMap[match.groupValues[2]] = match.groupValues[1]
            }

            // Parse spine for reading order
            val spineMatch = Regex("<spine[^>]*>(.*?)</spine>", RegexOption.DOT_MATCHES_ALL).find(opfContent)
            val spineContent = spineMatch?.groupValues?.get(1) ?: ""

            Regex("""idref="([^"]+)"""").findAll(spineContent).forEach { match ->
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
                title = Regex("""title="([^"]+)"""").find(htmlContent)?.groupValues?.get(1)?.trim()
            }
            if (title.isNullOrBlank() || title in meaninglessTitles) {
                val divMatch = Regex("""<div[^>]*class="[^"]*calibre2[^"]*"[^>]*>([^<]+)</div>""").find(htmlContent)
                title = divMatch?.groupValues?.get(1)?.trim()
            }
            if (title.isNullOrBlank() || title in meaninglessTitles) null else title
        } catch (e: Exception) {
            null
        }
    }

    private fun escapeHtml(text: String): String {
        return text
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
    }
}
