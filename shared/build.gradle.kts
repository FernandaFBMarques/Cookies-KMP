plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization")
    id("com.vanniktech.maven.publish") version "0.30.0"
}

group = "io.github.fernandafbmarques"
version = "0.1.3"

kotlin {
    jvmToolchain(17)

    androidTarget {
        publishLibraryVariants("release")
    }

    val iosX64 = iosX64()
    val iosArm64 = iosArm64()
    val iosSimulatorArm64 = iosSimulatorArm64()
    val macosX64 = macosX64()
    val macosArm64 = macosArm64()

    listOf(
        iosX64,
        iosArm64,
        iosSimulatorArm64,
        macosX64,
        macosArm64,
    ).forEach { target ->
        target.binaries.framework {
            baseName = "CookiesKMP"
            isStatic = true
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }

        val androidMain by getting {
            dependsOn(commonMain)
        }
        val androidUnitTest by getting {
            dependsOn(commonTest)
        }

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val macosX64Main by getting
        val macosArm64Main by getting

        val appleMain by creating {
            dependsOn(commonMain)
        }

        val iosMain by creating {
            dependsOn(appleMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val macosMain by creating {
            dependsOn(appleMain)
            macosX64Main.dependsOn(this)
            macosArm64Main.dependsOn(this)
        }

        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val macosX64Test by getting
        val macosArm64Test by getting

        val appleTest by creating {
            dependsOn(commonTest)
        }
        val iosTest by creating {
            dependsOn(appleTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
        val macosTest by creating {
            dependsOn(appleTest)
            macosX64Test.dependsOn(this)
            macosArm64Test.dependsOn(this)
        }
    }
}

android {
    namespace = "io.github.fernandafbmarques.cookies.kmp.core"
    compileSdk = 35

    defaultConfig {
        minSdk = 26
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

mavenPublishing {
    publishToMavenCentral(com.vanniktech.maven.publish.SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()

    coordinates(
        groupId = "io.github.fernandafbmarques",
        artifactId = "cookies-kmp-core",
        version = version.toString(),
    )

    pom {
        name.set("Cookies KMP Core")
        description.set("Kotlin Multiplatform shared cookies business logic.")
        url.set("https://github.com/FernandaFBMarques/Cookies-KMP")

        licenses {
            license {
                name.set("MIT License")
                url.set("https://opensource.org/license/mit/")
            }
        }

        developers {
            developer {
                id.set("FernandaFBMarques")
                name.set("Maria Fernanda de Freitas Barbosa Marques")
                url.set("https://github.com/FernandaFBMarques")
            }
        }

        scm {
            connection.set("scm:git:git://github.com/FernandaFBMarques/Cookies-KMP.git")
            developerConnection.set("scm:git:ssh://git@github.com/FernandaFBMarques/Cookies-KMP.git")
            url.set("https://github.com/FernandaFBMarques/Cookies-KMP")
        }
    }
}
