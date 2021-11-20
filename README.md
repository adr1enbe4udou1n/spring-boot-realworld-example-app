# ![RealWorld Example App](logo.png)

Spring Boot codebase containing real world examples (CRUD, auth, advanced patterns, etc) that adheres to the [RealWorld](https://github.com/gothinkster/realworld-example-apps) spec and API.

## [RealWorld](https://github.com/gothinkster/realworld)

This codebase was created to demonstrate a fully fledged fullstack application built with Spring Boot including CRUD operations, authentication, routing, pagination, and more.

We've gone to great lengths to adhere to the Python community styleguides & best practices.

For more information on how to this works with other frontends/backends, head over to the [RealWorld](https://github.com/gothinkster/realworld) repo.

## Usage

### PostgreSQL

This project use **PostgreSQL** as main database provider. You can run it easily via `docker-compose up -d`.

Two databases will spin up, one for normal development and one dedicated for integrations tests.

### Run app

```sh
cp .env.example .env # access for above container
gradlew bootRun --args="--seed" # fake data with faker
gradlew bootRun # run
```

And that's all, go to <http://localhost:8080/api/swagger-ui.html>

### Validate API with Newman

Launch follow scripts for validating realworld schema (be sure to have wipped database before) :

```sh
gradlew bootRun
npx newman run postman.json --global-var "APIURL=http://localhost:8080/api" --global-var="USERNAME=johndoe" --global-var="EMAIL=john.doe@example.com" --global-var="PASSWORD=password"
```

### Full test suite

This project is fully tested via JUnit, just run `gradlew test` for launching it.

## License

This project is open-sourced software licensed under the [MIT license](https://adr1enbe4udou1n.mit-license.org).
