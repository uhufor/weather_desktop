import org.jetbrains.compose.compose
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.10"
    kotlin("plugin.serialization") version "1.6.10"
    kotlin("kapt") version "1.6.10"
    id("org.jetbrains.compose") version "1.1.1"
}

group = "me.larry"
version = "1.0"

dependencies {
    implementation(compose.desktop.currentOs)

    api("io.ktor:ktor-client-core:2.0.2")
    api("io.ktor:ktor-client-cio:2.0.2")
    api("io.ktor:ktor-client-serialization:2.0.2")
    api("io.ktor:ktor-client-content-negotiation:2.0.2")
    api("io.ktor:ktor-serialization-kotlinx-json:2.0.2")

    implementation("io.arrow-kt:arrow-core:1.1.2")
    implementation("io.arrow-kt:arrow-optics:1.1.2")
    kapt("io.arrow-kt:arrow-meta:1.6.0")
    implementation("io.arrow-kt:arrow-fx-coroutines:1.1.2")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}
