plugins {
    `java-library`
    id("com.gradleup.shadow") version "8.3.5"
}

group = "com.github.greenfoot5"
version = "1.2.5"
description = "CastleSiege plugin for ConwyMC"
java.sourceCompatibility = JavaVersion.VERSION_21

repositories {
    mavenCentral()

    maven {
        credentials {
            username = project.property("globalUser").toString()
            password = project.property("globalPassword").toString()
        }
        name = "ConwyMC Global"
        url = uri("https://maven.pkg.github.com/Greenfoot5/ConwyMC-global")
    }
    maven {
        name = "Citizens"
        url = uri("https://repo.citizensnpcs.co/")
    }
    maven {
        name = "ProtocolLib"
        url = uri("https://repo.dmulloy2.net/repository/public/")
    }
    maven {
        name = "World[Edit/Guard]"
        url = uri("https://maven.enginehub.org/repo/")
    }
    maven {
        name = "Paper"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }

    maven {
        name = "MythicCraft"
        url = uri("https://mvn.lumine.io/repository/maven-public/")
    }
    // Disabled until nametagedit is updated
//    maven {
//        name = "NameTagEdit"
//        url = uri("https://ci.nametagedit.com/plugin/repository/everything/")
//    }
    maven {
        url = uri("https://repo.codemc.io/repository/maven-releases/")
    }
    maven {
        name = "UltimateAdvancementAPI"
        url = uri("https://nexus.frengor.com/repository/public/")
    }
    maven {
        url = uri("https://jitpack.io")
    }
    maven {
        name = "Voting"
        url = uri("https://nexus.bencodez.com/repository/maven-public/")
    }
    maven {
        name = "Aikar" // For NTE
        url = uri("https://repo.aikar.co/content/groups/aikar/")
    }
    // Backup in case a plugin's update isn't in a repo for some reason
    flatDir {
        dir("lib")
    }
}

dependencies {
    val scoreboardLibraryVersion = "2.2.2"
    compileOnly("io.papermc.paper:paper-api:1.21.3-R0.1-SNAPSHOT")

    implementation("commons-io:commons-io:2.18.0")
    implementation("dev.dejvokep:boosted-yaml:1.3.7")

    // Via /lib
    compileOnly("com.github.Greenfoot5:conwymc-global:1.0.5")
    compileOnly("com.nametagedit:nametagedit:4.5.24")

    compileOnly("net.citizensnpcs:citizensapi:2.0.33-SNAPSHOT")
    compileOnly("com.comphenix.protocol:ProtocolLib:5.1.0")
    compileOnly("com.sk89q.worldedit:worldedit-core:7.3.0")
    compileOnly("com.sk89q.worldedit:worldedit-bukkit:7.3.0")
    compileOnly("io.lumine:Mythic-Dist:5.8.0-SNAPSHOT")
    compileOnly("LibsDisguises:LibsDisguises:10.0.44")
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.12")
    compileOnly("com.frengor:ultimateadvancementapi:2.4.3")

    runtimeOnly("com.bencodez:votifierplus:1.3")

    // Scoreboard
    implementation("net.megavex:scoreboard-library-api:$scoreboardLibraryVersion")
    runtimeOnly("net.megavex:scoreboard-library-implementation:$scoreboardLibraryVersion")
    implementation("net.megavex:scoreboard-library-extra-kotlin:$scoreboardLibraryVersion") // Kotlin specific extensions (optional)
    runtimeOnly("net.megavex:scoreboard-library-modern:$scoreboardLibraryVersion:mojmap")
}

java {
    withJavadocJar()
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks {
    assemble {
        dependsOn(shadowJar)
    }

    compileJava {
        options.release = 21
    }

    // Relocating a Package
    shadowJar {
        // helper function to relocate a package into our package
        fun reloc(pkg: String) = relocate(pkg, "me.greenfoot5.castlesiege.libs.$pkg")

        reloc("dev.dejvokep.boostedyaml")
        reloc("net.megavex.scoreboardlibrary")
//        minimize()

        manifest {
            attributes["paperweight-mappings-namespace"] = "mojang"
        }
    }

    javadoc {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
    }
}