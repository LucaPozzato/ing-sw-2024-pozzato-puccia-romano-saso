#!/bin/sh

mvn clean package -Dexec.mainClass="it.polimi.ingsw.codexnaturalis.ClientMain" -Djar.finalName="CodexNaturalis_Client"
java -jar target/CodexNaturalis_Client.jar