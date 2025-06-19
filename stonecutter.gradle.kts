plugins {
    id("dev.kikugie.stonecutter")
    id("org.jetbrains.changelog") version "2.2.0"
}
stonecutter active "1.21.6"

stonecutter registerChiseled tasks.register("chiseledBuild", stonecutter.chiseled) { 
    group = "project"
    ofTask("build")
}

stonecutter registerChiseled tasks.register("chiseledPublishMods", stonecutter.chiseled) {
    group = "project"
    ofTask("publishMods")
}

changelog {
    path = rootProject.file("CHANGELOG.md").path
}