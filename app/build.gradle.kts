plugins {
    // This is the correct way to apply the plugin using the versions catalog.
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("org.jetbrains.kotlin.kapt")// ✅ use this instead of id("org.jetbrains.kotlin.kapt") with a version(room DB)
    // The Google services plugin is applied here as it's not in the versions catalog.
    id("com.google.gms.google-services")
}

android {
    namespace = "lk.nibm.hdse242ft.artauctionapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "lk.nibm.hdse242ft.artauctionapp"
        minSdk = 30
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.androidx.material.icons.extended)
    implementation(libs.coil.compose)
    implementation(libs.google.play.services.location)
    implementation(libs.accompanist.permissions)

//    API DATA BASE
    implementation(libs.retrofit2)
    implementation(libs.retrofit2.gson)

    // Room (Local DB)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)

    // WorkManager for background syncing
    implementation(libs.androidx.work.runtime.ktx)

    // Kotlin Coroutines (for suspend/IO)
    implementation(libs.kotlinx.coroutines.android)



}
