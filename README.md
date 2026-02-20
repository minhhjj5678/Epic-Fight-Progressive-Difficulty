
# ⚔️ Epic Fight: Progressive Difficulty

![Minecraft](https://img.shields.io/badge/Minecraft-1.21.1-green?style=flat-square)
![Forge](https://img.shields.io/badge/NeoForge-21.1.x-red?style=flat-square)
![Version](https://img.shields.io/badge/Version-1.0.0-orange?style=flat-square)
![License](https://img.shields.io/badge/License-GPL--3.0-blue?style=flat-square)

**EF Progressive Difficulty** is a lightweight Forge mod tailored for the **Epic Fight Mod**. It solves the problem of combat becoming too easy in the late game by dynamically scaling mob attributes based on the world's total running time.

> *"Make late-game combat feel like a real challenge again."*

---

## ✨ Key Features

### 📈 Time-Based Scaling
Mobs grow stronger as time passes in your world. The difficulty scales based on **Server Game Time**, meaning the longer your server/world exists, the tougher the enemies become.

It buffs the following **Epic Fight Attributes**:
- **💥 Impact:** Enemies hit harder and break your guard faster.
- **⚖️ Weight:** Enemies become heavier and harder to knock back (prevents stun-locking bosses).
- **🛡️ Stun Armor:** Enemies are more resistant to being stunned by your attacks.
- **⚔️ Armor Negation:** Enemy attacks penetrate your armor more effectively.
- **🔄 Max Strikes:** Enemies can chain more attacks together.



### ⚙️ Fully Configurable
You have total control via `config/efprogressivediff-common.toml`.
- **Multipliers:** Adjust how fast difficulty increases (e.g., +1% per day).
- **Caps:** Set hard limits so mobs don't become invincible.
- **Base Value:** Adjust the starting baseline for scaling.

### 🛡️ Stable & Optimized
- **Lag-Free:** Logic only runs on server-side tick updates.

---

## 🧮 How it Works

The mod applies a bonus to mob attributes using this formula:

```math
Final Value = Original Value + Bonus
```
Where `Bonus` is calculated as:
```math
Bonus = BaseValue * (1 + Multiplier * DaysPassed)
```
(Note: "DaysPassed" is calculated from the total Server Game Time)

---

## 📥 Installation

1.  Install **Minecraft Forge** (1.20.1).
2.  Install **Epic Fight Mod** (Required dependency).
3.  Download **EF Progressive Difficulty** and drop it into your `mods` folder.
4.  Run the game and enjoy the pain!
    
----------

## 🤝 Credits

-   **Author:** Minhhjjj
-   **Dependency:** [Epic Fight Mod](https://www.curseforge.com/minecraft/mc-mods/epic-fight-mod) by Yesman.
---
_Found a bug? Please report it in the Issues tab!_