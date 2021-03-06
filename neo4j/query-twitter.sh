#!/bin/bash
mvn install:install-file -DgroupId=com.galaxybase.developer -DartifactId=bolt-driver -Dversion=3.0.1 -Dpackaging=jar -Dfile=../galaxybase-developer/lib/galaxybase-bolt-driver-3.0.1.jar
mvn -f ../pom.xml clean
mvn -f ../pom.xml package
rm -rf Twitter-2010/neo4j.jar
cp target/benchmark-neo4j-1.0.0-SNAPSHOT-jar-with-dependencies.jar Twitter-2010/neo4j.jar
java -jar Twitter-2010/neo4j.jar
