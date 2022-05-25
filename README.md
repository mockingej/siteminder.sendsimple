# SiteMinder SendSimple Application
Overview: A REST api application whose function is to send email/s to recipient/s using one or more email providers.
In this current configuration, there are two email providers (MailGun and SendGrid).

Environment: JDK 11

How to run:

1. Run command mvn clean install
2. Run command java -jar {filename}.jar
3. Enter localhost:8080/swagger-ui.html through a browser for Swagger Documentation
4. Alternatively, you can clone this repository and run it using an IDE preferably Intellij

Please note that apikeys for SendGrid and MailGun were excluded for security purposes.
Kindly provide your own apikeys for SendGrid and Mailgun. You should also replace the api for mailgun as it requires a 
domain which is generated for sandbox.