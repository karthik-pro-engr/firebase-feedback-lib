// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.android.library) apply false
    id("com.gradleup.nmcp.aggregation") version "1.2.0"
}
nmcpAggregation {
    centralPortal {
        username = System.getenv("SONATYPE_USERNAME")
        password = System.getenv("SONATYPE_PASSWORD")
        publishingType = "AUTOMATIC" // or "USER_MANAGED"
    }

    publishAllProjectsProbablyBreakingProjectIsolation()
}

subprojects {
    // Only configure signing if the module applies maven-publish
    plugins.withId("maven-publish") {
        plugins.withId("signing") {
            extensions.configure<SigningExtension>("signing") {
                val signingKey = System.getenv("SIGNING_KEY")
                val signingPassword = System.getenv("SIGNING_PASSWORD")

                if (!signingKey.isNullOrBlank() && !signingPassword.isNullOrBlank()) {
                    useInMemoryPgpKeys(signingKey, signingPassword)
                    sign(extensions.getByType<PublishingExtension>().publications)
                } else {
                    logger.lifecycle("Signing keys not found in env or gradle.properties; skipping signing.")
                }
            }
        }
    }
}