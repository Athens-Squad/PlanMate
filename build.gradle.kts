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
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(23)
}
kover.reports.filters.includes {
    packages(
        "net.thechance.data",
        "net.thechance.logic",
        "net.thechance.presentation"
    )
}
kover.reports.verify.rule {
    groupBy = GroupingEntityType.CLASS

    minBound(80, coverageUnits = CoverageUnit.LINE)
    minBound(80, coverageUnits = CoverageUnit.BRANCH)
    minBound(80, coverageUnits = CoverageUnit.INSTRUCTION)
}