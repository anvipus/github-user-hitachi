plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.jetbrains.kotlin.kapt)
}

android {
    namespace = "com.anvipus.core"
    compileSdk = 34

    defaultConfig {
        minSdk = 21

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
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    //android sdk
    api(libs.androidx.core.ktx)
    api(libs.androidx.lifecycle.runtime.ktx)
    api(libs.androidx.lifecycle.viewmodel.ktx)
    api(libs.androidx.lifecycle.livedata.ktx)
    api(libs.androidx.lifecycle.common.java8)
    kapt(libs.androidx.lifecycle.common.java8)
    api(libs.androidx.appcompat)
    api(libs.material)
    api(libs.navigation.ui.ktx)
    api(libs.navigation.runtime.ktx)
    api(libs.navigation.fragment.ktx)
    api(libs.recycler.view)
    api(libs.viewpager)

    // firebase
    api(platform(libs.firebase.bom))
    api(libs.firebase.crashlytics.ktx)
    api(libs.firebase.analytics.ktx)
    api(libs.firebase.messaging.ktx)
    api(libs.firebase.perf.ktx)

    //compose
    api(libs.androidx.activity.compose)
    api(platform(libs.androidx.compose.bom))
    api(libs.androidx.material3)
    api(libs.androidx.ui.tooling.preview)
    api(libs.androidx.ui)
    api(libs.androidx.ui.graphics)
    api(libs.androidx.constraint)

    //retrofit
    api(libs.retrofit)
    api(libs.retrofit.converter.moshi)

    //moshi
    api(libs.moshi)
    api(libs.moshi.codegen)

    // okhttp
    api(platform(libs.okhttp))
    api(libs.okhttp.module)
    api(libs.okhttp.logging.interceptor)

    //glide
    api(libs.glide)
    api(libs.glide.okhttp3)

    //gson
    api(libs.gson)

    //dagger
    api(libs.dagger)
    api(libs.dagger.android)
    api(libs.dagger.android.support)

    //room
    api(libs.room)

    //biometric
    api(libs.biometric)

    //lottie
    api(libs.lottie)

    //shimmer
    api(libs.shimmer)

    //camerax
    api(libs.camera.core)
    api(libs.camera.camera2)
    api(libs.camera.lifecycle)
    api(libs.camera.view)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}