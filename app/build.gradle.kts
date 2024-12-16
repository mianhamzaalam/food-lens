plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    kotlin("kapt")
}

android {
    namespace = "com.driven.foodlens"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.driven.foodlens"
        minSdk = 24
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
    }

    buildFeatures{
        viewBinding =true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.auth.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation("com.airbnb.android:lottie:6.6.0")
    implementation("com.intuit.sdp:sdp-android:1.1.1")
    implementation("com.intuit.ssp:ssp-android:1.1.1")

    // Retrofit:
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    // For navigation between fragments:
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.4")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.4")

    // bottom navigation:
    implementation("com.github.ibrahimsn98:SmoothBottomBar:1.7.9")

    // material design
    implementation("com.google.android.material:material:1.12.0")

    // circular profile image:
    implementation("de.hdodenhof:circleimageview:3.1.0")

    // Pagination:
    implementation("androidx.paging:paging-runtime:3.3.4")

    // Picasso for image loading
    implementation("com.squareup.picasso:picasso:2.71828")

    // Google Services:
    implementation("com.google.android.gms:play-services-auth:21.2.0")

    // Room Dependencies:
    val room_version = "2.6.1"

    implementation("androidx.room:room-runtime:$room_version")
    kapt("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")

    // LifeCycle of Room Dependecy:
    val lifecycle_version = "2.8.7"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
    kapt("androidx.lifecycle:lifecycle-compiler:$lifecycle_version")

    // Coroutines:
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")

    // Nlp:
    implementation("com.google.mlkit:language-id:17.0.6")

    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.0.20")

    implementation("com.google.firebase:firebase-auth:21.3.0") // Adjust according to your needs
    implementation("com.google.firebase:firebase-core:21.0.0")

}