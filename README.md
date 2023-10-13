# GenAI Training Repo

## Thought

By Github Copilot to quick code a [readworld application](https://www.realworld.how/)

## TODO Feature List

- [x] A Base Framework for web and server
- [ ] A Testing enviroment (include pact test between web and server) setup
- [ ] Feature list from [readworld spec]
  - [ ] Authenticate users via JWT (login/signup pages + logout button on settings page)
    - [x] Fake API login API, setup Login Page, when login load User Model to global store and set jwt token to localStorage
    - [ ] Stub Provider Server, when login request comes, server should return jwt token and user info
    - [ ] Fake signup API, setup Signup API, when sign up, load User Model to global store
    - [ ] Fake User model, setup settings page, when click logout button, clear User Model and redirect to login Page
  - [ ] CRU- users (sign up & settings page - no deleting required)
  - [ ] CRUD Articles
  - [ ] CR-D Comments on articles (no updating required)
  - [ ] GET and display paginated lists of articles
  - [ ] Favorite articles
  - [ ] Follow other users
