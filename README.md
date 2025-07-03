## Rate My Plate — Android App

**Rate My Plate** is a fun and simple Android app that lets users submit photos of their meals, write a short description, and get a random “health” rating and suggestion. 
It stores meals locally on the device and displays them in a scrollable list.
This app was built as part of a learning project to practice Android development, working with camera input, RecyclerView, local storage (SharedPreferences), and layouts.

---

## How to Run the App

1. Open the project in **Android Studio**.
2. Connect your Android device (real device recommended — camera works best this way).
3. Hit **Run** (or press `Shift + F10`).
4. Make sure to **grant camera and storage permissions** when asked.

---

## Tools and Libraries Used

- Java (Android)
- RecyclerView for the meal gallery
- SharedPreferences to store meal data locally
- FileProvider for handling camera photo saving
- ConstraintLayout + ScrollView for responsive UI

---

## What’s Working

- Take a meal photo using the **camera** or choose from **gallery**
- Add a **description** (with a character counter)
- Choose a **meal type** from a dropdown (spinner)
- Get a random **rating**, **calories**, and **suggestion**
- Meals are saved **locally**, even after closing the app
- Scroll through submitted meals in a **clean list**
- Tap a meal to open a **detail view**

---

Extra Features Added
.Categorize meals



