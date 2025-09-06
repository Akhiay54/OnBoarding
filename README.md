# OnBoarding - Instant Saving App

Android onboarding application built with Jetpack Compose featuring smooth card animations and clean architecture.

## Features

- **Sequential Card Animations**: Cards appear from bottom with expand/collapse transitions
- **Tilt Effects**: Alternating left/right tilt animations between cards
- **Interactive Cards**: Tap to expand/collapse after animation sequence
- **Dynamic Background**: Background color changes based on selected card
- **API Integration**: Fetches data from JAR's education metadata endpoint

## Tech Stack

- **Jetpack Compose** - Modern UI toolkit
- **Clean Code** - Well-structured architecture
- **Retrofit** - API client
- **MVVM + Clean Architecture** - Proper separation of concerns
- **Coroutines** - Asynchronous operations

## Getting Started

### Prerequisites
- Android Studio Hedgehog or later
- Android SDK 29+
- Internet connection

### Installation

1. Clone the repository
2. Open in Android Studio
3. Build and run:
   ```bash
   ./gradlew installDebug
   ```

## Architecture

- **Presentation Layer**: Compose UI, ViewModel
- **Domain Layer**: Use cases for data and animations
- **Data Layer**: Repository, API service
- **Manual DI**: Simple dependency injection without frameworks

## API

Uses JAR's education metadata endpoint:
```
https://myjar.app/_assets/shared/education-metadata.json
```

##Requirements ✅

- ✅ Jetpack Compose
- ✅ MVVM Architecture  
- ✅ Clean Architecture
- ✅ Coroutines
- ✅ Retrofit
- ✅ Modern Android practices
- ✅ Complex animations
- ✅ Professional code quality

---