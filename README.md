Beatshot
========

  Beatshot is an 8-bit stylized Vertical Shooter/Bullet Curtain game that makes use of the Beatmania Controller as its main way of playing.
  
Stylistic Guidelines
====================
  - Assets may not contain more than 4 colors (Transparency is not considered a separate color, we are using PNGs)
  - Colors are restricted to the NES Technical capabilities
  - Assets sizes are
    - Bullets 8x8
    - Player 24x24 (Collision box is 16x16)
    - Boss 24x24
    - Enemy 16x16
    - Parallax Map 192x(256-2048)
    - Character Portraits 64x64

Controls
========

  The player's ship may only move along one axis.  Since Beatshot is a vertical shooter that means the player will move along the x-axis instead of the y-axis.
  
  Movement will be controlled by the Disc
  - Clockwise - right
  - Counter-Clockwise - left
  
  The ship is equipped with 5 weapons that are capable of being fired simultaneously.  Each button of the 5-key Beatmania controller will fire a different laser, while each laser will produce bullets that follow a unique path.

  Alternatively, the game can be played on the PC with Keyboard controls
  - Z,X,C,V,SPACE - fire lasers
  - Arrow Left, Right - move ship
  
  Since controls are also visible on screen, mobile devices such as the Android or iPhone should be able to play by pressing the on-screen controls.

Mechanics
=========

  Lasers consume ammo at a rate of 10 bullets per second while Ammo recharges at a rate of 20 bullets per second.
  - no lasers = +20 bps
  - 1 laser = +10 bps
  - 2 laser = 0 bps
  - 3 laser = -10 bps
  - 4 laser = -20 bps
  - 5 laser = -30 bps
  
  Each bullet deals 1 damage to the enemy, maximum hp an enemy may have is 999 hp.
  - basic = 1 hp
  - medium = 3 hp
  - mid-boss = 25 * (level / 2) hp
  - boss = 50 * level hp
  
  Beatshot will consist of 7 levels.

  Movement speed of the player is 128 pixels per second, crossing from one side to the other should take almost 2 seconds
  
Scripted Dialog
===============
  Dialog will be displayed at the bottom of the screen where the controls are normally located.  Dialog can consist of the actor's name, a portrait, and will display 3 32 character lines at a time.  During scripted dialog sequences the background will continue to scroll, but the music will be silent by deafault.  A specific music track can be set for the dialog and will stop afterwards.
  Scripted dialog may only appear at the beginning of the level, before the mid-boss, before the level-boss, and after the level-boss.
  
Licensing
=========
Copyright 2013 Nicholas Hydock

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this project's source files except in compliance 
  with the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.

  Assets are Licensed under the Creative Commons - Attribution-ShareAlike 3.0 Unported (CC BY-SA 3.0).

      http://creativecommons.org/licenses/by-sa/3.0/
 
