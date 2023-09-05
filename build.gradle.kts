plugins {
    id("java")
    kotlin("jvm") version "1.9.0"
    `maven-publish`
}

val ver = "1.0.1"

group = "me.superpenguin.superglue"
version = ver

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.19.4-R0.1-SNAPSHOT")
}

publishing.publications.create<MavenPublication>("maven") {
    groupId = "me.superpenguin.superglue"
    artifactId = "SuperTeamsAPI"
    version = ver

    from(components["java"])
}