# Code Refactoring and Analysis Plugin

This plugin offers simple code refactoring and analysis tools, perfect for those working on Java Gradle applications outside of IntelliJ IDEA. No more missing the magic of `C-A-l` or dealing with ineffective LSP `:Format`.

## Setup

1. **Configuration:** You can adjust the configuration and modify task names as needed:
```kotlin
tasks.register("format") {
    it.dependsOn("spotlessApply")
}

tasks.register("analyze") {
    it.dependsOn("checkstyleMain")
}
```

2. **Push to Local Maven Repository:**

`gradle publishToMavenLocal`

3. **Integrate in Your Project:**
```
buildscript {
    repositories {
        mavenLocal()
        gradlePluginPortal()
    }
    dependencies {
        classpath 'com.simple:simple-formatter:0.0.1'
    }
}

apply plugin: 'com.simple.simple-formatter'

```
