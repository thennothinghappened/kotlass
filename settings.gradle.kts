pluginManagement {

    val kotlinVersion = extra["kotlin.version"] as String
    val agpVersion = extra["agp.version"] as String

    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }

    resolutionStrategy {
        eachPlugin {
            if (requested.id.namespace == "com.android") {
                useModule("com.android.tools.build:gradle:$agpVersion")
            }
        }
    }

    plugins {
        kotlin("multiplatform") version kotlinVersion
        kotlin("plugin.serialization") version kotlinVersion
        id("com.android.library")
        id("maven-publish")
        id("org.jetbrains.kotlin.android") version kotlinVersion apply false
        id("org.jetbrains.dokka") version "1.8.10"
    }
}
rootProject.name = "kotlass"

