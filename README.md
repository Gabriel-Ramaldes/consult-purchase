# Store and Consult Purchase Transactions
Code Challenge:

Consult Purchase App is an application made in JAVA using Spring Boot Framework. 
The application is capable of saving a purchase transaction with a unique identifier,
a description, a value in USD and a transaction date. Once saved, the user can consult
this transaction with the value converted into a currency from another country using the 
conversion rates provided by the Treasury Reporting API.

  https://fiscaldata.treasury.gov/datasets/treasury-reporting-rates-exchange/treasury-reporting-rates-of-exchange

The Spring Boot Framework was used because it was easy to generate a web project with tests and connections.
The H2 memory database was used as a prerequisite as the application must be fully functional without installing
separate databases, web servers, servlets or containers.

### Endpoints:

	URL : /purchase
		GET/id={id}&currency={currency} : Consult a saved purchase transaction and convert the value to the desired currency, the default currency parameter is Country-Curreny (e.g.: Brazil-Real). 
		POST : Saves a purchase transaction, needs a Json object in the body. The required fields and their validations are documented in the code and in the challenge.

### Running and Testing:
You can open the application with an IDE and run it. It is also possible to go to the project's root folder 
and run the commands in the shell as:

    -mvn spring-boot:run
    -mvn test

The H2 database is stored in memory and can be accessed at http://localhost:8080/h2 and the credentials are stored in 
the application.properties, the default user/pass are admin/admin.
For manual testing, you can use a tool like Postman or Insomnia to call the endpoints.
Some example calls to get started are:

    POST/ http://localhost:8080/purchase 
    (Remenber to put Content-Type application/json in your header)

A valid Json should look like this:

    {
	"desc": "Your first transaction stored!",
	"date": "2024-03-10",
	"amount": 1856.56
    }

And to consult:

     GET/ http://localhost:8080/purchase/id={id}&currency={currency}
     (Use the id returned in the POST call and some currency like: Brazil-Real,Euro Zone-Euro,Canada-Dollar.)
     
