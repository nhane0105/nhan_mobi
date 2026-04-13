plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.baitap_1"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.baitap_1"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout) {
        exclude(group = "androidx.navigationevent", module = "navigationevent-android")
    }
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    
    // Exclude navigationevent from all dependencies
    configurations.all {
        exclude(group = "androidx.navigationevent", module = "navigationevent-android")
    }
}