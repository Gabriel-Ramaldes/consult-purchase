# Store and Consult Purchase Transactions

## What is it:

Consult Purchase App is an application made in JAVA using Spring Boot Framework. 
The application is capable of saving a purchase transaction with a unique identifier,
a description, a value in USD and a transaction date. Once saved, the user can consult
this transaction with the value converted into a currency from another country using the 
conversion rates provided by the Treasury Reporting API.

  https://fiscaldata.treasury.gov/datasets/treasury-reporting-rates-exchange/treasury-reporting-rates-of-exchange

### Architecture:

The Spring Boot Framework was used for its ease in generating a web project with tests and rest connections.
The design pattern chosen was the Repository-Service-Controller, widely used in SpringBoot applications, 
to separate the layers with the endpoints, business rules and connection to the database. 
The Entities and DTOs are located in the Model directory.
The H2 memory database was used as a prerequisite as the application must be fully functional without installing
separate databases, web servers, servlets or containers.

## Getting started:

### Pre-requisites:

There are some prerequisites to be validated before using the application, 
such as the saved purchase transaction must have a description of a maximum of 50 characters,
a valid and mandatory local date and a positive value in dollars with a maximum of 12 whole digits and 2 fractionals digits.
Any discrepancy in this will be returned by the function which field is incorrect.

To Consult a purchase transaction stored in database you'll need its ID which can be recovered after storing a 
purchase in the database or by checking the database purchase table in the h2 console (see how in Running and Testing).

### Endpoints:

	URL : /purchase
		  POST : Saves a purchase transaction, needs a Json object in the body. The required fields and their validations are documented in the code and in the challenge.
          /purchase/consult/{id}/{currency} 
          GET : Consult a saved purchase transaction and convert the value to the desired currency, the default currency parameter is Country-Curreny (e.g.: Brazil-Real). 

### Running and Testing:

You'll need *JAVA JDK 17* in your machine to run this application.
You can clone this project and then import it to your IDE and run it. 
It is also possible to go to the project's root folder and use MAVEN to run these commands in the shell:

    -mvn spring-boot:run
    -mvn test

The H2 database is stored in memory and can be accessed at http://localhost:8080/h2 and the credentials are stored in 
the 'application.properties' file, the default user/pass are admin/admin.
For manual testing, you can use a tool like Postman or Insomnia to call the endpoints.
Some example calls to get started are:

    POST/ http://localhost:8080/purchase 
    (Remenber to put Content-Type application/json in your header)

  A valid JSON should look like this:

    {
	"desc": "Your first transaction stored!",
	"date": "2024-03-10",
	"amount": 1856.56
    }

  And to consult:

     GET/ http://localhost:8080/purchase/consult/{id}/{currency}
     (Use the id returned in the POST call and some currency like: Brazil-Real,Euro Zone-Euro,Canada-Dollar.)

