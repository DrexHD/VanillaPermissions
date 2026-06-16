import org.jetbrains.changelog.Changelog
import org.jetbrains.changelog.ChangelogPluginExtension

plugins {
    id("net.fabricmc.fabric-loom") version "1.17-SNAPSHOT"
    id("maven-publish")
    id("me.modmuss50.mod-publish-plugin") version "1.1.0"
    id("org.jetbrains.changelog")
}

version = findProperty("mod_version") as String + "+" + findProperty("minecraft_version")
group = findProperty("maven_group") as String

base {
    archivesName = findProperty("archives_base_name") as String
}

java {
    toolchain.languageVersion = JavaLanguageVersion.of(findProperty("java_version") as String)
}

repositories {
    mavenCentral()
    maven("https://maven.nucleoid.xyz/")
}

loom {
    splitEnvironmentSourceSets()

    runConfigs.all {
        ideConfigGenerated(true)
    }
    accessWidenerPath = file("src/main/resources/vanilla-permissions.accesswidener")
}

fun DependencyHandlerScope.includeMod(
    dep: String,
    configure: ExternalModuleDependency.() -> Unit = {}
) {
    val dependency = implementation(dep) as ExternalModuleDependency
    dependency.configure()
    include(dependency)
}

dependencies {
    minecraft("com.mojang:minecraft:${findProperty("minecraft_version")}")
    implementation("net.fabricmc:fabric-loader:${findProperty("loader_version")}")

    implementation("net.fabricmc.fabric-api:fabric-api:${findProperty("fabric_version")}")

    includeMod("me.lucko:fabric-permissions-api:${findProperty("fabric_permissions_version")}") {
        exclude(group="net.fabricmc.fabric-api")
    }
}

publishMods {
    file.set(tasks.jar.get().archiveFile)
    type.set(STABLE)
    changelog.set(fetchChangelog())

    displayName = "VanillaPermissions ${version.get()}"
    modLoaders.add("fabric")
    modLoaders.add("quilt")


    curseforge {
        accessToken = providers.environmentVariable("CURSEFORGE_TOKEN")
        projectId = "686569"
        minecraftVersions.addAll(findProperty("curseforge_minecraft_versions")!!.toString().split(", "))
    }
    modrinth {
        accessToken = providers.environmentVariable("MODRINTH_TOKEN")
        projectId = "fdZkP5Bb"
        minecraftVersions.addAll(findProperty("modrinth_minecraft_versions")!!.toString().split(", "))
    }
    github {
        accessToken = providers.environmentVariable("GITHUB_TOKEN")
        repository = providers.environmentVariable("GITHUB_REPOSITORY").getOrElse("DrexHD/VanillaPermissions")
        commitish = providers.environmentVariable("GITHUB_REF_NAME").getOrElse("main")
    }
}

tasks {
    processResources {
        val props = mapOf(
            "version" to project.version,
            "javaVersion" to findProperty("java_version")
        )

        inputs.properties(props)

        filesMatching(listOf("fabric.mod.json", "*.mixins.json")) {
            expand(props)
        }
    }
}

fun fetchChangelog(): String {
    val log = rootProject.extensions.getByType<ChangelogPluginExtension>()
    val modVersion = findProperty("mod_version")!!.toString()
    return if (log.has(modVersion)) {
        log.renderItem(
            log.get(modVersion).withHeader(false),
            Changelog.OutputType.MARKDOWN
        )
    } else {
        ""
    }
}