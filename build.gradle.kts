import com.matthewprenger.cursegradle.CurseArtifact
import com.matthewprenger.cursegradle.CurseProject
import com.matthewprenger.cursegradle.CurseRelation
import java.time.Instant
import java.time.format.DateTimeFormatter

fun property(key: String) = project.findProperty(key).toString()
fun optionalProperty(key: String) = project.findProperty(key)?.toString()

apply(from = "https://gist.githubusercontent.com/Harleyoc1/4d23d4e991e868d98d548ac55832381e/raw/applesiliconfg.gradle")

plugins {
    id("java")
    id("net.minecraftforge.gradle")
    id("org.parchmentmc.librarian.forgegradle")
    id("idea")
    id("maven-publish")
    id("com.harleyoconnor.translationsheet") version "0.1.1"
    id("com.matthewprenger.cursegradle") version "1.4.0"
    id("com.modrinth.minotaur") version "2.+"
    id("com.harleyoconnor.autoupdatetool") version "1.0.9"
}

repositories {
    mavenCentral()
    maven("https://ldtteam.jfrog.io/ldtteam/modding/") //patchouli
    maven("https://www.cursemaven.com") {
        content {
            includeGroup("curse.maven")
        }
    }
    flatDir {
        dirs("libs")
    }
    maven("https://maven.su5ed.dev/releases")


    maven("https://harleyoconnor.com/maven")
    maven("https://squiddev.cc/maven/") //cc-twekaed
}

val modName = property("modName")
val modId = property("modId")
val modVersion = property("modVersion")
val mcVersion = property("mcVersion")

version = "$mcVersion-$modVersion"
group = property("group")

minecraft {
    mappings(property("mappingsChannel"), property("mappingsVersion"))
    accessTransformer(file("src/main/resources/META-INF/accesstransformer.cfg"))

    runs {
        create("client") {
            applyDefaultConfiguration()

            if (project.hasProperty("mcUuid")) {
                args("--uuid", property("mcUuid"))
            }
            if (project.hasProperty("mcUsername")) {
                args("--username", property("mcUsername"))
            }
            if (project.hasProperty("mcAccessToken")) {
                args("--accessToken", property("mcAccessToken"))
            }
        }

        create("server") {
            applyDefaultConfiguration("run-server")
        }

        create("data") {
            applyDefaultConfiguration()

            args(
                "--mod", modId,
                "--all",
                "--output", file("src/generated/resources/"),
                "--existing", file("src/main/resources"),
                "--existing-mod", "dynamictrees",
                "--existing-mod", "edumia"
            )
        }
    }
}

sourceSets.main.get().resources {
    srcDir("src/generated/resources")
    srcDir("src/localization/resources")
}

dependencies {
    minecraft("net.minecraftforge:forge:$mcVersion-${property("forgeVersion")}")
    minecraftLibrary( fg.deobf("dev.su5ed.sinytra:Connector:1.0.0-beta.43+1.20.1"))
    implementation(fg.deobf("com.ferreusveritas.dynamictrees:DynamicTrees-$mcVersion:${property("dynamicTreesVersion")}"))

    implementation(fg.deobf("com.ferreusveritas.dynamictreesplus:DynamicTreesPlus-$mcVersion:${property("dynamicTreesPlusVersion")}"))
    implementation(fg.deobf("curse.maven:jade-324717:${property("jadeVersion")}"))

    runtimeOnly(fg.deobf("mezz.jei:jei-$mcVersion-forge:${property("jeiVersion")}"))
    runtimeOnly(fg.deobf("curse.maven:SereneSeasons-291874:${property("ssVersion")}"))
    runtimeOnly(fg.deobf("vazkii.patchouli:Patchouli:${property("patchouliVersion")}"))

    runtimeOnly(fg.deobf("cc.tweaked:cc-tweaked-$mcVersion-core:${property("ccVersion")}"))
    runtimeOnly(fg.deobf("cc.tweaked:cc-tweaked-$mcVersion-forge:${property("ccVersion")}"))

    runtimeOnly(fg.deobf("curse.maven:forgified-fabric-api-889079:5387432"))

    runtimeOnly(fg.deobf("curse.maven:fabric-api-306612:4902738"))

    // Correct local library inclusion
    implementation(fg.deobf(files("libs/edumia-1.4.0-1.20.1.jar")))


}

translationSheet {
    sheetId.set("1xjxEh2NdbeV_tQc6fDHPgcRmtchqCZJKt--6oifq1qc")
    sectionColour.set(0xF9CB9C)
    sectionPattern.set("Dynamic Trees Edumia")
    outputDirectory.set(file("src/localization/resources/assets/dtedumia/lang/"))
    useJson()
}

