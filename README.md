# Store and Consult Purchase Transactions

## What is it:

Consult Purchase is an application made in JAVA using Spring Boot Framework. 
The application is capable of saving a purchase transaction with a unique identifier,
a description, a value in USD and a transaction date. Once saved, the user can consult
this transaction with the value converted into a currency from another country using the 
conversion rates provided by the Treasury Reporting Rates of Exchange API.

  https://fiscaldata.treasury.gov/datasets/treasury-reporting-rates-exchange/treasury-reporting-rates-of-exchange

### Architecture:

The Spring Boot framework was used due to its popularity for building a web application and its agility and ease of starting a project.
The design pattern chosen was the Repository-Service-Controller, widely used in SpringBoot applications, 
to separate the layers with the endpoints, business rules and connection to the database. 
The Entities and DTOs are located in the Model directory.
The H2 memory database was used as a prerequisite as the application must be fully functional without installing
separate databases, web servers, servlets or containers.

## Getting started:

### Prerequisites:

There are some prerequisites to be validated before using the application, 
such as the saved purchase transaction must have a description of a maximum of 50 characters,
a valid and mandatory local date and a positive value in dollars with a maximum of 12 whole digits and 2 fractionals digits.
Any discrepancy in this will be returned by the function which field is invalid.

To Consult a purchase transaction stored in database you'll need its ID which can be recovered after storing a 
purchase in the database, by consult all the purchases stored by date or by checking the database purchase table in the h2 console (see how in Running and Testing).

### Endpoints:

	URL :    /purchase
		   POST : Saves a purchase transaction, needs a Json object in the body.
             /purchase/consult 
               GET : Consult a saved purchase transaction and convert the value to a desired currency.
                 PARAMETERS: id - UUID, currency - text in the pattern Country-Currency.
             /purchase/findPageByDate
               GET: Consult a page with a size and index filtered by a date ordered by id.
                 PARAMETERS: size - optional default is 5, page - optional the index of the page the default is 0, date - required date formatted like yyyy-MM-dd

### Running and Testing:

You'll need *JAVA JDK 17* in your machine to run this application.
You can clone this project and then import it to your IDE and run it. 
It is also possible to go to the project's root folder and use MAVEN to run these commands in the shell:

    -mvn spring-boot:run
    -mvn test

The H2 database is stored in memory and can be accessed at http://localhost:8080/h2
if you uncomment the two console lines, the credentials are stored in 
the 'application.properties' file, the default user/pass are admin/admin.
For manual testing, you can use a tool like Postman or Insomnia to call the endpoints.
Some example calls to get started are:

    POST/ http://localhost:8080/purchase 
    (Remenber to put Content-Type application/json in your header)

  A valid JSON should look like this next example:

    {
	"desc": "Your first transaction stored!",
	"date": "2024-03-12",
	"amount": 1856.56
    }

  To consult what is stored in the database you can find all purchases by date:

    GET/ http://localhost:8080/purchase/findPageByDate?page={page}&size={size}&date={date}
    (The page and size parameters are optional and their default value is 0 and 10. The date should be formatted like yyyy-MM-dd.)
    Example: http://localhost:8080/purchase/findPageByDate?page&size&date=2024-03-12

  And to consult and convert the amount of a purchase:

     GET/ http://localhost:8080/purchase/consultconsult?id={id}&currency={currency}
     (Use the id returned when stored or consult by date and some currency like: Brazil-Real,Euro Zone-Euro,Canada-Dollar.)
     Example: http://localhost:8080/purchase/consult?id=c3c5977f-a735-46e8-b71f-b2af9516b510&currency=Brazil-Real
