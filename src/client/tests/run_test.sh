#!/bin/sh
javac *.java
java -cp ../mysql-connector-java-8.0.18.jar:..:. PrintCursorTest
