# 💧 HydroTrack

**HydroTrack** est une application Android native développée avec **Jetpack Compose**, conçue pour aider les utilisateurs à suivre et optimiser leur hydratation quotidienne.

## 📱 Aperçu

L'application propose une interface moderne basée sur Material 3, avec un thème sombre par défaut, permettant à l'utilisateur de suivre sa consommation d'eau au fil de la journée et de recevoir des recommandations personnalisées grâce à l'intégration de Firebase AI.

## ✨ Fonctionnalités

- Suivi de l'hydratation quotidienne
- Interface utilisateur moderne en Jetpack Compose (Material 3)
- Persistance locale des données via Room
- Intégration Firebase (AI, App Check)
- Appels réseau via Retrofit / OkHttp / Moshi
- Architecture MVVM (ViewModel + State)

## 🛠️ Stack technique

| Catégorie | Technologies |
|---|---|
| Langage | Kotlin |
| UI | Jetpack Compose, Material 3 |
| Architecture | MVVM (ViewModel, Lifecycle) |
| Base de données locale | Room |
| Réseau | Retrofit, OkHttp, Moshi |
| Cloud / IA | Firebase AI, Firebase App Check |
| Build | Gradle (Kotlin DSL), AGP 8.10.1 |
| Tests | JUnit, Espresso, Robolectric, Roborazzi |

## 📋 Prérequis

- Android Studio (Meerkat feature Drop 2024.3.2 patch1)
- JDK 11
- Un fichier `google-services.json` valide à la racine du module `app/` (requis pour Firebase)
- Un émulateur ou appareil physique en API 33 

## 🚀 Installation

1. Clone le dépôt :
```bash
   git clone https://github.com/Irsteve77/hydrotrackk.git
```
2. Ouvre le projet dans Android Studio.
3. Ajoute ton fichier `google-services.json` dans `app/`.
4. Crée un fichier `.env` à la racine (basé sur `.env.example`) si des clés d'API sont nécessaires.
5. Synchronise le projet (`Sync Project with Gradle Files`).
6. Lance l'application sur un émulateur ou un appareil (`Shift + F10`).

## 🧩 Problèmes résolus (ordre chronologique)

1. **`libs.versions.toml` incomplet** → conservé toutes les clés existantes, mis à jour uniquement agp/kotlin/coreKtx/activityCompose/composeBom, aligné KSP sur Kotlin (`2.0.21-1.0.28`).
2. **`compileSdk { ... }` (syntaxe AGP 9)** incompatible avec AGP 8.10.1 → revenu à `compileSdk = 36`.
3. **`android.useAndroidX` manquant** → ajouté `android.useAndroidX=true` dans `gradle.properties`.
4. **`debug.keystore` introuvable** → généré via `keytool -genkey -v -keystore debug.keystore -storepass android -alias androiddebugkey -keypass android -keyalg RSA -keysize 2048 -validity 10000 -dname "CN=Android Debug,O=Android,C=US"`.
5. **`ClassNotFoundException: MainActivity`** → plugin `kotlin-android` manquant (seul `kotlin-compose` était déclaré) → ajouté dans `libs.versions.toml` et les `build.gradle.kts`.
6. **JVM target incohérent (Java 11 vs Kotlin 21)** → ajouté :
```kotlin{
     compilerOptions {
       jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11)
     }
   }
```