tasks.jar {
    manifest.attributes(
        "Specification-Title" to project.name,
        "Specification-Vendor" to "reiniertheghost",
        "Specification-Version" to "1",
        "Implementation-Title" to project.name,
        "Implementation-Version" to project.version,
        "Implementation-Vendor" to "reiniertheghost",
        "Implementation-Timestamp" to DateTimeFormatter.ISO_INSTANT.format(Instant.now())
    )

    archiveBaseName.set(modName)
    finalizedBy("reobfJar")
}

java {
    withSourcesJar()

    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

val changelogFile = file("build/changelog.txt")

curseforge {
    val curseApiKey = optionalProperty("curseApiKey") ?: System.getenv("CURSEFORGE_API_KEY")
    if (curseApiKey == null) {
        project.logger.warn("API Key for CurseForge not detected; uploading will be disabled.")
        return@curseforge
    }

    apiKey = curseApiKey

    project {
        id = "852886"

        addGameVersion(mcVersion)

        changelog = changelogFile
        changelogType = "markdown"
        releaseType = optionalProperty("versionType") ?: "release"

        addArtifact(tasks.findByName("sourcesJar"))

        mainArtifact(tasks.findByName("jar")) {
            relations {
                requiredDependency("dynamictrees")
                requiredDependency("edumia")
                optionalDependency("dynamictreesplus")
            }
        }
    }
}

modrinth {
    val modrinthToken = optionalProperty("modrinthToken") ?: System.getenv("MODRINTH_TOKEN")
    if (modrinthToken == null) {
        project.logger.warn("Token for Modrinth not detected; uploading will be disabled.")
        return@modrinth
    }

    token.set(modrinthToken)
    projectId.set(modId)
    versionNumber.set("$mcVersion-$modVersion")
    versionType.set(optionalProperty("versionType") ?: "release")
    uploadFile.set(tasks.jar.get())
    gameVersions.add(mcVersion)
    if (changelogFile.exists()) {
        changelog.set(changelogFile.readText())
    }
}

tasks.withType<GenerateModuleMetadata> {
    enabled = false
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = "$modName-$mcVersion"
            version = modVersion

            from(components["java"])

            pom {
                name.set(modName)
                url.set("https://github.com/DynamicTreesTeam/$modName")
                licenses {
                    license {
                        name.set("MIT")
                        url.set("https://mit-license.org")
                    }
                }
                developers {
                    developer {
                        id.set("supermassimo")
                        name.set("Max Hyper")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/DynamicTreesTeam/$modName.git")
                    developerConnection.set("scm:git:ssh://github.com/DynamicTreesTeam/$modName.git")
                    url.set("https://github.com/DynamicTreesTeam/$modName")
                }
            }

            pom.withXml {
                val element = asElement()

                // Clear dependencies.
                for (i in 0 until element.childNodes.length) {
                    val node = element.childNodes.item(i)
                    if (node?.nodeName == "dependencies") {
                        element.removeChild(node)
                    }
                }
            }
        }
    }
    repositories {
        maven("file:///${project.projectDir}/mcmodsrepo")
        val mavenUsername = optionalProperty("harleyOConnorMavenUsername") ?: System.getenv("MAVEN_USERNAME")
        val mavenPassword = optionalProperty("harleyOConnorMavenPassword") ?: System.getenv("MAVEN_PASSWORD")
        if (mavenUsername == null || mavenPassword == null) {
            logger.warn("Credentials for maven not detected; it will be disabled.")
            return@repositories
        }
        maven("https://harleyoconnor.com/maven") {
            name = "HarleyOConnor"
            credentials {
                username = mavenUsername
                password = mavenPassword
            }
        }
    }
}

autoUpdateTool {
    minecraftVersion.set(mcVersion)
    version.set(modVersion)
    versionRecommended.set(property("versionRecommended") == "true")
    changelogOutputFile.set(changelogFile)
    updateCheckerFile.set(file("temp/version_info.json"))
}

tasks.autoUpdate {
    doLast {
        modrinth.changelog.set(changelogFile.readText())
    }
    finalizedBy("publishMavenJavaPublicationToHarleyOConnorRepository", "curseforge", "modrinth")
}

fun net.minecraftforge.gradle.common.util.RunConfig.applyDefaultConfiguration(runDirectory: String = "run") {
    workingDirectory = file(runDirectory).absolutePath

    property("forge.logging.markers", "REGISTRIES,REGISTRYDUMP")
    property("forge.logging.console.level", "debug")

    property("mixin.env.remapRefMap", "true")
    property("mixin.env.refMapRemappingFile", "${buildDir}/createSrgToMcp/output.srg")

    mods {
        create(modId) {
            source(sourceSets.main.get())
        }
    }
}

fun com.matthewprenger.cursegradle.CurseExtension.project(action: CurseProject.() -> Unit) {
    this.project(closureOf(action))
}

fun CurseProject.mainArtifact(artifact: Task?, action: CurseArtifact.() -> Unit) {
    this.mainArtifact(artifact, closureOf(action))
}

fun CurseArtifact.relations(action: CurseRelation.() -> Unit) {
    this.relations(closureOf(action))
}
