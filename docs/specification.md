# Project Requirement Specification

## Problem Statement

Finding musicians to play with can be extraordinarily difficult, especially when trying to match factors such as experience level, instrument coverage, and music taste. In college, there may be music organizations on campus that allow students to participate in the music scene, but these groups often have high barriers to entry and do not offer comprehensive networking opportunities. While online platforms exist to faciliate musical networking in the public sphere, there is no comprehensive solution designed for college students, who have different needs and means than the average adult musician.

## Potential Clients
Potential clients include college-age musicians looking to start a band or join an existing one, 
general users without a profile who just want to browse profiles and events, and young
musicians looking to socialize with people with common interests.

## Proposed Solution

Our proposed solution is a web application that allows college students to create profiles that represent their musical 
interests and what kind of opportunities they are interested in pursuing. This application will allow them to easily find
and connect with musicians with similar musical tastes who are also looking for bandmates. Once multiple people have 
connected, they can create a united profile for their band to make it easier for target musicians to fill their specific 
areas of need. The application will also facilitate networking events designed to provide musicians opportunities to 
socialize, identify potential collaborators, and connect with the commnunity.

## Functional Requirements 

**Must-have**
- As a user, I want to be able to create a visible profile with my name, instrument, experience level, 
  and musical taste so that I can meet people with similar interests.
- As a user, I want to be able to manually search for other profiles with similar interests and geographic location 
  to me so that I can connect with them.
- As a band member, I want to be able to create a profile on the app for my band so that we can easily search for 
  additional musicians as a single entity.
- As a band member, I want to be able to link my personal profile to the band profile so that prospective musicians 
  can easily learn about the band and its members.
- As a guest without login credentials, I want to have view-only privileges for the available profiles 
  so that I can see what kind of musicians and groups are around.
- As a user, I want to be able to save the profiles of people I meet in the app so I can connect with them later.

**Nice-to-have**
- As a user and as a band, I would like to be able to put audio/video clips on my profiles so I can give prospective 
  collaborators a preview of my playing.
- As a band member, I would like to be able to create and customize an application portal for virtual tryouts, so that 
  musicians interested in joining my band can submit recordings/videos of themselves playing their instrument.
- As a user, I want to be able to specifically browse for bands with virtual tryouts so I can easily find them and apply.
- As a user, I would like to be able to sign in with my Spotify account to display portions of my listening history on
  my profile.
- As a student user, I would like to verify my college enrollment with an .edu email so I can filter and find other 
  students on my campus.
- As a user, I want to be able to chat with other users that I connect with so that we can discuss our tastes,
  see if we are a good fit, and arrange to meet in person without having to exchange personal contact information.




## Software Architecture & Technology Stack
We feel that a web-application is best for this design. We will use the
client-server model. We want to create a backend in {TODO: Java/Python}, and Heroku
PostgreSQL for a relational database.

TODO: Heroku for production db, but what about a dev db? 