plugins {
    // Convention Kotlin plugins support
    `kotlin-dsl`
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    // Gradle plugin portal to apply community plugins in convention plugins
    gradlePluginPortal()
}
