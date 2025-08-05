pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/")
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.7.6"
}

stonecutter {
    create(rootProject) {
        versions("1.21.8", "1.21.5", "1.21.4", "1.21.1", "1.20.1")
        vcsVersion = "1.21.8"
    }
}