# An example of the reactive application with event sourcing

It's based on the Spring Boot and Akka stacks.

# Running the application

## Running the application with PostgreSQL as an event journal

1. Launch db in `development` folder:
```
docker-compose -f development/docker-compose-jdbc.yml up    
```
2. Launch application:
```
./gradlew bootRun    
```

## Running the application with Cassandra as an event journal

1. Launch Cassandra in `development` folder:
```
docker-compose -f development/docker-compose-cassandra.yml up    
```

2. Launch application:

To run our application with Cassandra plugin, add the -Dconfig.resource=/application-cassandra.conf
VM option in Intellij launch configuration or run it directly from the command line:

```
./gradlew bootRun --args='-Dconfig.resource=/application-cassandra.conf'
```

# Testing the application
```
chmod +x ./development/show.sh
```

```
./development/show.sh
```

# Architectural Decision Log

See [here](doc/index.md)