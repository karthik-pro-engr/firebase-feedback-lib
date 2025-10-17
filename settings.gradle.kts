pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        maven {
            url = uri("https://maven.pkg.github.com/karthik-pro-engr/build-logic")
            credentials {
                username =
                    providers.gradleProperty("gpr.user").orNull ?: System.getenv("GITHUB_ACTOR")
                password =
                    providers.gradleProperty("gpr.token").orNull ?: System.getenv("GITHUB_TOKEN")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "karthik.pro.engr.android.library") {
                useModule("karthik.pro.engr:android-library-plugin:${requested.version}")
            }

        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "firebase-feedback-lib"
include(":feedbacklib")
