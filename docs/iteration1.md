# Teamwork
Team Lead - Ramchandran Muthukumar

[Team Availability](https://www.when2meet.com/?11153087-BLSdS)

# OO Design
![](assets/image.png)

# Wireframe & Use-case
<img src="assets/login.PNG" width="600" height="400" />
<img src="assets/create_profile.PNG" width="600" height="400" />
<img src="assets/advanced_search.PNG" width="600" height="400" />

Use Case: Browse on Homepage
1. The user visits our web application using a web browser.
2. The user lands on the homepage and is invited to browse through musicians and bands suggested by our app.
3. Clicking on a user/band icon will redirect them to the chosen profile page.
4. Clicking on "Search by Category" will redirect them to the advanced search page.

Use Case: Signing Up
1. A user will click on the "Log In/Create Profile", and a pop up box will appear.
2. The user can log in to our app using their previously created credentials or through Spotify.
3. The user has the option to create a new account.

Use Case: Creating a Profile
1. Once redirected to the "Creating a Profile" page, a user will enter their information to complete their profile.
2. A user has the option to link their Spotify information such as profile picture, name, and music preferences to their profile.

# Iteration Backlog
- As a user, I would like to be able to use my Spotify account for single sign-on so I can display portions of my listening history on my profile.
- As a user, I want to be able to create a visible profile with my name, instrument, experience level, and musical taste so that I can meet people with similar interests.
- As a band member, I want to be able to create a profile on the app for my band so that we can easily search for additional musicians as a single entity.

# Tasks
- Setup technology stack
- Design UML class diagrams for implementing User Stories
- Design Wire-frames for 
  1. Landing Page
  2. Post-login User dashboard
- Test and Deploy. 

# Retrospective
#### What we have done
1. Object-oriented Analysis and design to support the user-stories listed above (using UML). 
2. Web Application design to support the user-stories listed above (using Wireframe). 
3. Created basic API (without unit-testing) to service CRUD - Create, Read, Update and Delete operations for `Musician`.  
4. Created basic react-app that interfaces with API and contains - landing page, user-login 
5. Deployed both apps using Heroku. 

#### In Progress
Add this to the done list when finished.
- user-profile view. (add to 4.)

#### What we haven't done
1. Basic API to service CRUD operations for `Band`.
2. React app components to provide profile view of `Band`
3. Unit-testing and Documentation of API. 
4. Front-end styling to fully support the wireframes. 
5. Front-end browsing feature for bands and musicians. 

#### Difficulties
1. Interfacing the React app with API backend. 
2. Spotify-authorization. 
3. Deploying both apps simultaneously via Github actions. 

#### Looking forward
1. The team lead should anchor the iteration, create issues in the first two days of the iteration and have a preliminary meeting to ensure all members understand the work ahead sufficiently.  
2. Allocate resources treating each user story as two different but coupled github issues - frontend + backend. 
3. Extra meetings for design session to anticipate implementation issues.


