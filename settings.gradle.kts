pluginManagement {
    repositories {
        // 阿里云镜像 - 优先
        maven("https://maven.aliyun.com/repository/gradle-plugin")
        maven("https://maven.aliyun.com/repository/google")
        maven("https://maven.aliyun.com/repository/public")
        // 原始仓库作为备用
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        // 国内镜像 - 优先
        maven("https://maven.aliyun.com/repository/google")
        maven("https://maven.aliyun.com/repository/public")
        maven("https://repo.huaweicloud.com/repository/maven/")
        // JitPack
        maven("https://jitpack.io")
        // 原始仓库作为备用
        google()
        mavenCentral()
    }
}

rootProject.name = "IReader"
include(":app")