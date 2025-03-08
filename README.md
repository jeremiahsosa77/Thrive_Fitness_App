# Thrive Fitness/Wellness App

**Thrive** is a student made fitness mobile application built using **Android Studio**, **Java**, and **Firebase** to help users log their workouts, track meals, monitor progress, and receive personalized recommendations. The app provides workout logging, meal tracking, an AI powered chatbot and much more.

---

## Features

- **User Authentication**: Email/password and Google Sign-In via Firebase Authentication.
- **Workout Logging**: Log exercises, sets, reps, weights, and photos for verification.
- **Meal Tracking**: Track daily caloric intake and macronutrients.
- **Progress Tracking**: Monitor weight changes, workout progress, and meal logs on a calendar-based UI.
- **Personalized Recommendations**: AI-powered workout and meal suggestions using the **Surprise Library**.
- **Coach Rocky (AI Chatbot)**: Real-time responses from an AI-powered chatbot to motivate and guide users.
- **Virtual Pet System**: Customizable pet that responds to user progress and provides motivational feedback.

---

## Tech Stack

- **Frontend**:  
  - Android Studio  
  - Java (Programming Language)  
  - Firebase (Authentication, Firestore, Cloud Messaging)  
  - Room (Local Database)

- **Backend**:  
  - Firebase Firestore (Cloud Database)  
  - Firebase Cloud Messaging (Push Notifications)  
  - AI Chatbot Integration (Llama 3 or Surprise Library)

- **Testing**:  
  - JUnit (Unit Testing)  
  - Espresso (UI Testing)

---

## Getting Started

# Thrive Fitness App - Installation Guide

This guide will walk you through the process of setting up and running the Thrive Fitness App on your local machine using Android Studio's emulator. Follow these steps carefully to get the application up and running.

## Prerequisites

Before you begin, make sure you have the following installed on your computer:

- **Java Development Kit (JDK) 11** or higher
- **Android Studio** (latest version recommended)
- **Git** for cloning the repository

## Step 1: Clone the Repository

1. Open a terminal or command prompt
2. Navigate to the directory where you want to store the project
3. Run the following command:

git clone https://github.com/jeremiahsosa77/Thrive_Fitness_App.git

## Step 2: Open the Project in Android Studio

1. Launch Android Studio
2. Select "Open an Existing Project"
3. Navigate to the directory where you cloned the repository
4. Select the root folder of the project (Thrive_Fitness_App) and click "Open"
5. Wait for Android Studio to complete the Gradle sync process (this may take a few minutes)

## Step 3: Configure the Android Emulator

1. In Android Studio, click on "Tools" > "Device Manager"
2. Click on "Create Device"
3. Select a phone device (like Pixel 4 or 5) and click "Next"
4. Choose a system image:
- Select the "x86 Images" or "x86_64 Images" tab
- Choose an API level 26 or higher (Android 8.0 Oreo or higher)
- If no system image is downloaded yet, click the "Download" link next to your preferred version
- Click "Next" when ready
5. Give your virtual device a name (or leave the default) and click "Finish"

## Step 4: Build and Run the Application

1. After the emulator is set up, click on the "Run" menu in Android Studio
2. Select "Run 'app'" or click the green play button in the toolbar
3. Select the emulator you just created from the device selection dialog
4. Wait for the emulator to start and the app to build and install
5. The Thrive Fitness App should launch automatically in the emulator

## Step 5: Using the App

Once the app launches, you'll see the login screen. Since this is a local installation:

1. Create a new account by clicking "Create Account"
2. Fill in the required fields and click "Sign Up"
3. Log in with your newly created credentials
4. Explore the app's features:
- View Calendar
- Track Fitness Goals
- Log workout data
- 
## Additional Resources

- For more detailed Android Studio setup instructions, visit the [official Android developer site](https://developer.android.com/studio/install)
- For emulator optimization tips, check [this guide](https://developer.android.com/studio/run/emulator-acceleration)

Now you should have the Thrive Fitness App running on your Android emulator and be ready to explore or further develop the application!
