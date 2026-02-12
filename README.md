## Overview

This is a test Android project that shows nearby jobs on Neshan map and filter to find the right
one. It uses Jetpack Compose for UI and Neshan Map SDK for map features. The app allows users to
search for jobs within a specified radius, visualize them on the map, and filter results by
category. Users can tap on a job listing or map marker to view more details about the job.

## Features

- Search for jobs within a radius
- Draw a radius circle on the map
- Show results as a list and filter by category (horizontal scrolling)
- Tap a list item or a pin/label to focus the job and show details

## Tech stack

- Kotlin
- Clean Architecture + MVVM (project structure)
- Koin (dependency injection)
- Jetpack Compose (UI)
- AndroidX Lifecycle ViewModel (state management)
- Neshan Map SDK (map + markers)

## How to run

### Requirements

- Android Studio
- Android SDK installed
- Java Development Kit (JDK) installed
- An emulator or physical Android device for testing

### Steps

1. Open the project in Android Studio
2. Sync Gradle ( Most of the time it take about 8 to 10 minutes to download all dependencies and
   build the project for the first time)
3. Run the `composeApp` configuration on an emulator or device
