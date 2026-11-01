// Top-level build file. Plugin versions are declared here and applied per-module.
plugins {
    id("com.android.application") version "8.13.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.24" apply false
}

tasks.register("assembleSuite") {
    group = "build"
    description = "Assembles both Mazdash launcher and Open Headunit receiver"
    dependsOn(":app:assembleLegacyDebug")
    dependsOn(gradle.includedBuild("open-headunit").task(":app:assembleGithubDebug"))
}
