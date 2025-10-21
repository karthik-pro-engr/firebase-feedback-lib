plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.karthik.pro.engr.android.library)
    alias(libs.plugins.kotlin.compose)

}

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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}


dependencies {
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.androidx.material.icons.extended)
}
