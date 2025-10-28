## 🩸 Nightblood Mod

A powerful Minecraft mod that adds blood-powered weapons and night-enhanced tools with unique mechanics and abilities.

## 👥 Credits

**Developer**: Nighttie
**Mod ID**: `nightblood`
**Category**: Equipment, Magic, Adventure

---

<div align="center">

### ⭐ If you enjoy this mod, please consider giving it a star!

**Made with ❤️ and lots of ☕**

*"In the darkness, we find strength. In blood, we find power."*

## ✨ Features

- **Night-Powered Tools** - Weapons and tools that gain special abilities during nighttime
- **Blood System** - Collect and use blood to empower your equipment
- **Blood Altar** - Humane blood collection without harming animals
- **Custom HUD** - Visual blood meter when holding blood items
- **Area Mining** - Blood Pickaxe can mine large areas based on blood level
- **Enhanced Looting** - Blood Sword grants looting bonuses

---

## 🗡️ Items

### 🌙 Night Items

#### **Night Sword**
- **Special Ability**: Gains **+10 damage** during nighttime (13000-23000 game time)
- **Visual Effect**: Glows and emits enchantment particles at night
- **Enchantable**: Yes
- **Durability**: Standard diamond-tier

#### **Night Pickaxe**
- **Special Ability**: 
  - **2x mining speed** during nighttime
  - **Fortune III** automatically applied at night (if lower)
- **Visual Effect**: Glows and emits enchantment particles at night
- **Enchantable**: Yes
- **Durability**: Standard diamond-tier

---

### 🩸 Blood Items

#### **Blood Sword**
- **Power Source**: Player's blood level (0-100)
- **Damage Scaling**: 
  - Every 10 blood = **+1.5 damage**
  - Maximum: **+15 damage** at 100 blood
- **Looting Bonus**:
  - Every 10 blood = **+1 Looting level**
  - Maximum: **Looting X** at 100 blood
- **Visual**: Glows when player has blood
- **Tooltip**: Shows current damage and looting bonuses
- **Hidden Name**: Name hidden in action bar for clean display

**Blood Levels:**
```
0-9 blood   → No bonus
10-19 blood → +1.5 damage, Looting I
20-29 blood → +3 damage, Looting II
...
90-100 blood → +15 damage, Looting X
```

#### **Blood Pickaxe**
- **Power Source**: Player's blood level (0-100)
- **Mining Speed**: 
  - Scales from 1x to 2x speed based on blood
  - 100 blood = **100% faster mining**
- **Area Mining**: Mines in expanding patterns (ignores ores)
  - 0-9 blood: **1x1** (normal)
  - 10-19 blood: **2x2** area
  - 20-39 blood: **3x3** area
  - 40-79 blood: **4x4** area
  - 80-100 blood: **5x5** area
- **Disable Area Mining**: Hold SHIFT while mining
- **Ore Protection**: Never breaks ores in area mode (mine them manually)
- **Visual**: Glows when player has blood

#### **Blood Bottle (Empty)**
- **Purpose**: Collect blood from passive animals
- **Usage**: Right-click on animals (cows, pigs, sheep, etc.)
- **Effects**:
  - **Normal**: Deals 4 damage + Poison II (10s) to animal
  - **On Blood Altar**: No damage, no poison (humane collection)
- **Result**: Converts to Blood Bottle (Filled)
- **Stack Size**: 16

#### **Blood Bottle (Filled)**
- **Purpose**: Restore player's blood level
- **Usage**: Drink like a potion (right-click and hold)
- **Restoration**: **+5 blood** per bottle
- **Result**: Returns empty Blood Bottle
- **Stack Size**: 16
- **Duration**: 32 ticks (1.6 seconds) to consume

---

## 🏛️ Blocks

### **Blood Altar**
<img src="docs/blood_altar_preview.png" alt="Blood Altar" width="200"/>

- **Size**: 14x8x14 (smaller than full block, like a cake)
- **Purpose**: Humane blood collection station
- **Effects**:
  - Animals on altar take **no damage** from Blood Bottle
  - Animals on altar receive **no poison** effect
  - Blood collection still works normally
- **Recipe**: [To be added]
- **Durability**: 2.0 hardness, 6.0 blast resistance

**Usage:**
1. Place Blood Altar on ground
2. Lead animal onto the altar
3. Use Blood Bottle on animal
4. Collect blood without harming the creature!

---

## 💻 Commands

### `/blood` - Blood Management Commands
*Requires OP level 4*

#### **Add Blood**
```
/blood add <player> <amount>
```
- Adds blood to a player (1-100)
- Example: `/blood add @s 50` (adds 50 blood to yourself)

#### **Remove Blood**
```
/blood remove <player> <amount>
```
- Removes blood from a player (1-100)
- Example: `/blood remove Steve 20`

#### **Set Blood**
```
/blood set <player> <amount>
```
- Sets exact blood level (0-100)
- Example: `/blood set @a 100` (sets all players to 100 blood)

#### **Check Blood**
```
/blood get <player>
```
- Displays player's current blood level
- Example: `/blood get @p`

---

### Requirements
- Minecraft 1.21.1
- NeoForge 21.1.209 or higher

---

## 🎮 Gameplay Guide

### Getting Started

1. **Craft Night Items** for basic nighttime advantages
2. **Craft Blood Bottles** to start collecting blood
3. **Find animals** to collect blood from
4. **Optional**: Build a Blood Altar for humane farming

### Blood System Mechanics

#### **Gaining Blood:**
- Drink **Blood Bottle (Filled)**: **+5 blood**
- Use commands (creative/admin only)

#### **Losing Blood:**
- Taking damage: **-10 blood** per hit
- Natural decay: **-1 blood per minute**

#### **Blood HUD:**
- Appears when holding blood items or bottles
- Shows blood drop icon (fills based on blood level)
- Displays **+/-** changes for 2 seconds when blood changes
- Color-coded:
  - **Red (+X)**: Gained blood
  - **Gray (-X)**: Lost blood

### Tips & Tricks

💡 **Blood Sword Usage:**
- Keep blood level high for maximum damage output
- Combine with Sharpness enchantment for devastating attacks
- Natural looting bonus saves inventory space

💡 **Blood Pickaxe Usage:**
- Perfect for clearing large areas of stone/netherrack
- Hold SHIFT when you want precision mining
- Ores are automatically protected in area mode
- Best used with high blood levels for speed + area

💡 **Blood Altar Benefits:**
- Set up near your animal farm
- No need to heal animals constantly
- More efficient than normal collection
- Animals stay healthy and productive

💡 **Night Items Strategy:**
- Use during night for maximum efficiency
- Night Pickaxe auto-applies Fortune III
- Night Sword becomes incredibly powerful
- Plan mining/combat sessions for nighttime

---

## ⚙️ Configuration

Currently, the mod uses hardcoded values. Future updates may include config files for:
- Blood drain rate
- Blood gain per bottle
- Night time range
- Damage/speed multipliers
- Area mining sizes

---

## 🔄 Changelog

### Version 1.0.0 (Current)
- Initial release
- Added Night Sword and Night Pickaxe
- Added Blood Sword and Blood Pickaxe
- Added Blood Bottle system
- Added Blood Altar block
- Implemented blood HUD
- Added blood management commands
- Area mining with blood scaling
- Looting bonus system


## 📝 Planned Features

- [ ] Config file for customization

