# bad-stat

This project uses Quarkus, to create a parser for Turnier.de. 

## Links
 + https://www.zenrows.com/blog/htmlunit-web-scraping#extract-single-element

## App Links
 + Dev UI : http://localhost:8181/q/dev/
 + HtmlUnit (Maven) : https://mvnrepository.com/artifact/org.htmlunit/htmlunit 

## Running the application in dev mode
+ You can run your application in dev mode that enables live coding using

```shell script
quarkus dev
./mvnw quarkus:dev
```

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script

```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/bad-stat-1.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.

## Related Guides

- YAML Configuration ([guide](https://quarkus.io/guides/config-yaml)): Use YAML to configure your Quarkus application

## Provided Code

### YAML Config

Configure your application with YAML

[Related guide section...](https://quarkus.io/guides/config-reference#configuration-examples)

The Quarkus application configuration is located in `src/main/resources/application.yml`.

### RESTEasy JAX-RS

Easily start your RESTful Web Services

[Related guide section...](https://quarkus.io/guides/getting-started#the-jax-rs-resources)

# JavaFX
-> GitHub Examples: https://github.com/quarkiverse/quarkus-fx/tree/main

# Errors

## BasicLoggingEnabler failed to retrieve config
+ happens with start of a test
+ could be fixed in pom with exclusion of Junit5 properties
+ https://quarkus.io/guides/getting-started-testing#testing_different_profiles

````xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-junit5</artifactId>
    <scope>test</scope>
    <exclusions>
        <exclusion>
            <groupId>io.quarkus</groupId>
            <artifactId>quarkus-junit5-properties</artifactId>
        </exclusion>
    </exclusions>
</dependency>
````
