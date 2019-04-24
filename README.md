# Javassified

A Java-based classified ads system built on Spring Boot.

## Installation

Please, use [Docker](https://docs.docker.com/) to install the app.

```
$ docker-compose build
```
```
$ docker-compose up
```

An admin account with the following log-in details will be created for you:

email: **admin@admin.com**\
password: **123456**

A standard user account with the following log-in details will be created for you:

email: **user@user.com**\
password: **123456**

An admin user can delete all ads. A standard user can only delete their own.

You will need to update the 'application.properties' file with your email settings in order to allow the system to send emails.

Remove the 'data.sql' file if you do not require dummy data.

Finally, load [http://localhost](http://localhost) in your browser.


## Running the tests

```
$ docker-compose run --rm javassified_app ./mvnw test
```
