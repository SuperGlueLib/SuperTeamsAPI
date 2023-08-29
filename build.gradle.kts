plugins {
    id("java")
    kotlin("jvm") version "1.9.0"
    `maven-publish`
}

group = "me.superpenguin.superglue"
version = "0.0.1"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.19.4-R0.1-SNAPSHOT")
}