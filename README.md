# voting-system-rest
REST service to vote for a restaurant

<p align="center">
   <img src="https://img.shields.io/badge/Version-0.0.1-important" alt="App Version">
   <img src="https://img.shields.io/badge/Lecense-MIT-9cf" alt="License">
</p>

## About
REST service for creating a restaurant, as well as a menu for them. And after that, users can vote for the restaurant.

## Documentation
Если вы не будете использовать докер, вам потребуется база данных **postgreSQL**. Для сборки jar вы должны использовать maven 

```
./mvnw -N package
```

<details>
   <summary> Subject area</summary>
  
  ![image](https://github.com/gnori-zon/voting-system-rest/assets/108410527/120253a4-ba2a-4ddc-ad2e-eab3a95822ab)

**If you are not using docker:** The database structure is generated automatically by liquibase so there is no need to create anything manually. You need to have just an empty database.
</details>

<details>
   <summary> Config for app </summary>
   
   ```yaml
  spring:
  liquibase:
    enabled: true
  datasource:
   # (address of database)
    url: 
    username:
    password:
    # (dirver for database)
    driver-class-name:
  jpa:
    show-sql: true
    hibernate.ddl-auto: none

security:
  jwt:
  # 128-bit key to encrypt the jwt token
    secret: 
  # token expiration time in seconds (default: 1 week)
    expiration: 604800

logging:
  level:
    root: info
    web: error
    sql: error
  file:
  # log file name with path relative to directory 'src'
      name: src/app.log
      max-history: 1
  pattern:
  # log file view pattern
    file: '%d{HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n'
    
# activating controllers profiling and outputting them to the logs
profiling:
  all:
    controllers: false
   ```
</details>

<a href="https://github.com/gnori-zon/voting-system-rest/tree/master/docker#readme">docker<a>

You can use UI access after running the application from the link: http://localhost:8080/swagger-ui.html

## Developers

- [gnori-zon](https://github.com/gnori-zon)
