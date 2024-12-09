buildscript {
    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.51.1")
    }

    fun getApiKey(keyName: String): String {
        val properties = java.util.Properties()
        val localPropertiesFile = rootProject.file("local.properties")
        if (localPropertiesFile.exists()) {
            localPropertiesFile.inputStream().use { properties.load(it) }
        }
        return properties.getProperty(keyName) ?: ""
    }

    val mapkitApiKey: String by extra { getApiKey("MAPKIT_API_KEY") }
    val openWeatherApiKey: String by extra { getApiKey("OPENWEATHER_API_KEY") }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false

    id("com.google.gms.google-services") version "4.4.2" apply false
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
    id("com.google.firebase.crashlytics") version "3.0.2" apply false
}

