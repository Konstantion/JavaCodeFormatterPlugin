plugins {
    kotlin("jvm") version "1.8.0"
    id("maven-publish")
    `java-gradle-plugin`
    application
}

group = "com.simple"
version = "0.0.1"

kotlin {
    jvmToolchain(8)
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation("com.diffplug.spotless:spotless-plugin-gradle:6.10.0")
    implementation("org.checkerframework:checkerframework-gradle-plugin:0.6.23")
    implementation("gradle.plugin.com.keith.gradle:keith-checkstyle-plugin:1.0.3")
}

gradlePlugin.plugins.create("JavaFormatter") {
    id = "$group.simple-formatter"
    implementationClass = "$group.$name"
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            groupId = group.toString()
            artifactId = "simple-formatter"
            version = version
        }
    }
    repositories {
        mavenLocal()
    }
}