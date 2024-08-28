import com.android.build.gradle.internal.tasks.factory.dependsOn

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.hilt)
    kotlin("kapt")
}

val ktlint by configurations.creating
project.tasks.preBuild.dependsOn("ktlintCheck")

android {
    namespace = "ziedsportdb.test.fdjtest"
    compileSdk = 34

    defaultConfig {
        applicationId = "ziedsportdb.test.fdjtest"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    android.buildFeatures.buildConfig = true
    val dimension = "dimension"
    flavorDimensions.add(dimension)
    productFlavors {
        create("dev") {
            this.dimension = dimension
            buildConfigField("String", "BASE_URL", properties["baseUrl"] as String)
            buildConfigField("String", "APIKEY", properties["devApiKey"] as String)
        }
        create("prod") {
            this.dimension = dimension
            buildConfigField("String", "BASE_URL", properties["baseUrl"] as String)
            buildConfigField("String", "APIKEY", properties["prodApiKey"] as String)
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
        }
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

kapt {
    correctErrorTypes = true
}

val appCompatVersion by extra("1.7.0")
val coreKtxVersion by extra("1.13.1")
val lifeCycleAndLiveDataCompilerAndViewModelKTXVersion by extra("2.8.4")
val swipeRefreshLayoutVersion by extra("1.1.0")
val activityVersion by extra("1.9.1")
val fragmentVersion by extra("1.8.2")
val retrofitVersion by extra("2.11.0")
val okHttpVersion by extra("4.12.0")
val roomVersion by extra("2.6.1")
val daggerVersion by extra("2.15")
val coroutineVersion by extra("1.8.1")
val multidexVersion by extra("2.0.1")
val materialDesignVersion by extra("1.12.0")
val coilVersion by extra("2.7.0")
val hiltVersion by extra("2.52")
val hiltCompilerVersion by extra("1.2.0")
val composeVersion by extra("1.6.8")
val composeFoundationVersion by extra("1.6.8")
val composeMaterialVersion by extra("1.6.8")
val composeMaterial3Version by extra("1.2.1")
val composeNavigationVersion by extra("2.7.7")
val composeHiltNavigationVersion by extra("1.2.0")

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifeCycleAndLiveDataCompilerAndViewModelKTXVersion")
    implementation("androidx.activity:activity-compose:$activityVersion")
    implementation("androidx.navigation:navigation-compose:$composeNavigationVersion")
    implementation("androidx.hilt:hilt-navigation-compose:$composeHiltNavigationVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifeCycleAndLiveDataCompilerAndViewModelKTXVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.1")

    // Ktlint
    ktlint(libs.ktlint) {
        attributes {
            attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
        }
    }

    // Hilt
    implementation(libs.hilt)
    kapt(libs.hilt.compiler)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter)

    // OkHttp
    implementation(libs.okhttp)
    implementation(libs.okhttp.interceptor)

    // security
    // implementation("androidx.security:security-crypto:1.1.0-alpha05")

    // room
    implementation(libs.room)
    annotationProcessor(libs.room.compiler)
    kapt(libs.room.compiler)

    // Timber
    implementation(libs.timber)

    // room tests
    // testImplementation("androidx.room:room-testing:$room_version")

    // sl4j : to avoid warning in tests logs
    // testImplementation("org.slf4j:slf4j-simple:2.0.5")

    // Mockk
    // testImplementation("io.mockk:mockk:1.13.4")

    // coroutines mocks
    // testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")

    // test rules
    // testImplementation("androidx.arch.core:core-testing:2.2.0")

    // testImplementation("junit:junit:4.13.2")
    // androidTestImplementation("androidx.test.ext:junit:1.1.5")
    // androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}

val ktlintCheck by tasks.registering(JavaExec::class) {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    description = "Check Kotlin code style"
    classpath = ktlint
    mainClass.set("com.pinterest.ktlint.Main")
    args(
        "**/src/**/*.kt",
        "**.kts",
        "!**/build/**",
    )
}

tasks.check {
    dependsOn(ktlintCheck)
}

tasks.register<JavaExec>("ktlintFormat") {
    group = LifecycleBasePlugin.VERIFICATION_GROUP
    description = "Check Kotlin code style and format"
    classpath = ktlint
    mainClass.set("com.pinterest.ktlint.Main")
    jvmArgs("--add-opens=java.base/java.lang=ALL-UNNAMED")
    // see https://pinterest.github.io/ktlint/install/cli/#command-line-usage for more information
    args(
        "-F",
        "**/src/**/*.kt",
        "**.kts",
        "!**/build/**",
    )
}
