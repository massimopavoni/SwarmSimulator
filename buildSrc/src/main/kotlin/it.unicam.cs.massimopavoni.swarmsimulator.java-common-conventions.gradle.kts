plugins {
    // Java support
    java
    // Tests JaCoCo reports support
    jacoco
}

repositories {
    // Maven repository
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

dependencies {
    // Use Guava for common utilities
    implementation("com.google.guava:guava:32.+")
    // Use JUnit Jupiter for testing
    testImplementation("org.junit.jupiter:junit-jupiter:5.+")
}

tasks.javadoc {
    setDestinationDir(file("$rootDir/docs/javadoc/${project.name}"))
}

tasks.named<Test>("test") {
    dependsOn(tasks.javadoc)
    // Use JUnit Platform for unit tests
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    // Enable xml reports for SonarQube
    reports {
        xml.required.set(true)
    }
}

// Add sonar dependency on test reports
rootProject.tasks.named("sonar").get().dependsOn(tasks.jacocoTestReport)
