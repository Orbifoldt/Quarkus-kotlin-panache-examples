# Quarkus-kotlin-panache-examples Project

In this project I used the Quarkus framework: https://quarkus.io/ . This project contains some example of how to create and persist a data model using Hibernate ORM. Quarkus has a very nice library for this, namely Panache. Using the repository-model we create entities using JPA annotations and then we can persist them with said Panache-repositories. 

Most examples of Quarkus use Java, in this project we demonstrate how easy it is to create a back-end application with Kotlin and Quarkus. Also, most sample projects just show a single table (without any relations) in action. That's why I chose to create more complex data models with relationships, for example I've added both a bidirectional and unidirectional one-to-many model for bands, albums and songs. While we don't do anything fancy -- we just use JPA -- I think it's nice to have an example of Quarkus + Kotlin with some non-trivial data structures.

A small note: a better practice would be to have the entities be immutable. One of the main reasons to prefer Kotlin to Java is the inherent immutability of collections (unless specifically defined to be mutable). Sadly, Hibernate ORM  and Panache don't work like that by default. See [Exposed](https://github.com/JetBrains/Exposed) by JetBrains, or [Krush](https://github.com/TouK/krush) (this combines JPA with Exposed) for a more Kotlin way of handling persistence (it's not a full ORM though).

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/.


