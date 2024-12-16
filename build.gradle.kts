import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    id("org.jetbrains.kotlin.plugin.compose")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {

    implementation(compose.desktop.currentOs)
    implementation("br.com.devsrsouza.compose.icons:font-awesome:1.1.1")
    implementation("br.com.devsrsouza.compose.icons:feather:1.1.1")
    implementation("org.jetbrains.compose.material3:material3-desktop:1.2.1")
    implementation("io.github.alexzhirkevich:compottie:2.0.0-rc01")

    implementation(compose.foundation)


}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "journal"
            packageVersion = "1.0.0"
        }
    }
}
