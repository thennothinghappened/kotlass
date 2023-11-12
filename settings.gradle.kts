rootProject.name = "kotlass"
include(":shared")

pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
    plugins {
        kotlin("multiplatform").version("1.9.20")
        kotlin("plugin.serialization").version("1.9.20")
        id("com.android.library").version("7.4.2")
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            library("kotlinx-coroutines-core", "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
            library("ktor-client-core", "io.ktor:ktor-client-core:2.2.4")
            library("ktor-client-okhttp", "io.ktor:ktor-client-okhttp:2.2.4")
            library("ktor-client-ios", "io.ktor:ktor-client-ios:2.2.4")
            library("ktor-client-jvm", "io.ktor:ktor-client-jvm:2.2.4")
            library("ktor-client-resources", "io.ktor:ktor-client-resources:2.2.4")
            library("ktor-client-content-negotiation", "io.ktor:ktor-client-content-negotiation:2.2.4")
            library("ktor-serialization-kotlinx-json", "io.ktor:ktor-serialization-kotlinx-json:2.2.4")
            library("kotlinx-datetime", "org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
            library("kotlinx-serialization-json", "org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
            library("touchlab-kermit", "co.touchlab:kermit:2.0.2")
        }
    }
}
