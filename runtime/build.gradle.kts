import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    id("module.publication")
    id("org.jetbrains.compose") version "1.5.11"  // Install compose compiler, TODO: use version of compose compiler we build locally
}

val dummy = Attribute.of("dummy", String::class.java)
kotlin {
    jvm("desktop")
    androidTarget {
        publishLibraryVariants("release")
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    // TODO: iosX64()
    // TODO: iosArm64()
    // TODO: iosSimulatorArm64()
    // TODO: linuxX64()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.22")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
                implementation("androidx.collection:collection:1.4.0")
            }
        }

        val jvmMain by creating {
            dependencies {
                dependsOn(commonMain)
            }
        }

        val androidMain by getting {
            dependencies {
                dependsOn(jvmMain)
            }
        }

        val desktopMain by getting {
            dependencies {
                dependsOn(jvmMain)
            }
        }
    }
}

tasks.withType(KotlinCompile::class).all {
    kotlinOptions {
        jvmTarget = "11"
        freeCompilerArgs = listOf("-Xjvm-default=all")
    }
}

android {
    namespace = "androidx.compose.runtime"
    compileSdk = 33
    defaultConfig {
        minSdk = 21
    }
}
