import com.android.build.api.dsl.Packaging
import com.android.utils.TraceUtils.simpleId

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlinx-serialization")
    id("kotlin-kapt")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("dagger.hilt.android.plugin")
    alias(libs.plugins.baselineprofile)
}
kapt {
    correctErrorTypes = true
}
android {
    packagingOptions {
        resources.excludes.add("META-INF/*")
    }
    namespace = "com.example.apiretrofitktor"
    compileSdk = 34
    defaultConfig {
        applicationId = "com.example.apiretrofitktor"
        minSdk = 29
        targetSdk = 34
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
        create("benchmark") {
            initWith(buildTypes.getByName("release"))
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks += listOf("release")
            isDebuggable = false
        }

    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}
hilt {
    enableAggregatingTask = true
}
dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.gson)
    implementation(libs.logging.interceptor)
    implementation(libs.androidx.viewmodel)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.retrofit)
    implementation(libs.glide)
    implementation(libs.androidx.room.common)
    implementation(libs.core.ktx)
    implementation(libs.androidx.profileinstaller)
    testImplementation(libs.junit.jupiter)
    androidTestImplementation(libs.junit.jupiter)
    annotationProcessor(libs.glide.annotations)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    // ktor dependencies
    implementation(libs.ktor.cio)
    implementation(libs.ktor.serialization)
    implementation(libs.ktor)
    implementation(libs.slf4j)
    implementation(libs.ktor.logging)
    implementation(libs.ktor.okHttp)
    implementation(libs.ktor.contentNegotiation)
    // room
    implementation(libs.room.runtime)
    "baselineProfile"(project(":baselineprofile"))
    kapt(libs.sqlite.jdbc)
    kapt(libs.androidx.room.compiler.v261)
    implementation(libs.room.ktx)
    // hilt
    kapt(libs.androidx.hilt.compiler)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    androidTestImplementation(libs.hilt.android.testing)
    kaptAndroidTest(libs.hilt.android.compiler)
    implementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.mockk)

}