# SiteMinder SendSimple Application
Overview: A REST api application whose function is to send email/s to recipient/s using an email provider.
In this current setup, there are two email providers (MailGun and SendGrid). It would first try the first email provider service; 
if it's successful it would return a (200) successful response. If not, it would switch to another email provider. 
If all email providers are somehow unavailable, it would return a 500 internal server error, including a message.

Environment: JDK 11
Framework: Spring Framework, Boot.

How to run:

1. Run command mvn clean install
2. Run command java -jar {filename}.jar
3. Enter localhost:8080/swagger-ui.html through a browser for Swagger Documentation
4. Alternatively, you can clone this repository and run it using an IDE preferably Intellij

Please note that apikeys for SendGrid and MailGun were excluded for security purposes.
Kindly provide your own apikeys for SendGrid and Mailgun. You should also replace the api for mailgun as it requires a 
domain which is generated for sandbox testing.
