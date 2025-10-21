plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.karthik.pro.engr.android.library)
    alias(libs.plugins.kotlin.compose)
    `maven-publish`
    signing
    id("org.jetbrains.dokka") version "2.1.0"
}

group = "io.github.karthik-pro-engr"
version = "0.0.1-beta"

android {
    namespace = "com.karthik.pro.engr.feedback.api"
    compileSdk = 36
    buildFeatures { compose = true }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.6.0"  // MUST match in app & lib
    }
    defaultConfig {
        minSdk = 28

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        create("beta") {
            // Start from release so beta is signed the same and uses release-like settings
            initWith(getByName("release"))
            isMinifyEnabled=false
        }
    }

    // ✅ Ensure publishing component exists
    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                 from(components["release"])
                groupId = project.group.toString()
                artifactId = "firebase-feedback-api"
                version = project.version.toString()

                pom {
                    name.set("Firebase Feedback API")
                    description.set("Public API for in-app beta tester feedback flows — lightweight composables, events, and interfaces for integrating feedback UI without bundling Firebase App Distribution. ")
                    url.set("https://github.com/karthik-pro-engr/firebase-feeback-lib")
                    licenses {
                        license {
                            name.set("Apache License 2.0")
                            url.set("https://www.apache.org/licenses/LICENSE-2.0")
                        }
                    }
                    developers {
                        developer {
                            id.set("karthik.pro.engr")
                            name.set("Karthik Pro Engr")
                            email.set("karthik.pro.engr@gmail.com")
                        }
                    }
                    scm {
                        url.set("https://github.com/karthik-pro-engr/firebase-feeback-lib")
                        connection.set("scm:git:https://github.com/karthik-pro-engr/firebase-feeback-lib.git")
                        developerConnection.set("scm:git:ssh://github.com:karthik-pro-engr/firebase-feeback-lib.git")
                    }
                }

            }
        }
    }
}


dependencies {
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.androidx.material.icons.extended)
}
