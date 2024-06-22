![banner 1386x400px](https://github.com/LucaPozzato/ing-sw-2024-pozzato-puccia-romano-saso/src/main/resources/it/polimi/ingsw/codexnaturalis/Backgrounds/github_banner.png)

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
| Basic Rules                 | :heavy_check_mark: |
| Complete Rules              | :heavy_check_mark: |
| CLI                         | :heavy_check_mark: |
| GUI                         | :heavy_check_mark: |
| Socket                      | :heavy_check_mark: |
| RMI                         | :heavy_check_mark: |
| Chat (FA 1)                 | :heavy_check_mark: |
| Multiple Matches (FA 2)     | :heavy_check_mark: |
| Client Disconnection (FA 3) | :heavy_check_mark: |
| Server Persistence (FA 4)   | :x:                |

[Project requirements](https://github.com/LucaPozzato/ing-sw-2024-pozzato-puccia-romano-saso/documents/project_requirements.pdf)

Rule book: [Italian](https://github.com/LucaPozzato/ing-sw-2024-pozzato-puccia-romano-saso/documents/CODEX_Rulebook_IT.pdf), [English](https://github.com/LucaPozzato/ing-sw-2024-pozzato-puccia-romano-saso/documents/CODEX_Rulebook_EN.pdf)

# Running the application

We suggest to recompile the project and regenerate the jar files, otherwise they are already provided [here](https://github.com/LucaPozzato/ing-sw-2024-pozzato-puccia-romano-saso/deliverables/Jar)

## Download the project

Clone this repo by either using the `git clone` command, or downloading the `.zip` and extracting it.

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

java -jar target/CodexNaturalis_Client.jar
```

###### Server

```sh
./mvnw clean package -Dexec.mainClass="it.polimi.ingsw.codexnaturalis.ServerMain" -Djar.finalName="CodexNaturalis_Server"

java -jar target/CodexNaturalis_Server.jar
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

java -jar target/CodexNaturalis_Client.jar
```

###### Server

```sh
mvnw.cmd clean package -Dexec.mainClass="it.polimi.ingsw.codexnaturalis.ServerMain" -Djar.finalName="CodexNaturalis_Server"

java -jar target/CodexNaturalis_Server.jar
```

## TUI help

Type `help` and press enter when using the Text-based User Interface or see here the list of all possible commands.

**Commands (case insensitive):**
**Commands (case insensitive):**

```
create: <Game ID>, <nick name>, <password>, <color>, <number of players>
```

```
join: <Game ID>, <nick name>, <password>, <color>
```

```
rejoin: <Game ID>, <nick name>, <password>
```

```
choose: <side of card>, <objective card>
```

```
place: <ID of card to place>, <ID of bottom card>, <position>, <side of card>
```

```
draw: <ID of card to draw (also RXX/GXX)>
```

```
chat
```

```
send: <message> [, <nick name of receiver>]
```

```
next
```

```
esc
```

```
help
```

```
quit
```

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

## Coverage

| Package            | Class | Line |
| ------------------ | ----- | ---- |
| model              |       |      |
| model.chat         |       |      |
| model.enumerations |       |      |
| model.exceptions   |       |      |
| model.game         |       |      |
| controller         |       |      |

## [Javadocs](https://github.com/LucaPozzato/ing-sw-2024-pozzato-puccia-romano-saso/javadoc)
