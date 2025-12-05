pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/")
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.kikugie.dev/snapshots")
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.8-alpha.14"
}

stonecutter {
    create(rootProject) {
        versions("1.21.11", "1.21.10", "1.21.8", "1.21.5", "1.21.4", "1.21.1", "1.20.1")
        vcsVersion = "1.21.11"
    }
}