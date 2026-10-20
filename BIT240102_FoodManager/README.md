# BIT240102_FoodManager

Native Android food-management application built with Kotlin, Jetpack Compose,
Room, Retrofit, and WorkManager.

## Requirements

- Android Studio with Android SDK 36 or newer and Android Build Tools 35 or newer
- JDK 17
- An Android emulator or Android 8.0+ device

The Room database is named `BIT240102_FoodManager.db`. All Room table names,
columns, persisted preference keys, and MockAPI-owned application fields use the
`BIT240102_` prefix. The server-generated MockAPI `id` is kept as remote
metadata and is not used as a local database column.

## Run

Open this project directory in Android Studio, allow Gradle sync to finish, and
run the `app` configuration on an emulator. Alternatively, from PowerShell:

```powershell
.\gradlew.bat :app:installDebug
```

The app seeds three food categories, five dishes, and sample image URLs the
first time its local database is created. Browsing, adding, editing, deleting,
filtering, and selecting thumbnails work without an Internet connection.

## Configure MockAPI

Create a MockAPI project with the resource endpoints `categories` and `dishes`.
Dish records include the nested, prefixed `BIT240102_Images` array. Both
resources must keep the prefixed `BIT240102_LocalId` property so an interrupted
offline create can be looked up before retrying; this prevents creating a
duplicate server record. The app saves the server's generated `id` locally and
uses it for later updates and deletes. App-managed fields, including
`BIT240102_RemoteId`, use the required prefix; the Retrofit response parser also
accepts MockAPI's server-generated `id` field.

Add the project's HTTPS base URL to the root `local.properties` file (this
file is ignored by Git):

```properties
BIT240102_MOCK_API_BASE_URL=https://YOUR_PROJECT.mockapi.io/api/v1/
```

The placeholder URL in the Gradle configuration deliberately disables network
requests until a real project URL is supplied. WorkManager waits for a network
connection, syncs categories before dishes, and retries transient failures.
Pending sync operations are stored in Room alongside the application data.
After adding the URL, synchronize Gradle again and reinstall the application so
the new endpoint is included in `BuildConfig`.

## Submit this project to GitHub in small steps

The repository intentionally ignores Gradle caches, Android build output, APKs,
local SDK paths, signing credentials, and local SQLite databases. Keep
`gradle/wrapper/gradle-wrapper.jar` in Git; it is required to build the project.

From PowerShell, in the project folder, start a local Git repository:

```powershell
git init -b main
git status --short
```

Submit and review the project in separate, manageable commits instead of
uploading generated files:

```powershell
git add .gitignore .gitattributes README.md
git commit -m "Add project documentation and GitHub configuration"

git add settings.gradle.kts build.gradle.kts gradle.properties gradle
git commit -m "Add Gradle wrapper and Android project configuration"

git add app/build.gradle.kts app/src/main/AndroidManifest.xml app/src/main/res
git commit -m "Add Android app module and bilingual resources"

git add app/src/main/java
git commit -m "Add food manager, Room database, and background sync"

git status --short
```

The final `git status --short` should be empty before pushing. Create an empty
GitHub repository, copy its HTTPS URL, and connect it with:

```powershell
git remote add origin https://github.com/USERNAME/REPOSITORY.git
git push -u origin main
```

If Git asks for your author name or email, set those values only on your own
computer with `git config --global user.name "Your Name"` and
`git config --global user.email "YOUR_EMAIL"`.

## Structure

- `app/src/main/java/com/example/bit240102_foodmanager/data`: Room entities,
  prefixed columns, database, DAOs, and filters.
- `app/src/main/java/com/example/bit240102_foodmanager/services`: repository,
  Retrofit API, and network-constrained background sync.
- `app/src/main/java/com/example/bit240102_foodmanager/ui`: Compose screens and
  MVVM state holder.
- `app/src/main/res/values/strings.xml` and `values-vi/strings.xml`: English
  and Vietnamese interface strings.
