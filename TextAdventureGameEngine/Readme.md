<!-- TOP WAVE -->

<img width="100%" src="https://capsule-render.vercel.app/api?type=waving&color=0:0f2027,50:203a43,100:2c5364&height=200&section=header&text=&fontSize=0"/>

<h1 align="center">🎮 Text Adventure Game Engine</h1>

<p align="center">
  <b>Build rich, interactive text-based worlds with ease</b><br>
  <sub>Modular • JSON-driven • Extensible</sub>
</p>



<p align="center">
  <img src="https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java">
  <img src="https://img.shields.io/badge/Maven-Build-blue?style=for-the-badge&logo=apachemaven">
  <img src="https://img.shields.io/badge/JSON-Driven-green?style=for-the-badge&logo=json">
  <img src="https://img.shields.io/badge/Type-CLI-purple?style=for-the-badge">
</p>

---

## 📌 Table of Contents

* Overview
* Highlights
* Features
* Demo
* Tech Stack
* Project Structure
* Commands
* Core Concepts
* How to Run
* Architecture
* Future Enhancements
* Author

---

## ✨ Overview

A **lightweight, fully customizable text adventure engine** built from scratch in Java.

Designed with a **data-driven approach**, the entire game world — rooms, items, puzzles — is controlled via JSON.

> 🧠 Build your own adventure without touching core logic.

---

## 🌟 Highlights

* ⚡ Zero code required to design adventures
* 🧠 Flexible condition system
* 🔌 Easily extendable architecture
* 💾 Built-in persistence system

---

## 🚀 Features

* 🧭 Room Navigation (`go`, `look`)
* 🎒 Inventory System
* 🧩 Puzzle Mechanics (Conditions-based)
* 🔐 Conditional Exits (Locked paths)
* 🌗 Dynamic Room Descriptions
* ⚙️ Advanced Item Usage System
* 💾 Save / Load Game State
* 📦 JSON-driven Game Design
* 🧱 Extensible Architecture

---

## 🖥️ Demo

```bash
> look
You are in a dark cellar. A broken staircase leads up.

> take sturdy plank
You take the sturdy plank.

> go north
You move north.

> use sturdy plank on broken staircase
You place the plank across the gap.

> go up
You move up.
```

<p align="center">
  <img src="demo.gif" width="600"/>
</p>

---

## 🛠️ Tech Stack

| Technology  | Purpose                       |
| ----------- | ----------------------------- |
| ☕ Java 17   | Core engine                   |
| 📦 Maven    | Build & dependency management |
| 🔄 Gson     | JSON parsing                  |
| 🧠 OOP      | Architecture                  |
| 📂 File I/O | Persistence                   |

---

## 📂 Project Structure

```bash
TextAdventureGameEngine/
├── src/
│   ├── main/
│   │   ├── java/com/textadventure/
│   │   │   ├── engine/
│   │   │   ├── game/
│   │   │   ├── model/
│   │   │   ├── utils/
│   │   │   └── Main.java
│   │   └── resources/
│   │       └── adventure.json
├── pom.xml
└── README.md
```

---

## 🎮 Commands

```bash
go <direction>      # Move between rooms
look                # Re-display current room
take <item>         # Pick up item
inventory / inv     # Show inventory
use <item> on <x>   # Use item
save                # Save game
load                # Load game
quit                # Exit game
```

---

## 🧠 Core Concepts

### 🔐 Conditional Exits

```json
"up": {
  "targetRoom": "Upstairs Landing",
  "conditions": {
    "requiresItem": "sturdy plank",
    "failMessage": "You can't climb up without fixing the staircase."
  }
}
```

### 🌗 Conditional Descriptions

```json
"conditionalDescriptions": [
  {
    "conditions": {
      "requiresItem": "torch"
    },
    "description": "With light, you can now see everything clearly."
  }
]
```

### ⚙️ Item Usage Logic

```json
"usability": {
  "target": "lever slot",
  "effectDescription": "You hear a mechanism unlock.",
  "modifiesExit": {
    "direction": "north",
    "clearRequiresItem": true
  }
}
```

---

## ▶️ How to Run

### ✅ Prerequisites

* Java 17+
* Maven

### ▶️ Run via Maven

```bash
mvn clean install
mvn exec:java -Dexec.mainClass="com.textadventure.Main"
```

### ▶️ Run in IDE

* Open project
* Run `Main.java`
* Start playing 🎮

---

## 🏗️ Architecture

* **Engine Layer** → JSON parsing & world building
* **Game Layer** → Command handling & game loop
* **Model Layer** → Core entities
* **Utils Layer** → Save/load system

---

## 🔥 Future Enhancements

* 🧠 NPC interactions
* 🗺️ Map system
* 🎵 Sound effects
* 🧪 Unit testing
* 🌐 GUI version (Swing/JavaFX)

---

## 👨‍💻 Author

**Sushobhit Chattaraj**

> “A game engine is not just code — it’s a system for creating worlds.”



---

<!-- BOTTOM WAVE -->

<img width="100%" src="https://capsule-render.vercel.app/api?type=waving&color=0:2c5364,50:203a43,100:0f2027&height=120&section=footer"/>
