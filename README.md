# Shopify Marketplace Back-End | Summer 2019 Developer Intern Challenge Question

A sample Shopify marketplace back-end. Supports inventory manipulation. Created for the Shopify Summer 2019 Back-End Developer challenge.

## Introduction
This back-end was created with Java Spring Boot, and features Swagger integration for easy visualization of endpoints as well as endpoint testing. For easy setup, an H2 in-memory database was used; in the real world, the database would be hosted on the cloud, but for the purposes of this challenge I chose an in-memory database. Test cases were created to test each endpoint using MockMVC.

## Setup
If you haven't already, install Maven at `https://maven.apache.org/install`, as well as Java at `https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html`.

Clone the repository into a directory of your choosing, and run the Spring Boot application with Maven using `mvn spring-boot:run`. For the interactive Swagger UI, go to `localhost:8080/swagger-ui.html`. From this page you can view and test each endpoint.

Unit tests can be performed by running `mvn clean test`.

## Additional info

Thank you for the opportunity to take this challenge! It was really fun being able to create a simple app like this and test the knowledge/skills I learned from my previous co-ops.

Cheers!

P.S.: If you're reading this README.md through a download .zip, feel free to check out the GitHub repo at `https://github.com/brian1999lin/shopifyinventoryapp`.
