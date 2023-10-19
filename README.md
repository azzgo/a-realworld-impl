# GenAI Training Repo

## Thought

By Github Copilot to quick code a [readworld application](https://www.realworld.how/)

## Archtecture & Test Strategy

### Frontend

技术栈：React, React Hook, Pact.js, jotai

- View 层: 主要是页面和封装的 React Component
    - 渲染：通过 jotai 准备 ViewModel，通过 React Component 渲染 View 验证页面交互行为正确
    - 调用: 通过 jotai 准备 ViewModel，通过 React Component 验证 View 的行为触发后, ViewModel 层的方法被正确调用
- ViewModel 层: 使用 jotai 状态库封装的数据结构以及相配套的操作 Controller
    - 层内行为：通过 render hook，触发 controller 的行为后，验证 jotai 的 ViewModel 数据有被正确影响到
    - 应用边界：通过 Pact.js Stub WebServer， 验证触发 Controller 行为后，Jotai 内的 ViewModel 有正确影响到 

### Backend

技术栈: Springboot mvc with kotlin, Pact Provider for jvm, hibernate

- Controller 层：定义 Rest API 相关的 Controller 以及相关的 DTO
    - Stub Model 和其他服务，Stub WebServer, 通过 Pact 测试验证 Controller 有正确定义 API 和参数校验
- Model 层：定义核心领域对象以及相关核心业务逻辑
    - Stub Repository 服务，验证 Model 层的方法调用行为正确
- Repository 层：定义数据库表对应 Mapper 对象以及相应的 Repository 服务
    - Fake database，验证 Repository 调用行为正确

## TODO Feature List

### CRF Features

- [x] A Base Framework for web and server
- [x] A Testing enviroment (include pact test between web and server) setup
- [x] intercept user when not login in authenticated endpoints

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
    - [ ] Article Edit Page
    - [ ] Article Detail Page
    - [ ] Delete Article Page
- [ ] CR-D Comments on articles (no updating required)
- [ ] GET and display paginated lists of articles
    - [ ] Article List Page
- [ ] Favorite articles
- [ ] Follow other users

- constraints on API Spec not on features list impl latter
    - [ ] Article title must be unique, if update Article title changed, will be published a new
