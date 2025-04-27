plugins {
    kotlin("jvm") version "2.1.10"
}

group = "net.thechance"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    //test
    testImplementation(kotlin("test"))


    implementation("io.insert-koin:koin-core:4.0.4")
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.10.0")


    //google truth
    testImplementation("com.google.truth:truth:1.4.2")

    //mockk
    testImplementation("io.mockk:mockk:1.13.16")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(23)
}