![banner](https://m.media-amazon.com/images/I/814qEh0JKdS.jpg)

# Codex Naturalis

## Software Engineering project at Politecnico di Milano, academic year 2023/2024

## Team Members (Group GC17)

- [Luca Pozzato](https://github.com/LucaPozzato)
- [Niccolò Puccia](https://github.com/niccolopuccia)
- [Stefano Romano](https://github.com/StRomano02)
- [Edoardo Saso](https://github.com/EdoardoSaso)

## Implemented Functionalities

| Functionality               | Status             |
| --------------------------- | ------------------ |
| Basic Rules                 | :white_check_mark: |
| Complete Rules              | :white_check_mark: |
| CLI                         | :white_check_mark: |
| GUI                         | :white_check_mark: |
| Socket                      | :white_check_mark: |
| RMI                         | :white_check_mark: |
| Chat (FA 1)                 | :white_check_mark: |
| Multiple Matches (FA 2)     | :white_check_mark: |
| Client Disconnection (FA 3) | :white_check_mark: |
| Server Persistence (FA 4)   | :x:                |

[Project requirements](https://github.com/LucaPozzato/ing-sw-2024-pozzato-puccia-romano-saso/blob/main/documents/project_requirements.pdf)

Rule book: [Italian](https://github.com/LucaPozzato/ing-sw-2024-pozzato-puccia-romano-saso/blob/main/documents/CODEX_Rulebook_IT.pdf), [English](https://github.com/LucaPozzato/ing-sw-2024-pozzato-puccia-romano-saso/blob/main/documents/CODEX_Rulebook_EN.pdf)

## Coverage Report

### Controller

| Class            | Method                                            | Line                                             | Branch                                            |
| ---------------- | ------------------------------------------------- | ------------------------------------------------ | ------------------------------------------------- |
| **controller**   | ![🔋](https://img.shields.io/badge/-100%25-green) | ![🔋](https://img.shields.io/badge/-93%25-green) | ![🔋](https://img.shields.io/badge/-76%25-green)  |
| ChooseSetUpState | ![🔋](https://img.shields.io/badge/-100%25-green) | ![🔋](https://img.shields.io/badge/-99%25-green) | ![🔋](https://img.shields.io/badge/-88%25-green)  |
| ControllerState  | ![🔋](https://img.shields.io/badge/-100%25-green) | ![🔋](https://img.shields.io/badge/-86%25-green) | ![🔋](https://img.shields.io/badge/-78%25-green)  |
| DrawnCardState   | ![🔋](https://img.shields.io/badge/-100%25-green) | ![🔋](https://img.shields.io/badge/-90%25-green) | ![🔋](https://img.shields.io/badge/-69%25-orange) |
| EndGameState     | ![🔋](https://img.shields.io/badge/-100%25-green) | ![🔋](https://img.shields.io/badge/-90%25-green) | ![🔋](https://img.shields.io/badge/-83%25-green)  |
| ForcedEndState   | ![🔋](https://img.shields.io/badge/-100%25-green) | ![🔋](https://img.shields.io/badge/-90%25-green) | ![🔋](https://img.shields.io/badge/-100%25-green) |
| InitState        | ![🔋](https://img.shields.io/badge/-100%25-green) | ![🔋](https://img.shields.io/badge/-99%25-green) | ![🔋](https://img.shields.io/badge/-93%25-green)  |
| PlacedCardState  | ![🔋](https://img.shields.io/badge/-100%25-green) | ![🔋](https://img.shields.io/badge/-91%25-green) | ![🔋](https://img.shields.io/badge/-70%25-orange) |
| WaitPlayerState  | ![🔋](https://img.shields.io/badge/-100%25-green) | ![🔋](https://img.shields.io/badge/-96%25-green) | ![🔋](https://img.shields.io/badge/-88%25-green)  |

### Model

| Class               | Method                                            | Line                                              | Branch                                            |
| ------------------- | ------------------------------------------------- | ------------------------------------------------- | ------------------------------------------------- |
| **model**           | ![🔋](https://img.shields.io/badge/-92%25-green)  | ![🔋](https://img.shields.io/badge/-94%25-green)  | ![🔋](https://img.shields.io/badge/-86%25-green)  |
| chat                | ![🔋](https://img.shields.io/badge/-100%25-green) | ![🔋](https://img.shields.io/badge/-100%25-green) | ![🔋](https://img.shields.io/badge/-83%25-green)  |
| enumerations        | ![🔋](https://img.shields.io/badge/-100%25-green) | ![🔋](https://img.shields.io/badge/-100%25-green) | ![🔋](https://img.shields.io/badge/-100%25-green) |
| exceptions          | ![🔋](https://img.shields.io/badge/-100%25-green) | ![🔋](https://img.shields.io/badge/-100%25-green) | ![🔋](https://img.shields.io/badge/-100%25-green) |
| game                | ![🔋](https://img.shields.io/badge/-91%25-green)  | ![🔋](https://img.shields.io/badge/-93%25-green)  | ![🔋](https://img.shields.io/badge/-87%25-green)  |
| **game components** |                                                   |                                                   |                                                   |
| components          | ![🔋](https://img.shields.io/badge/-86%25-green)  | ![🔋](https://img.shields.io/badge/-93%25-green)  | ![🔋](https://img.shields.io/badge/-91%25-green)  |
| parser              | ![🔋](https://img.shields.io/badge/-100%25-green) | ![🔋](https://img.shields.io/badge/-92%25-green)  | ![🔋](https://img.shields.io/badge/-100%25-green) |
| player              | ![🔋](https://img.shields.io/badge/-100%25-green) | ![🔋](https://img.shields.io/badge/-100%25-green) | ![🔋](https://img.shields.io/badge/-100%25-green) |
| strategies          | ![🔋](https://img.shields.io/badge/-100%25-green) | ![🔋](https://img.shields.io/badge/-96%25-green)  | ![🔋](https://img.shields.io/badge/-69%25-orange) |
| Game                | ![🔋](https://img.shields.io/badge/-97%25-green)  | ![🔋](https://img.shields.io/badge/-95%25-green)  | ![🔋](https://img.shields.io/badge/-93%25-green)  |

## Javadocs -> [here](https://lucapozzato.github.io/ing-sw-2024-pozzato-puccia-romano-saso/javadocs/)

## Packages used

- [GSON](https://github.com/google/gson)
- [JUnit](https://junit.org/junit5/)
- [JavaFX](https://openjfx.io)

# Running the application

We suggest to recompile the project and regenerate the jar files, otherwise they are already provided [here](https://github.com/LucaPozzato/ing-sw-2024-pozzato-puccia-romano-saso/blob/main/deliverables/Jar)

## Download the project

Clone this repo by either using the `git clone` command (make sure `git lfs` is installed -> [link for installation](https://github.com/git-lfs/git-lfs)), or downloading the `.zip` and extracting it (may require to recompile jar files).

```sh
cd /path/to/project/home/directory
```

## Enable UTF-8 Encoding on Windows

To enable UTF-8 encoding on a Windows system, follow these steps:

1. **Open Control Panel**:

   - Press `Win + R` to open the Run dialog.
   - Type `control` and press `Enter`.

2. **Navigate to Region Settings**:

   - In the Control Panel, click on `Clock and Region`.
   - Then click on `Region`.

3. **Change System Locale**:

   - In the Region settings window, go to the `Administrative` tab.
   - Click on the `Change system locale...` button.

4. **Enable Beta: Use Unicode UTF-8 for worldwide language support**:
   - In the Region Settings window, check the box for `Beta: Use Unicode UTF-8 for worldwide language support`.
   - Click `OK` to apply the changes.
   - Restart your computer for the changes to take effect.

## Install Java

#### Windows

1. Download the Java SE Development Kit (JDK) from the [Oracle website](https://www.oracle.com/java/technologies/javase-downloads.html).
2. Run the installer and follow the on-screen instructions.

#### Linux (Debian/Ubuntu-based)

1. Open a terminal.
2. Update your package index:

   ```sh
   sudo apt update
   ```

3. Install the default JDK with:

   ```sh
   sudo apt install default-jdk
   ```

#### Linux (Red Hat/CentOS/Fedora-based)

1. Open a terminal.
2. Update your package index:

   ```sh
   sudo yum update
   ```

3. Install the JDK with:

   ```sh
   sudo yum install java-latest-openjdk-devel
   ```

#### Linux (Arch-based)

1. Open a terminal.
2. Update your package index:

   ```sh
   sudo pacman -Syu
   ```

3. Install the JDK with:

   ```sh
   sudo pacman -S jdk
   ```

#### macOS

1. Download the Java SE Development Kit (JDK) from the [Oracle website](https://www.oracle.com/java/technologies/javase-downloads.html).
2. Open the downloaded `.dmg` file and run the installer.

#### macOS (Using Homebrew)

1. Open Terminal.
2. Install Homebrew if it is not already installed by following the instructions at [Homebrew's official website](https://brew.sh/).
3. Install the latest JDK with:

   ```sh
   brew install openjdk
   ```

4. Add the installed JDK to your PATH by typing:

   ```sh
   echo 'export PATH="/usr/local/opt/openjdk/bin:$PATH"' >> ~/.zshrc

   source ~/.zshrc
   ```

### Verify Installation

Once Java is installed, verify the installation by opening your terminal or command prompt and typing:

```sh
java -version
```

You should see the installed Java version.

## Run the provided Jar

#### Server

```sh
java -jar deliverables/Jar/CodexNaturalis_Server.jar
```

#### Client

```sh
java -jar deliverables/Jar/CodexNaturalis_Client.jar
```

and then follow the instructions (like typing ip address and then pressing enter...)

## Recompile Jar

#### UNIX based systems

##### Manual

###### Client

```sh
./mvnw clean package -Dexec.mainClass="it.polimi.ingsw.codexnaturalis.ClientMain" -Djar.finalName="CodexNaturalis_Client"

java -jar target/CodexNaturalis_Client-jar-with-dependencies.jar
```

###### Server

```sh
./mvnw clean package -Dexec.mainClass="it.polimi.ingsw.codexnaturalis.ServerMain" -Djar.finalName="CodexNaturalis_Server"

java -jar target/CodexNaturalis_Server-jar-with-dependencies.jar
```

##### Using shell scripts

```sh
chmod +x jar.sh

./jar.sh
```

The jar.sh script is going to recompile the jar files and place them under deliverables/Jar

To run the jar files using shell scripts:

```sh
chmod +x client.sh

./client.sh

chmod +x server.sh

./server.sh
```

#### Windows

###### Client

```sh
mvnw.cmd clean package -Dexec.mainClass="it.polimi.ingsw.codexnaturalis.ClientMain" -Djar.finalName="CodexNaturalis_Client"

java -jar target/CodexNaturalis_Client-jar-with-dependencies.jar
```

###### Server

```sh
mvnw.cmd clean package -Dexec.mainClass="it.polimi.ingsw.codexnaturalis.ServerMain" -Djar.finalName="CodexNaturalis_Server"

java -jar target/CodexNaturalis_Server-jar-with-dependencies.jar
```

## TUI

### Help

Type `help` and press enter when using the Text-based User Interface or see here the list of all possible commands.

**Commands (case insensitive):**
**Commands (case insensitive):**

```
create: <Game ID>, <nick name>, <password>, <color>, <number of players>
```

_example -> create: 0, nickname, pswd, red, 3_

```
join: <Game ID>, <nick name>, <password>, <color>
```

_example -> join: 0, nickname2, pswd2, green_

```
rejoin: <Game ID>, <nick name>, <password>
```

_example -> rejoin: 0, nickname2, pswd2_

```
choose: <side of card>, <objective card>
```

_example -> choose: front, 1_

```
place: <ID of card to place>, <ID of bottom card>, <position>, <side of card>
```

_example -> place: r01, i01, tr, front_

```
draw: <ID of card to draw (also RXX/GXX)>
```

_example -> draw: r01_

_example -> draw: rxx_

```
chat
```

```
send: <message>
```

_example -> send: hello world_

```
send: <message>, <nickname of receiver>
```

_example -> send: hello world, nickname_

```
next
```

```
esc
```

_to return to previous view_

```
help
```

```
quit
```

_to quit the client_

**Possible colors:**

- yellow
- red
- blue
- green

**Possible value of objective card:**

- 1 (objective card 1)
- 2 (objective card 2)

**Possible value of position:**

- TL (top-left)
- TR (top-right)
- BL (bottom-left)
- BR (bottom-right)

**Possible side of card:**

- front
- back

### Resizing the Terminal Window

If the game view doesn't fit properly in the terminal window, you can adjust the font size to ensure everything is visible. Here are the keyboard shortcuts to decrease the font size on different operating systems:

#### Windows

- **Windows Terminal**: `Ctrl` + `-` (minus key)
- **Command Prompt / PowerShell**: Right-click on the title bar, select `Properties`, go to the `Font` tab, and adjust the font size manually.

#### macOS

- **Terminal**: `Cmd` + `-` (minus key)

#### Linux

The shortcuts may vary slightly depending on the terminal emulator you are using. Here are the shortcuts for some popular terminal emulators:

##### GNOME Terminal

- `Ctrl` + `Shift` + `-` (minus key)

##### KDE Konsole or Xfce Terminal

- `Ctrl` + `-` (minus key)

## GUI

### Instructions for Flipping Cards in the Player's Hand

To switch from the front view of the card to the back and vice versa, simply use the following keyboard shortcuts:

- **F**: Press the "F" key to view the front side of the cards. This will display the details and illustrations specific to each card.
- **B**: Press the "B" key to view the back side of the cards. This will hide the card details, showing only the standard card back.
