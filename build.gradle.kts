buildscript {
    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.51.1")
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false

    id("com.google.gms.google-services") version "4.4.2" apply false
    id("com.google.dagger.hilt.android") version "2.51.1" apply false

//    kotlin("jvm") version "2.0.21"
//    kotlin("plugin.serialization") version "2.0.21"
}

//allprojects {
//    repositories {
//        google()
//        mavenCentral()
//    }
//}