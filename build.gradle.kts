buildscript {
    repositories {
        google()
    }
    dependencies {
        classpath(libs.navigation.safeargs)
    }

}


plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.serialization) apply false
    alias(libs.plugins.dagger.hilt) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.androidx.navigation.safe.args) apply false
}