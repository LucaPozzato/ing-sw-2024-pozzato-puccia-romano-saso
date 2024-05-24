# Software Engineering Project

## Codex Naturalis

### To play the game

#### Jar

To generate Jar file for client/server

`> chmod +x jar.sh`

`> ./jar.sh`

#### Client

`> /jar.sh`

`> chmod +x client.sh`

`> ./client.sh`

or

`> mvn clean package -Dexec.mainClass="it.polimi.ingsw.codexnaturalis.ClientMain" -Djar.finalName="CodexNaturalis_Client"`

`> java -jar target/CodexNaturalis_Client.jar`

if playing from Windows make sure UTF-8 is enabled

#### Server

`> /jar.sh`

`> chmod +x server.sh`

`> ./server.sh`

or

`> mvn clean package -Dexec.mainClass="it.polimi.ingsw.codexnaturalis.ServerMain" -Djar.finalName="CodexNaturalis_Server"`

`> java -jar target/CodexNaturalis_Server.jar`
