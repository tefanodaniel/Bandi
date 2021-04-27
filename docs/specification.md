# Project Requirement Specification

## Problem Statement

Finding musicians to play with can be extraordinarily difficult, especially when trying to match factors such as experience level, instrument coverage, and music taste. In college, there may be music organizations on campus that allow students to participate in the music scene, but these groups often have high barriers to entry and do not offer comprehensive networking opportunities. While online platforms exist to faciliate musical networking in the public sphere, there is no comprehensive solution designed for college students, who have different needs and means than the average adult musician.

## Potential Clients
Potential clients include college-age musicians looking to start a band or join an existing one, 
general users without a profile who want to browse profiles and find local bands, and young
musicians looking to socialize with people with common interests.

## Proposed Solution

Our proposed solution is a web application that allows college students to create profiles that represent their musical interests and what kind of opportunities they are interested in pursuing. This application will allow them to easily find and connect with musicians with similar musical tastes who are also looking for bandmates. Once multiple people have connected, they can create a united profile for their band to make it easier for target musicians to fill their specific areas of need. The application will also facilitate networking events designed to provide musicians opportunities to socialize, identify potential collaborators, and connect with the commnunity.

## Functional Requirements 

**Must-have**

Single User
- As a user, I would like to be able to use my Spotify account for single sign-on so I can display portions of my listening history on my profile.
- As a user, I want to be able to create a visible profile with my name, instrument, experience level, 
  and musical taste so that I can meet people with similar interests.
- As a user, I want to be able to filter profiles based on a set of similar interests and/or geographic proximity to me so that I can connect with those users.
- As a user, I would like to be able to put links to audio/video clips (YouTube, Soundcloud, etc.) on my profiles so I can give prospective collaborators a preview of my playing.
- As a user, I want to be able to chat with other users that I connect with so that we can discuss our tastes, see if we are a good fit, and arrange to meet in person without having to exchange personal contact information.
- As a user, I want to be able to save the profiles of people I meet in the app so that I can connect with them later.
- As a user, I want to be able to browse and register for speed-dating events that are happening in my area so that I can quickly meet a lot of people with similar interests and see if we might be a good match.
- As a user, I want to browse song-of-the-week events by genre and make submissions. 

Band Member
- As a band member, I want to be able to create a profile on the app for my band so that we can easily search for additional musicians as a single entity.
- As a band member, I want to be able to link my and my bandmates' personal profiles to the band profile so that prospective musicians can easily learn about the band and its members.
- As a band member, I would like to be able to put links to audio/video clips (YouTube, Soundcloud, etc.) on our profile so I can give prospective collaborators a preview of our playing.
- As a band member, I want to be able to send an invite to other registered users to join my band profile.


Guest
- As a guest without login credentials, I want to have view-only privileges for the available profiles 
  so that I can see what kind of musicians and groups are around.

Admin
- As an admin, I want to create speed-dating-like events to send participants into different chatrooms (zoom) where they can chat for a short amount of time before moving on to the next chosen match, so that users can have a fun way of meeting a lot of people in a short amount of time.
- As an admin, I want to set the time and date of speed-dating events, as well as the minimum number of users that must register themselves for an event before the virtual chatrooms are opened/created, to ensure that events will have a good user turnout.
- As an admin, I want to create "song-of-the-week" events that chooses a song high in the latest charts. The event has description or information about the song and an option for users to submit youtube/soundcloud links (capped to 1-2 mins)
- As an admin, I want to enable RSVP functionality for the speed-dating events and set visibility to all registered users, so that only registered users can attend the events.

**Nice-to-have**

- As a user, I want to see where other musicians are located on a geographic map of my city or town so that I have a better idea of who's playing what in my area.
- As a band member, I would like to be able to create and customize an application portal for virtual tryouts, so that musicians interested in joining my band can submit recordings/videos of themselves playing their instrument.
- As a user, I want to be able to specifically browse for bands with virtual tryouts so I can easily find them and apply.
- As a student user, I would like to verify my college enrollment with an .edu email so I can filter and find other students on my campus and at nearby schools.
- As a user, I want to submit a "play-along" to another user's 1) uploaded personal audio/video 2) song-of-the-week submissions
- As a user, I want to be able to register with an email/password so that I can still use the app even if I don't have a Spotify account.



## Software Architecture & Technology Stack
We feel that a web-application is best for this design. We will use the client-server model. We want to create a backend API in JAVA using the Sparkjava framework, and Heroku PostgreSQL for a relational database. We may need to use AWS for audio/video file hosting if Heroku doesn't support this.

For our frontend, we will use React to design the web application for a smooth user experience. Optionally, and if we have enough time, we will use Cordova or Electron to extend our web app to a mobile app.

In addition, we will integrate a music streaming API, such as Spotify's, to implement listening history insights on user profiles.
