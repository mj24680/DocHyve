// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
}

buildscript{
    val nav_version = "2.9.7"
    repositories{
        google()
    }

    dependencies{
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")
    }
}