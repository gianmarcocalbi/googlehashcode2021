#!/bin/zsh
mvn compile exec:java -Dexec.mainClass="com.google.hashcode.App" -Dexec.args="$1 $2"