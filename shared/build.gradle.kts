import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.dokka)
    id("maven-publish")
}

val dokkaOutputDir = "${layout.buildDirectory}/dokka"
tasks.dokkaHtml {
    outputDirectory.set(file(dokkaOutputDir))
}

val dokkaJar by tasks.creating(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    archiveClassifier = "javadoc"
    from(tasks.dokkaHtml)
}

val localProperties = Properties()
localProperties.load(project.rootProject.file("local.properties").inputStream())
val usr: String = localProperties.getProperty("gpr.usr")
val key: String = localProperties.getProperty("gpr.key")

val _groupId = "com.youxiang8727"
val _artifactId = "pretty_kmm_composable"
val _version = "1.0.0"

kotlin {

    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
        publishAllLibraryVariants()
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()


    sourceSets {
        commonMain.dependencies {
            // put your Multiplatform dependencies here
        }

        androidMain.dependencies {
            implementation(kotlin("stdlib-jdk11"))
        }

        iosMain.dependencies {

        }
    }
}

android {
    namespace = "com.example.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    sourceSets {
        getByName("main") {
            manifest.srcFile("src\\androidMain\\AndroidManifest.xml")
            java.srcDir("src\\androidMain\\kotlin")
        }
    }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://github.com/YouXiang8727/PrettyKmmComposableLib")
            credentials {
                username = usr
                password = key
            }
        }
    }

    publications {
        publications.configureEach {
            if (this is MavenPublication) {
                artifact(dokkaJar)
                pom {
                    name = "$_groupId.$_artifactId"
                    description = "KMM Composable Library"
                    url = "https://github.com/YouXiang8727/PrettyKmmComposableLib"
                }
            }
        }
    }
}
