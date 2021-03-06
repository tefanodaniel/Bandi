# Bandi 

A social networking application for musicians to meet other musicians, start a band, or look at local music groups in their area.

**Advisors** 

| Name | JHU Email | GitHub Username |
| ---- | --------- | --------------- |
|  Nick Xitco    |     nxitco@jhu.edu      |       NickXitco          |

**Team**

| Name | JHU Email | GitHub Username |
| ---- | --------- | --------------- |
|  Max Torres    |    mtorre28@jhu.edu       |   maxrtorres              |
|  Elizabeth Aguirre |  eaguirr3@jhu.edu     |   elizaguirre      |
| Matthew Ost     | most1@jhu.edu | most21 |
|   Noah Johnson   |  njohns81@jhu.edu         |       nkjohnson16          |
|   Ramachandran Muthukumar   |   rmuthuk1@jhu.edu        |    ramcha24             |
|   Stefano Tusa Lavieri   |    stusa2@jhu.edu       |    stefanotusa             |

## Authorization 
If you are a project member or instructor, please visit [here](https://docs.google.com/document/d/1hD0D15o0scDjNEAtohE44THiiPuuUH_Sy34Ze_uUl9M/edit?usp=sharing)
to get details on how to set environment variables locally for testing. 

## Installing / Getting started

A quick introduction of the minimal setup you need to get the app up & running.

```shell
commands here
```

Here you should say what actually happens when you execute the code above.

## Developing

### Built With
Back-end : [SparkJava](https://sparkjava.com/), [Spotify-Web-Api](https://github.com/thelinmichael/spotify-web-api-java), [Sql2o](https://www.sql2o.org/)

Front-end : [React](https://create-react-app.dev/) 

### Prerequisites
Back-end Development : [Java](https://adoptopenjdk.net/)

Build automation : [Gradle](https://gradle.org/)

Deployment : [Heroku CLI](https://devcenter.heroku.com/articles/heroku-cli)

Front-end Development : [NodeJS](https://nodejs.org/en/download/), [Yarn](https://yarnpkg.com/getting-started/install)

IDE : [IntelliJ](https://www.jetbrains.com/idea/)

## Database
Development Database instance is provided by [elephantsql](https://www.elephantsql.com/). 

Production Database instance is provided by [heroku-postgres](https://www.heroku.com/postgres)

Development Database : [Download Backup : 2021-03-06](https://elephantsql-backups-us-east-1.s3.amazonaws.com/ziggy/jflfxstq.2021-03-06T19%3A46%3A40%2B00%3A00.sql.lzo?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIA2M5CQHIEWQ6JP3W2%2F20210306%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20210306T194733Z&X-Amz-Expires=900&X-Amz-SignedHeaders=host&X-Amz-Signature=30afdd955ea8a82b58a56886624697b0a740416e7f42aaa838aa53bdf683a7a6) 
Production Database : [Download Backup :: 2021-03-06](https://data.heroku.com/datastores/a88e0937-9a2f-4f5a-84ad-2330719b1361#durability)
Explaining what database (and version) has been used. Provide download links.


### Setting up Dev

Here's a brief intro about what a developer must do in order to start developing
the project further:

1. Clone the project : 
```shell
git clone https://github.com/cs421sp21-homework/project-g10.git
cd project-g10/
```

2. Setup Back-end : In IntelliJ, Open a new gradle project 'backend' at 
```shell 
code/backend
``` 

3. Setup Heroku Remote (using information from shared doc): 
```shell
heroku login
git remote add heroku-backend https://heroku:{{current authorization token}}@git.heroku.com/bandiscover-api.git
git remote add heroku-frontend https://heroku:{{current authorization toeken}}@git.heroku.com/bandiscover.git
```

3. Setup Front-end :
```shell
cd code/frontend
yarn install
```

### Building/Developing

- Edit back-end API source code 
- Run the following tasks in order using gradle tool window in IntelliJ
  1. clean
  2. build
  3. jar

- Edit front-end API source code

#### Build Locally 
- Run the back-end api server locally. 
```shell
java -jar code/backend/build/libs/backend-{{version}}.jar 
```

- Run the front-end react app locally. (in a separate terminal window/tab)
```shell 
cd code/frontend
yarn start
```

#### Deployment
- Deploy the back-end api server to heroku app `bandiscover-api`
```shell
git push heroku-backend `git subtree split --prefix code/backend main`:refs/heads/main --force
```
- Deploy the front-end react app to heroku app `bandiscover`
```shell
git push heroku-frontend `git subtree split --prefix code/frontend main`:refs/heads/main --force
```

Alternatively the front-end app is also deployed with every push to github repo. 

<details><summary><span style="color:red">Documentation :To Do</span></summary>
<p>

## Versioning

We can maybe use [SemVer](http://semver.org/) for versioning. 

## Configuration

Here you should write what are all of the configurations a user can enter when using the project.

## Tests

Describe and show how to run the tests with code examples.
Explain what these tests test and why.

```shell
Give an example
```

## Style guide

Explain your code style and show how to check it.

## Api Reference

If the api is external, link to api documentation. If not describe your api including authentication methods as well as explaining all the endpoints with their required parameters.

</p>
</details> 


