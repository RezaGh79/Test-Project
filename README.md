This is a Kotlin Multiplatform project targeting Android.

## Features

- Map-based job discovery around the user's current location using Neshan Map SDK
- Configurable search radius with a simple dropdown selector
- Dynamic generation and visualization of nearby jobs as map markers and labels
- Category-based filtering with chips and a scrollable job list
- Clickable markers and list items that show a concise alert dialog with job details

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - `commonMain` is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    `iosMain` would be the right folder for such calls.


Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…