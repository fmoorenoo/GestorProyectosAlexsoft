import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.kotlin.plugin.serialization")
}

group = "ies.haria"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    // Compose
    implementation(compose.desktop.currentOs)
    implementation(compose.material3)
    implementation(compose.materialIconsExtended)
    implementation(compose.foundation)

    // Voyager
    implementation("cafe.adriel.voyager:voyager-navigator:${property("voyager.version")}")
    implementation("cafe.adriel.voyager:voyager-transitions:${property("voyager.version")}")
    implementation("cafe.adriel.voyager:voyager-koin:${property("voyager.version")}")

    // Ktor
    implementation("io.ktor:ktor-client-core:${property("ktor.version")}")
    implementation("io.ktor:ktor-client-cio:${property("ktor.version")}")
    implementation("io.ktor:ktor-client-content-negotiation:${property("ktor.version")}")
    implementation("io.ktor:ktor-serialization-kotlinx-json:${property("ktor.version")}")
    implementation("io.ktor:ktor-client-logging:${property("ktor.version")}")
    implementation("org.slf4j:slf4j-simple:2.0.9")


    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${property("coroutines.version")}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:${property("coroutines.version")}")
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "GestionProyectos"
            packageVersion = "1.0.0"
        }
    }
}
