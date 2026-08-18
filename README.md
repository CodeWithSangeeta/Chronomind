# ⏱️ ChronoMind

**Focus Better. Build Consistency. Achieve More.**

ChronoMind is a modern Android productivity app built to help users stay focused, build better habits, and track daily progress with clarity. Whether you're studying, coding, reading, exercising, learning a language, or working on personal goals, ChronoMind gives you a clean and flexible system to manage your focus sessions without unnecessary complexity.

---

## ✨ Overview

Staying productive is not about working longer — it is about showing up consistently.

ChronoMind combines a **focus timer**, **stopwatch-based activity tracking**, **habit streaks**, **session history**, **productivity insights**, and **daily reminders** into one clean Android app. It is designed for people who want a distraction-free experience with useful customization and strong everyday usability.

---

## 📸 Screenshots

<p align="center">
  <img src="https://github.com/user-attachments/assets/0c0fd044-22c7-4e1c-8bc9-3c83eaf4ab83" alt="ChronoMind Screenshot 1" width="220" />
  <img src="https://github.com/user-attachments/assets/93073735-f0fd-419e-bfeb-3a7ef0b77230" alt="ChronoMind Screenshot 2" width="220" />
  <img src="https://github.com/user-attachments/assets/cb765da4-f259-4456-aff2-4fd726bbd78f" alt="ChronoMind Screenshot 3" width="220" />
</p>

<p align="center">
  <img src="https://github.com/user-attachments/assets/759729fb-3f96-46b7-a93c-d6f8167d1548" alt="ChronoMind Screenshot 4" width="220" />
  <img src="https://github.com/user-attachments/assets/4ee61777-bca6-4c66-a3c6-857bce5b1443" alt="ChronoMind Screenshot 5" width="220" />
  <img src="https://github.com/user-attachments/assets/9a571b87-f3a3-4a0f-a68c-22755766fd1d" alt="ChronoMind Screenshot 6" width="220" />
</p>

<p align="center">
  <img src="https://github.com/user-attachments/assets/7b1f7985-75c0-45ee-8d49-6f39a44ed44a" alt="ChronoMind Screenshot 7" width="260" />
</p>
```

---

## 🚀 Features

### 🎯 Create Activities That Match Your Goals
- Create personalized activities with custom names, icons, and accent colors
- Choose the tracking style that fits your routine
- Set up activities for study, coding, reading, exercise, meditation, language learning, and more

### ⏳ Dual Focus Modes
- **Timer Mode** — set a target duration and stay focused until the countdown ends
- **Stopwatch Mode** — track progress without a fixed time limit

### ✅ Flexible Completion Styles
- **Manual Completion** — mark activities complete whenever you decide you're done
- **Automatic Completion** — automatically mark timer-based activities complete when the countdown finishes

### 🔥 Custom Streak Rules
- **Continue Streak** — keep the streak even if a day is missed
- **Reset to Zero** — restart the streak from zero after a missed day

### 🌅 Daily Reset Workflow
- Activities refresh for a new day automatically
- Incomplete sessions are moved to history
- Your workspace stays clean while your progress remains preserved

### 📊 Progress Tracking
- Review completed and incomplete sessions
- Explore productivity insights and consistency trends
- Understand how your time is being used over time

### 🔔 Smart Daily Reminders
- Set a custom daily reminder time
- Reminder scheduling continues even after device restarts
- Notifications help users stay accountable without being intrusive

### 📱 Foreground Session Tracking
- Running sessions display a live foreground notification
- Users can stay aware of active focus sessions even outside the app

### 📦 Offline First
- Works fully offline
- Fast, native performance with local storage
- No account required

---

## 🧩 Core Highlights

- Focus Timer and Stopwatch support
- Activity-based habit tracking
- Custom daily streak behavior
- Manual and auto completion styles
- Session history and productivity insights
- Daily reminders with reboot support
- Offline-first architecture
- Clean modern UI built with Jetpack Compose

---

## 🛠️ Tech Stack

| Category | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose, Material 3 |
| Architecture | MVVM, Repository Pattern |
| Dependency Injection | Hilt |
| Database | Room |
| Preferences | DataStore |
| Background Tasks | WorkManager |
| Notifications | Foreground Service, Notification Manager |
| Navigation | Navigation Compose |
| Async | Kotlin Coroutines, Flow |
| Build System | Gradle Kotlin DSL |

---

## 🏗️ Architecture

ChronoMind follows a modern Android architecture built with **MVVM** and a **Repository** layer.

### Structure
- **UI Layer** — Jetpack Compose screens and components
- **ViewModel Layer** — state handling and business flow coordination
- **Repository Layer** — activity logic, settings, onboarding, reminders, and session management
- **Data Layer** — Room database + DataStore preferences

This structure keeps the app scalable, maintainable, and easier to extend.

---

## 📱 Main Screens

- **Home** — focus dashboard, active session view, quick actions, recent activities
- **All Activities** — manage and track all created activities
- **Create / Edit Activity** — customize name, icon, color, mode, and rules
- **History** — review session history with status and dates
- **Insights** — track productivity and consistency metrics
- **Settings** — reminders, notifications, default preferences, support, and app options

---

## 🔐 Permissions Used

ChronoMind uses only the permissions needed for core app functionality:

- `POST_NOTIFICATIONS` — for reminders and active session notifications
- `RECEIVE_BOOT_COMPLETED` — to restore reminders after device restart
- `FOREGROUND_SERVICE` — to keep active focus sessions visible while running
- `FOREGROUND_SERVICE_DATA_SYNC` — for user-noticeable active session continuity

---

## ⚙️ Getting Started

### Prerequisites
- Android Studio
- JDK 11 or above
- Android SDK

### Clone the repository

```bash
git clone https://github.com/your-username/ChronoMind.git
cd ChronoMind
```

### Run the app
1. Open the project in Android Studio
2. Sync Gradle
3. Run on an emulator or physical Android device

---

## 📦 Build Info

- **Application ID:** `com.sangeeta.chronomind`
- **Min SDK:** 24
- **Target SDK:** 36
- **Version:** 1.0.0

---

## 🎯 Ideal For

ChronoMind is built for:

- Students
- Developers
- Writers
- Readers
- Freelancers
- Professionals
- Competitive exam aspirants
- Anyone building stronger daily habits

---

## 🌱 Why ChronoMind?

Many productivity apps feel bloated, distracting, or overly complicated.

ChronoMind is designed around a simpler idea:  
**help users stay consistent with a clean, calm, and flexible daily focus system.**

It gives users enough customization to make the app feel personal, while keeping the experience minimal and purposeful.

---

## 🔮 Roadmap

Planned improvements may include:

- Drag-and-drop activity reordering
- Backup and restore support
- Exportable progress reports
- More advanced analytics
- Improved tablet optimization
- More customizable reminder behavior

---

## 🤝 Contributing

Contributions, suggestions, and feedback are welcome.

If you'd like to improve ChronoMind:
1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Open a pull request

---

## 📄 License

Add your license here, for example:

**MIT License**

or

**Apache License 2.0**

---

## ⭐ Support

If you like this project, consider giving it a **star** on GitHub — it helps a lot and supports future improvements.

---

**Small actions repeated every day lead to big results.**  
**ChronoMind helps turn focus into consistency — and consistency into progress.**
