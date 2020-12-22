#!/bin/bash
mvn install:install-file -DgroupId=com.galaxybase.developer -DartifactId=bolt-driver -Dversion=3.0.1 -Dpackaging=jar -Dfile=../galaxybase-developer/lib/galaxybase-bolt-driver-3.0.1.jar
mvn -f ../pom.xml clean
mvn -f ../pom.xml package
rm -rf Graph500/tigergraph.jar
cp target/benchmark-tiger-1.0.0-SNAPSHOT-jar-with-dependencies.jar Graph500/tigergraph.jar
java -jar Graph500/neo4j.jar
