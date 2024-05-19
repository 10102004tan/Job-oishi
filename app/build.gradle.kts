plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.joboishi"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.joboishi"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("config") {
            keyAlias = "my_alias"  // key0 is the alias name of the key
            keyPassword = "111111" // password is the password of the key
            storeFile = file("C:\\Users\\Public\\Downloads\\my_keystore.jks") // key.jks is the path of the key
            storePassword = "111111" // password is the password of the key
        }
    }



    buildTypes {

        debug {

            signingConfigs {
                getByName("config")
            }
        }

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


    buildFeatures {
        viewBinding = true
    }
    sourceSets {
        getByName("main") {
            assets {
                srcDirs("src\\main\\assets", "src\\main\\assets", "src\\main\\assets", "src\\main\\assets\\fonts")
            }
        }
    }
}

dependencies {
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.activity:activity:1.9.0")
    implementation("androidx.navigation:navigation-fragment:2.7.7")
    implementation("androidx.navigation:navigation-ui:2.7.7")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("com.google.firebase:firebase-messaging:24.0.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("com.makeramen:roundedimageview:2.3.0")
    implementation ("com.facebook.shimmer:shimmer:0.1.0@aar")
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    implementation ("com.airbnb.android:lottie:6.0.1")
    implementation("com.google.android.gms:play-services-auth:21.1.1")
    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation ("com.github.Spikeysanju:MotionToast:1.4")
    implementation ("com.github.angads25:toggle:1.1.0")
    implementation ("com.github.gabriel-TheCode:AestheticDialogs:1.3.8")
    implementation ("com.facebook.android:facebook-login:latest.release")
    implementation ("com.facebook.android:facebook-android-sdk:17.0.0")
    implementation("com.google.firebase:firebase-database:21.0.0")
    implementation("com.google.firebase:firebase-auth:23.0.0")
    implementation("com.facebook.android:facebook-android-sdk:latest.release")
    implementation ("com.google.firebase:firebase-auth:23.0.0")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.github.vedraj360:DesignerToast:0.1.3")
    implementation ("com.google.android.flexbox:flexbox:3.0.0")
    implementation ("com.github.CuteLibs:CuteDialog:2.1")

}
