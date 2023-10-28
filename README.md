# GenAI Training Repo

## Thought

By Github Copilot to quick code a [readworld application](https://www.realworld.how/)

## Archtecture & Test Strategy

### Frontend

Technology stack: React, React Hook, Pact.js, jotai.

- View layer: mainly consists of pages and encapsulated React components.
    - Rendering: ViewModel is prepared using jotai, and View is rendered to verify that the page interaction behavior is correct.
    - Invocation: ViewModel is prepared using jotai, and React components are used to verify that ViewModel layer methods are properly invoked after View behavior triggers.
- ViewModel layer: data structures are encapsulated using jotai state library and accompanied by operation controllers.
    - Intra-layer behavior: Using the render hook, after triggering controller behavior, verify whether the jotai ViewModel data has been accurately affected.
    - Application boundaries: Using Pact.js Stub WebServer, verify whether ViewModel within Jotai has been accurately affected after triggering Controller behavior.

### Backend

Technology Stack: Springboot MVC with Kotlin, Springboot data jpa for database, Pact Provider for JVM, Mockk as the mock libray

- Controller Layer: Define Controllers related to Rest APIs and their respective DTOs
    - Stub Model and other services, Stub WebServer, validate Controllers for correct API definition and parameter validation
- Model Layer: Define core domain objects and relevant core business logic
    - Stub Repository services, validate the correctness of method calls in the Model layer
- Repository Layer: Define Mapper objects corresponding to database tables and the respective Repository services
    - Fake database, validate Repository call behavior correctness

## TODO Feature List

### CRF Features

- [x] A Base Framework for web and server
- [x] A Testing enviroment (include pact test between web and server) setup
- [x] intercept user when not login in authenticated endpoints
- [ ] tech debt: if controller not defined rest mapping, Springboot will return 401 not 404 HTTP status 
- [x] handle the token invalid case redirect to login page

### Business Features

- [x] Authenticate users via JWT (login/signup pages + logout button on settings page)
    - [x] Fake API login API, setup Login Page, when login load User Model to global store and set jwt token to localStorage
    - [x] Stub Provider Server, when login request comes, server should return jwt token and user info
    - [x] user register on user not registered and user username or email exist case
    - [x] create user and save to database
    - [x] logout on user setting page
- [x] CRU- users (sign up & settings page - no deleting required)
    - [x] create new user via signup page
    - [x] load user info in setting page
    - [x] update user info in settings page
- [ ] CRUD Articles
    - [x] Create Article
    - [x] Article Edit Page
    - [x] Article Detail Page
    - [ ] Delete Article Page
- [ ] CR-D Comments on articles (no updating required)
- [ ] GET and display paginated lists of articles
    - [ ] Article List Page
- [ ] Favorite articles
- [ ] Follow other users

- constraints on API Spec not on features list impl latter
    - [ ] Article title must be unique, if update Article title changed, will be published a new
