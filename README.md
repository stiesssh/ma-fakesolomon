# fakesolomon

This is a fake of the monitoring and issue creation part of the [Solomon Tool](https://github.com/ccims/solomon).
It receives alert from Prometheus, creates a faked issue and forwards the alert *and* the issue id to the [Thesis' backend](https://github.com/stiesssh/ma-backend).

It exists because at the time of the thesis' implementation the Solomon Tool did not yet support the forwarding. (And 'you just gotta add a few lines of code' is a lot of work when you don't know anything about the tools implementation.)


## HTTP API Endpoints 

* `/` : GET greetings
* `/api/v2/alerts` : prometheus alert manager should POST their alerts here

## Spring Properties

As this service is a fake, and is supposed to be removed as soon as Solomon it mature enough, the implementation has as few logic as possible. This also means, that some things, such as knowledge about the architecture must be passed into the fakesolomon via properties. (yeah, basically hardcoded x.x)

Property    | Read from env var | Description
------------|-------------------|----------------
backend.url | BACKEND_URL       | Location of the Thesis' endpoint (e.g. `http://localhost:8083/api/alert`) 
gropius.url | GROPIUS_URL       | Location of the Gropius Backend (e.g. `http://localhost:8080/api` )
gropius.projectId       | GROPIUS_PROJECT_ID            | Id of the Gropius Project to add the faked issues to.
gropius.providerCompId  | GROPIUS_PROVIDER_COMPONENT_ID | Id of the Gropius Component to add the faked issues to.
gropius.providerIfaceId | GROPIUS_PROVIDER_INTERFACE_ID | Id of the Gropius ComponentInterface to add the faked issues to.

## Requirements 

Thing           | Version   
----------------|-----------
Java            | `11`
Docker          | `20.10.8`

## Build and Rund

1. Get into repository 
```
git clone https://github.com/stiesssh/fakesolomon.git
cd fakesolomon
```

2. Build
```
./mvnw clean install
docker build -t fakesolomon .
```

3. Run : Most likely you'll now add a some lines like those below to the `docker-compose.yml` at the Thesis' backend repository and up the fakesolomon together with the backend. Because really, the fakesolomon alone is pretty useless. 
(Do not forget to fill in the correct values for the environment variables.)
```  
  fakesolomon:
    image: fakesolomon
    ports:
      - "8082:8080"
    environment:
      BACKEND_URL: "http://<...>:8080/api/alert"
      GROPIUS_URL: "http://<...>:8080/api/"
      GROPIUS_PROJECT_ID: "..."
      GROPIUS_PROVIDER_COMPONENT_ID: "..."
      GROPIUS_PROVIDER_INTERFACE_ID: "..."

```