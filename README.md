# Firebase Feedback Library

A modular Android library that provides a reusable feedback flow for beta applications using Firebase App Distribution, while keeping the consuming application decoupled from the concrete Firebase implementation.

---

## Why This Exists

Beta applications often need a simple way for testers to report feedback.

Instead of integrating Firebase App Distribution feedback functionality directly into every application, this library separates the public feedback contract from the concrete Firebase implementation.

This allows the consuming application to depend on an abstraction while keeping the Firebase-specific implementation isolated.

---

## Architecture

The library is divided into two modules:

```text
feedback-api
      │
      │ Public contract
      ▼
feedback-impl
      │
      │ Firebase-specific implementation
      ▼
Firebase App Distribution
```

The separation keeps Firebase-specific functionality out of the public API module.

---

## `feedback-api`

The API module contains the public contract and reusable UI components.

The public contract is represented by:

```kotlin
interface FeedbackSender {
    fun startFeedback(messageId: Int)
}
```

The API module also contains:

- Feedback UI state
- Feedback events
- UI effects
- Compose feedback UI components

The public contract does not directly depend on Firebase App Distribution.

---

## `feedback-impl`

The implementation module provides the concrete Firebase integration.

It contains:

- `FirebaseFeedbackSender`
- Feedback ViewModel
- Firebase App Distribution integration
- Email fallback handling
- Diagnostic information collection

The Firebase implementation invokes Firebase App Distribution through the `FeedbackSender` abstraction.

---

## Feedback Flow

The overall flow is:

```text
Consumer Application
        ↓
feedback-api
        ↓
FeedbackSender
        ↓
feedback-impl
        ↓
Firebase App Distribution
```

If the Firebase feedback flow cannot be launched, the implementation can fall back to an email-based feedback flow.

---

## Email Fallback

The email fallback can include diagnostic context such as:

- Application package
- Application version
- Version code
- Android version
- Android SDK
- Device manufacturer
- Device model
- Locale
- Timestamp
- Steps to reproduce
- Expected behavior
- Actual behavior

This helps testers provide useful environment information without manually collecting every detail.

---

## Consumer Dependency Model

The intended dependency model is:

```text
Main Application
       │
       └── feedback-api

Beta Implementation
       │
       └── feedback-impl
```

The API module provides the stable contract while the implementation module contains the Firebase-specific behavior.

The implementation can be included only where beta feedback functionality is required.

---

## Modular Design

The separation between API and implementation demonstrates:

- Dependency inversion
- Interface-based design
- Modular Android library development
- Separation of public contract from infrastructure
- Ability to replace or evolve the concrete implementation without changing consumers of the public API

---

## Publishing

The library is configured for Maven publishing under the namespace:

```text
io.github.karthik-pro-engr
```

The API and implementation modules are published as separate artifacts.

The repository also contains GitHub Actions workflows for validation and publishing.

---

## CI/CD

The CI workflow validates:

- Android build
- Unit tests
- Android lint
- Build artifacts

The publishing workflow supports tagged releases and publishing to Maven Central.

GitHub Packages publishing can also be enabled when the required credentials are configured.

---

## Project Structure

```text
firebase-feedback-lib/

├── feedback-api/
│   ├── src/main/
│   │   └── FeedbackSender.kt
│   └── ...
│
├── feedback-impl/
│   ├── src/main/
│   │   ├── FirebaseFeedbackSender.kt
│   │   ├── FeedbackViewModel.kt
│   │   └── ...
│   └── ...
│
├── .github/
│   └── workflows/
│       ├── android-ci.yml
│       └── publish.yml
│
├── gradle/
│   └── libs.versions.toml
│
└── settings.gradle.kts
```

---

## Tech Stack

- Kotlin
- Android SDK
- Jetpack Compose
- AndroidX ViewModel
- Kotlin Coroutines
- Kotlin Flow
- Firebase App Distribution
- Gradle Kotlin DSL
- Maven Publishing
- Dokka
- GitHub Actions

---

## Engineering Goals

This library demonstrates practical Android engineering around:

- Separation of API and implementation
- Dependency inversion
- Modular library design
- Reusable Compose UI
- Graceful fallback behavior
- Maven publishing
- Automated CI validation
- Release automation

---

## Status

Active reusable Android library project.
