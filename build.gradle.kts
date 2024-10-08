import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0"
}

group = "ruben.eduardo"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    // Note, if you develop a library, you should use compose.desktop.common.
    // compose.desktop.currentOs should be used in launcher-sourceSet
    // (in a separate module for demo project and in testMain).
    // With compose.desktop.common you will also lose @Preview functionality
    implementation(compose.desktop.currentOs)
    implementation("org.postgresql:postgresql:42.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    implementation(compose.materialIconsExtended)
    implementation(compose.material3)
    // https://mvnrepository.com/artifact/io.coil-kt/coil-compose
    // https://mvnrepository.com/artifact/io.coil-kt.coil3/coil-compose
    implementation("io.coil-kt.coil3:coil-compose:3.0.0-alpha09")
    // https://mvnrepository.com/artifact/com.airbnb.android/lottie-compose
    // https://mvnrepository.com/artifact/com.airbnb.android/lottie
// https://mvnrepository.com/artifact/io.coil-kt.coil3/coil-gif














}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "demo"
            packageVersion = "1.0.0"
        }
    }
}
