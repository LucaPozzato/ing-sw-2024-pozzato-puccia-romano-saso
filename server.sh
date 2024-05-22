#!/bin/sh

mvn clean package -Dexec.mainClass="it.polimi.ingsw.codexnaturalis.ServerMain" -Djar.finalName="CodexNaturalis_Server"
java -jar target/CodexNaturalis_Server.jar