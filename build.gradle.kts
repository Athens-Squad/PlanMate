import kotlinx.kover.gradle.plugin.dsl.CoverageUnit
import kotlinx.kover.gradle.plugin.dsl.GroupingEntityType

plugins {
    kotlin("jvm") version "2.1.10"
    id("org.jetbrains.kotlinx.kover") version "0.9.1"
}

group = "net.thechance"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    //test
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.0")
    //google truth
    testImplementation("com.google.truth:truth:1.4.2")
    //coroutine test
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3") // or your version
    //mockk
    testImplementation("io.mockk:mockk:1.13.16")
    //di
    implementation("io.insert-koin:koin-core:4.0.4")

    // Kotlin coroutine dependency
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")

    // MongoDB Kotlin driver dependency
    implementation("org.mongodb:mongodb-driver-kotlin-coroutine:4.10.1")


}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(23)
}
kover.reports.filters.includes {
    packages(
        "data",
        "logic",
        "ui"
    )
}
kover.reports.verify.rule {
    groupBy = GroupingEntityType.CLASS

    minBound(80, coverageUnits = CoverageUnit.LINE)
    minBound(80, coverageUnits = CoverageUnit.BRANCH)
    minBound(80, coverageUnits = CoverageUnit.INSTRUCTION)
}