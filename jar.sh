#!/bin/sh

mkdir -p deliverables/Jar

mvn clean package -Dexec.mainClass="it.polimi.ingsw.codexnaturalis.ClientMain" -Djar.finalName="CodexNaturalis_Client"
mv target/CodexNaturalis_Client.jar deliverables/Jar

mvn clean package -Dexec.mainClass="it.polimi.ingsw.codexnaturalis.ServerMain" -Djar.finalName="CodexNaturalis_Server"
mv target/CodexNaturalis_Server.jar deliverables/Jar