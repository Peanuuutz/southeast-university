import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")

    id("fabric-loom")
}

base {
    val archivesName: String by project
    this.archivesName.set(archivesName)
}

repositories {
    mavenCentral()
}

dependencies {
    val minecraftVersion: String by project
    minecraft("com.mojang:minecraft:$minecraftVersion")
    val mappingsVersion: String by project
    mappings("net.fabricmc:yarn:$mappingsVersion:v2")

    val fabricLoaderVersion: String by project
    modImplementation("net.fabricmc:fabric-loader:$fabricLoaderVersion")
    val fabricApiVersion: String by project
    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricApiVersion")

    val fabricKotlinVersion: String by project
    modImplementation("net.fabricmc:fabric-language-kotlin:$fabricKotlinVersion")

    val tomlktVersion: String by project
    implementation(include("net.peanuuutz:tomlkt-jvm:$tomlktVersion")!!)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(8))
    }
    withSourcesJar()
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }

    withType<KotlinCompile> {
        kotlinOptions { 
            jvmTarget = "1.8"
        }
    }

    processResources {
        inputs.property("version", project.version)
        filesMatching("fabric.mod.json") {
            expand(mapOf("version" to project.version))
        }
    }
 
    jar {
        from("LICENSE")
    }
}

loom {
    accessWidenerPath.set(file("src/main/resources/seu.accesswidener"))
}