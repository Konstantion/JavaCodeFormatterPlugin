package com.simple

import com.diffplug.gradle.spotless.SpotlessExtension
import com.diffplug.gradle.spotless.SpotlessPlugin
import com.diffplug.spotless.LineEnding
import org.checkerframework.gradle.plugin.CheckerFrameworkExtension
import org.checkerframework.gradle.plugin.CheckerFrameworkPlugin
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.plugins.quality.CheckstyleExtension
import org.gradle.api.plugins.quality.CheckstylePlugin
import org.gradle.api.plugins.quality.PmdPlugin
import org.gradle.api.resources.TextResource
import org.gradle.kotlin.dsl.getByType

class JavaFormatter : org.gradle.api.Plugin<Project> {
    override fun apply(project: Project): Unit = with(project) {
        listOf(
            JavaPlugin::class,
            SpotlessPlugin::class,
            CheckerFrameworkPlugin::class,
            CheckstylePlugin::class,
            PmdPlugin::class
        ).forEach { plugins.apply(it.java) }

        extensions.getByType(JavaPluginExtension::class.java).apply {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }

        extensions.getByType<SpotlessExtension>().java { ->
            lineEndings = LineEnding.PLATFORM_NATIVE
            prettier(mapOf("prettier" to "2.7.1", "prettier-plugin-java" to "2.0.0"))
                .config(
                    mapOf(
                        "parser" to "java",
                        "tabWidth" to 4,
                        "printWidth" to 120,
                        "trailingComma" to "all",
                        "endOfLine" to "auto"
                    )
                )
        }

        extensions.getByType<CheckerFrameworkExtension>().apply {
            checkers = listOf("org.checkerframework.checker.nullness.NullnessChecker")
            extraJavacArgs = listOf(
                "-Werror",
                "-AsuppressWarnings=type.anno.before.modifier"
            )
        }

        extensions.getByType<CheckstyleExtension>().apply {
            toolVersion = "10.3.1"
            config = load("/checkstyle.xml")
            configProperties = mapOf("checkstyle.cache.file" to rootProject.file("${buildDir}/checkstyle.cache"))
        }

        tasks.register("format") {
            dependsOn("spotlessApply")
        }

        tasks.register("analyze") {
            dependsOn("checkstyleMain")
            //TODO: resolve this problem
            //dependsOn("checkerFrameworkMain")
        }
    }

    private fun Project.load(name: String): TextResource =
        resources
            .text
            .fromUri(JavaFormatter::class.java.getResource(name)!!)
}