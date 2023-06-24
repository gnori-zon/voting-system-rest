You can configuring the main application.
- In service "bd" you can change ```POSTGRES_USER```, ```POSTGRES_PASSWORD``` and ```POSTGRES_DB```.
If the setting is changed, then the ```SPRING_DATASOURCE_USERNAME```, ```SPRING_DATASOURCE_PASSWORD``` and ```SPRING_DATASOURCE_URL``` settings in the ```app```  will also need to be changed.
- Also, you need to specify ```SECURITY_JWT_SECRET``` and ```SECURITY_JWT_EXPIRATION```, which are token params. 
- After can configure ```PROFILING_ALL_CONTROLLERS```

Before start docker-compose you need have **volume** for saving db data. You may create volume it by typing:

```
dicker volume create db-data-voting-system-rest
```

Once configured, you can run it by typing:

```
docker-compose -f docker-compose.yaml up
```

And also stop:

```
docker-compose -f docker-compose.yaml down
```

<details>
   <summary> Subject area</summary>

```yaml
version: '2'

services:
  app:
    container_name: app
    image: 'voting-system-rest:latest'
    build:
      context: .
    ports:
      - "8080:8080"
    depends_on:
      - db
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/voting-system-rest-db
      - SPRING_DATASOURCE_USERNAME=user01
      - SPRING_DATASOURCE_PASSWORD=password01
      - SECURITY_JWT_SECRET=4528482B4D6251655468566D597133743677397A24432646294A404E63526655
      - SECURITY_JWT_EXPIRATION=604800
  db:
    container_name: db
    image: 'postgres:14.5-alpine'
    volumes:
      - db-data-voting-system-rest:/var/lib/postgresql/data
    environment:
      - POSTGRES_DB=voting-system-rest-db
      - POSTGRES_USER=user01
      - POSTGRES_PASSWORD=password01
volumes:
  db-data-voting-system-rest:
    external: true
    
```

</details>
