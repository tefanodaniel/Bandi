package api;

import dao.*;
import exceptions.ApiError;
import exceptions.DaoException;
import model.*;
import spark.QueryParamsMap;
import spark.Spark;
import util.Database;
import com.google.gson.JsonSyntaxException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import static spark.Spark.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import dao.MusicianDao;
import org.sql2o.Sql2o;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.model_objects.specification.User;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import com.wrapper.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;

public class ApiServer {

    // flag for testing locally vs. deploying
    private static final boolean isLocalTest =
            Boolean.parseBoolean(System.getenv("isLocal"));

    // client id for Spotify
    private static final String client_id= "ae87181e126a4fd9ac434b67cf6f6f14";
    // Client Secret for using Spotify API (should never push to VCS)
    private static final String client_secret = System.getenv("client_secret");

    private static int getHerokuAssignedPort() {
        // Heroku stores port number as an environment variable
        String herokuPort = System.getenv("PORT");
        if (herokuPort != null) {
            return Integer.parseInt(herokuPort);
        }
        //return default port if heroku-port isn't set (i.e. on localhost)
        return 4567;
    }

    public static void main(String[] args) throws URISyntaxException {
        int myPort = getHerokuAssignedPort();
        port(myPort);
        staticFiles.location("/public");

        // Dao objects
        MusicianDao musicianDao = getMusicianDao();
        BandDao bandDao = getBandDao();
        Sql2oSpeedDateEventDao speedDateEventDao = getSpeedDateEventDao();
        RequestDao requestDao = getRequestDao();
        SongDao songDao = getSongDao();
        SotwSubmissionDao sotw_submissionDao = getSubmissionDao();
        SotwEventDao sotw_eventDao = getEventDao();

        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        exception(ApiError.class, (ex, req, res) -> {
            // Handle the exception here
            Map<String, String> map = Map.of("status", ex.getStatus() + "",
                    "error", ex.getMessage());
            res.body(gson.toJson(map));
            res.status(ex.getStatus());
            res.type("application/json");
        });

        // Set frontend and backend urls
        String frontend_url;
        String backend_url;
        if (isLocalTest) {
            frontend_url = "http://localhost:3000";
            backend_url = "http://localhost:4567";
        }
        else {
            frontend_url = "http://bandiscover.herokuapp.com";
            backend_url = "http://bandiscover-api.herokuapp.com";
        }

        // Set the redirect_uri from Spotify dialog
        final URI redirect_uri =
                SpotifyHttpManager.makeUri(backend_url + "/callback");

        // Spotify API variable
        final SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setClientId(client_id)
                .setClientSecret(client_secret)
                .setRedirectUri(redirect_uri)
                .build();

        // authorization code uri request
        final AuthorizationCodeUriRequest auth_code_uri_req =
                spotifyApi.authorizationCodeUri()
                        .scope("user-read-email,user-read-private,user-top-read")
                        .show_dialog(true)
                        .build();

        // Redirects to Spotify login
        get("/login", (req, res) -> {
            URI uri_for_code = auth_code_uri_req.execute();
            String uriString = uri_for_code.toString();
            res.redirect(uriString);
            return null;
            //return new JSONObject("{\"link\": \""+uriString+"\"}");
        });

        // Gets user info following login
        get("/callback", (req, res) -> {

            String error = req.queryParams("error");
            if (error != null) { // SSO was canceled by user
                res.redirect(frontend_url);
                return null;
            }

            // Use authorization code to get access token and refresh token
            String code = req.queryParams("code");
            AuthorizationCodeRequest auth_code_req =
                    spotifyApi.authorizationCode(code).build();
            AuthorizationCodeCredentials auth_code_credentials =
                    auth_code_req.execute();

            // Set tokens in Spotify API Object
            spotifyApi.setAccessToken(auth_code_credentials.getAccessToken());
            spotifyApi.setRefreshToken(auth_code_credentials.getRefreshToken());

            // get current user's info
            final GetCurrentUsersProfileRequest getCurrentUser =
                    spotifyApi.getCurrentUsersProfile()
                    .build();
            final User user = getCurrentUser.execute();
            String name = user.getDisplayName();
            String id = user.getId();

            res.redirect(frontend_url + "/?id=" + id);

            // Create user in database if not already existent
            Musician musician = musicianDao.read(id);
            if (musician == null) { // user has not been added to database yet
                musicianDao.create(id, name);
            }

            return null;
            //return new JSONObject("{\"id\": \"" + id + "\"}");
        });

        // Get musicians given the id
        get("/musicians/:id", (req, res) -> {
            try {
                String id = req.params("id");
                Musician musician = musicianDao.read(id);
                if (musician == null) {
                    throw new ApiError("Resource not found", 404); // Bad request
                }
                res.type("application/json");
                return gson.toJson(musician);
            } catch (DaoException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });

        // Get all musicians (with optional query parameters)
        get("/musicians", (req, res) -> {
            try {
                List<Musician> musicians;
                Map<String, String[]> query = req.queryMap().toMap();
                if(query.size() > 0) {
                    musicians = musicianDao.readAll(query);
                }
                else {
                    musicians = musicianDao.readAll();
                }
                return gson.toJson(musicians);
            } catch (DaoException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });

        // post musicians
        post("/musicians", (req, res) -> {
            try {
                Musician musician = gson.fromJson(req.body(), Musician.class);
                Set<String> instruments = musician.getInstruments();
                Set<String> genres = musician.getGenres();
                String experience = musician.getExperience();
                String location = musician.getLocation();
                String zipCode = musician.getZipCode();
                Set<String> profileLinks = musician.getProfileLinks();
                Set<String> friends = musician.getFriends();
                boolean admin = musician.getAdmin();

                if (instruments == null) { instruments = new HashSet<String>(); }
                if (genres == null) { genres = new HashSet<String>(); }
                if (experience == null) { experience = "NULL"; }
                if (location == null) { location = "NULL"; }
                if (zipCode == null) { zipCode = "NULL"; }
                if (profileLinks == null) { profileLinks = new HashSet<String>(); }
                if (friends == null) { friends = new HashSet<String>(); }
                musicianDao.create(musician.getId(), musician.getName(), genres,
                        instruments, experience, location, zipCode, profileLinks, friends, admin);
                res.status(201);
                return gson.toJson(musician);
            } catch (DaoException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });

        // put musicians
        put("/musicians/:id", (req, res) -> {

            try {

                String id = req.params("id");
                Musician musician = gson.fromJson(req.body(), Musician.class);
                if (musician == null) {
                    throw new ApiError("Resource not found", 404);
                }

                if (! (musician.getId()).equals(id)) {
                    throw new ApiError("musician ID does not match the resource identifier", 400);
                }

                String name = musician.getName();
                Set<String> genres = musician.getGenres();
                Set<String> instruments = musician.getInstruments();
                String experience = musician.getExperience();
                String location = musician.getLocation();
                String zipCode = musician.getZipCode();
                Set<String> profileLinks = musician.getProfileLinks();
                // TODO: add updateFriends method
                Set<String> friends = musician.getFriends();
                // no check for admin flag. We don't want to change admin on and off,
                // and since ints default to 0, we might accidentally take admin
                // permissions away.

                // Update specific fields:
                boolean flag = false;
                if (name != null) {
                    flag = true;
                    musician = musicianDao.updateName(id, name);
                } if (instruments != null) {
                    flag = true;
                    musician = musicianDao.updateInstruments(id, instruments);
                } if (genres != null) {
                    flag = true;
                    musician = musicianDao.updateGenres(id, genres);
                } if (experience != null) {
                    flag = true;
                    musician = musicianDao.updateExperience(id, experience);
                } if (location != null) {
                    flag = true;
                    musician = musicianDao.updateLocation(id, location);
                } if (zipCode != null) {
                    flag = true;
                    musician = musicianDao.updateZipCode(id, zipCode);
                } if (profileLinks != null) {
                    flag = true;
                    musician = musicianDao.updateProfileLinks(id, profileLinks);
                } if (!flag) {
                    throw new ApiError("Nothing to update", 400);
                } if (musician == null) {
                    throw new ApiError("Resource not found", 404);
                }

                return gson.toJson(musician);
            } catch (DaoException | JsonSyntaxException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });

        // delete musicians
        delete("/musicians/:id", (req, res) -> {
            try {
                String id = req.params("id");
                Musician musician = musicianDao.delete(id);
                if (musician == null) {
                    throw new ApiError("Resource not found", 404); // Bad request
                }
                res.type("application/json");
                return gson.toJson(musician);
            } catch (DaoException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });

        // get all of user's pending friend requests
        get("/friend/:senderid", (req, res) -> {
            try {
                String senderID = req.params("senderid");
                List<FriendRequest> requests = requestDao.readAllFrom(senderID);
                res.type("application/json");
                return gson.toJson(requests);
            } catch (DaoException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });

        // send friend request
        post("/friend/:senderid/:recipientid", (req, res) -> {
            try {
                String senderID = req.params("senderid");
                String recipientID = req.params("recipientid");
                FriendRequest fr = requestDao.createRequest(senderID, recipientID);
                if (fr == null) {
                    throw new ApiError("Resource not found", 404); // Bad request
                }
                res.type("application/json");
                return gson.toJson(fr);
            } catch (DaoException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });

        // respond (accept or decline) friend request
        delete("friend/:senderid/:recipientid/:action", (req, res) -> {
            try {
                String senderID = req.params("senderid");
                String recipientID = req.params("recipientid");
                String action = req.params("action");

                FriendRequest fr = null;

                if (action.equals("accept"))  {
                    fr = requestDao.acceptRequest(senderID, recipientID);
                } else if (action.equals("decline")) {
                    fr = requestDao.declineRequest(senderID, recipientID);
                } else {
                    throw new ApiError("Invalid action to perform on request", 505);
                }

                if (fr == null) {
                    throw new ApiError("Resource not found", 404); // Bad request
                }
                res.type("application/json");
                return gson.toJson(fr);
            } catch (DaoException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });

        // get the admin status of a musician
        get("/adminstatus/:id", (req, res) -> {
                    try {
                        String id = req.params("id");
                        Musician musician = musicianDao.read(id);
                        if (musician == null) {
                            throw new ApiError("Resource not found", 404); // Bad request
                        }
                        boolean isAdmin = musician.getAdmin();
                        res.type("application/json");
                        return "{\"isAdmin\":"+ isAdmin +"}";
                    } catch (DaoException ex) {
                        throw new ApiError(ex.getMessage(), 500);
                    }
                }
        );

        // update the admin status of a musician
        put("/adminstatus/:id", (req, res) -> {
                    try {
                        String id = req.params("id");
                        Map map = gson.fromJson(req.body(),HashMap.class);
                        boolean isAdmin = (boolean) map.get("isAdmin");
                        Musician m = musicianDao.updateAdmin(id, isAdmin);
                        res.type("application/json");
                        return gson.toJson(m);
                    } catch (DaoException ex) {
                        throw new ApiError(ex.getMessage(), 500);
                    }
                }
        );

        // Get all bands (optional query parameters)
        // if searching for id, only pass 1 parameter
        get("/bands", (req, res) -> {
            try {
                List<Band> bands;
                Map<String, String[]> query = req.queryMap().toMap();

                if (query.get("musicianId") != null) {
                    bands = bandDao.readAll(query.get("musicianId")[0]);
                } else if (query.size() > 0) {
                    bands = bandDao.readAll(query);
                } else {
                    bands = bandDao.readAll();
                }
                return gson.toJson(bands);
            } catch (DaoException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });

        // Get band given the id
        get("/bands/:id", (req, res) -> {
            try {
                String id = req.params("id");
                Band band = bandDao.read(id);
                if (band == null) {
                    throw new ApiError("Resource not found", 404); // Bad request
                }
                res.type("application/json");
                return gson.toJson(band);
            } catch (DaoException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });

        // post band
        post("/bands", (req, res) -> {
            try {
                Band band = gson.fromJson(req.body(), Band.class);

                String id = UUID.randomUUID().toString();
                Set<String> members = band.getMembers();
                Set<String> genres = band.getGenres();
                String name = band.getName();
                int capacity = band.getCapacity();

                if (members == null) { members = new HashSet<String>(); }
                if (genres == null) { genres = new HashSet<String>(); }
                if (name == null) { name = "NULL"; }
                if (capacity == 0) { capacity = 1; }

                bandDao.create(id, name, capacity, genres, members);

                res.status(201);
                return gson.toJson(band);
            } catch (DaoException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });

        // put new band members
        put("/bands/:bid/:mid", (req, res) -> {
            try {
                String bandId = req.params("bid");
                String musicianId = req.params("mid");
                Band band = bandDao.read(bandId);
                Musician musician = musicianDao.read(musicianId);
                if (band == null) {
                    throw new ApiError("Resource not found", 404);
                }
                if (musician == null) {
                    throw new ApiError("Resource not found", 404);
                }
                if (!(musician.getId()).equals(musicianId)) {
                    throw new ApiError("musician ID does not match the resource identifier", 400);
                }
                if (!(band.getId()).equals(bandId)) {
                    throw new ApiError("band ID does not match the resource identifier", 400);
                }

                Band b = bandDao.add(bandId, musicianId);
                if (b == null) {
                    throw new ApiError("Resource not found", 404);
                }

                return gson.toJson(b);
            } catch (DaoException | JsonSyntaxException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });

        // remove band member from band
        delete("/bands/:bid/:mid", (req, res) -> {
            try {
                String bandId = req.params("bid");
                String musicianId = req.params("mid");
                Band band = bandDao.remove(bandId, musicianId);
                if (band == null) {
                    throw new ApiError("Resource not found", 404);
                }

                res.type("application/json");
                return gson.toJson(band);
            } catch (DaoException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });

        // Get all SpeedDateEvents
        get("/speeddateevents", (req, res) -> {
            try {
                List<SpeedDateEvent> events;
                Map<String, String[]> query = req.queryMap().toMap();
                events = speedDateEventDao.readAll();
                return gson.toJson(events);
            } catch (DaoException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });

        // Get SpeedDateEvent given the id
        get("/speeddateevents/:id", (req, res) -> {
            try {
                String id = req.params("id");
                SpeedDateEvent event = speedDateEventDao.read(id);
                if (event == null) {
                    throw new ApiError("Resource not found", 404); // Bad request
                }
                res.type("application/json");
                return gson.toJson(event);
            } catch (DaoException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });

        // put new SpeedDateEvent participant
        put("/speeddateevents/:eid/:mid", (req, res) -> {
            try {
                String eventId = req.params("eid");
                String musicianId = req.params("mid");
                SpeedDateEvent event = speedDateEventDao.read(eventId);
                Musician musician = musicianDao.read(musicianId);
                if (event == null || musician == null) {
                    throw new ApiError("Resource not found", 404);
                }

                SpeedDateEvent e = speedDateEventDao.add(eventId, musicianId);
                if (e == null) {
                    throw new ApiError("Failed to add participant", 404);
                }

                return gson.toJson(e);
            } catch (DaoException | JsonSyntaxException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });

        // remove SpeedDateEvent participant from event
        delete("/speeddateevents/:eid/:mid", (req, res) -> {
            try {
                String eventId = req.params("eid");
                String musicianId = req.params("mid");
                SpeedDateEvent event = speedDateEventDao.remove(eventId, musicianId);
                if (event == null) {
                    throw new ApiError("Resource not found", 404);
                }

                res.type("application/json");
                return gson.toJson(event);
            } catch (DaoException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });

        // post a SpeedDateEvent
        post("/speeddateevents", (req, res) -> {
            try {
                SpeedDateEvent event = gson.fromJson(req.body(), SpeedDateEvent.class);

                String id = UUID.randomUUID().toString();
                String name = event.getName();
                String link = event.getLink();
                String date = event.getDate();
                int minusers = event.getMinusers();
                Set<String> participants = event.getParticipants();

                if (name == null) { name = "NULL"; }
                if (link == null) { link = "NULL"; }
                if (date == null) { date = "NULL"; }
                if (minusers == 0) { minusers = 1; }
                if (participants == null) {participants = new HashSet<>();}

                speedDateEventDao.create(id, name, link, date, minusers, participants);

                res.status(201);
                return gson.toJson(event);
            } catch (DaoException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });

        // sent friend request from user to user

        // Song Api Routes
        // get (read) all songs
        get("/songs", (req, res) -> {
            try {
                List<Song> songs = songDao.readAll();
                return gson.toJson(songs);
            } catch (DaoException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });


        // get (read) a song given songId
        get("/songs/:songId", (req, res) -> {
            try {
                String songId = req.params("songId");
                Song s = songDao.read(songId);
                if (s == null) {
                    throw new ApiError("Resource not found", 404); // Bad request
                }
                res.type("application/json");
                return gson.toJson(s);
            } catch (DaoException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });

        // post (create) a song
        post("/songs", (req, res) -> {
            try {
                Song song = gson.fromJson(req.body(), Song.class);
                Set<String> genres = song.getGenres();

                if (genres == null) { genres = new HashSet<String>(); }
                songDao.create(song.getSongId(), song.getSongName(), song.getArtistName(), song.getAlbumName(), song.getReleaseYear(), genres);
                res.status(201);
                return gson.toJson(song);
            } catch (DaoException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });

        // put (updated) a song with info
        put("/songs/:songId", (req, res) -> {

            try {

                String songId = req.params("songId");
                Song song = gson.fromJson(req.body(), Song.class);
                if (song == null) {
                    throw new ApiError("Resource not found", 404);
                }

                if (! (song.getSongId().equals(songId))) {
                    throw new ApiError("song ID does not match the resource identifier", 400);
                }

                String songName = song.getSongName();
                String artistName = song.getArtistName();
                String albumName = song.getAlbumName();
                Integer releaseYear = song.getReleaseYear();
                Set<String> genres = song.getGenres();

                // Update specific fields:
                boolean flag = false;
                if (songName != null) {
                    flag = true;
                    song = songDao.updateSongName(songId, songName);
                } if (artistName != null) {
                    flag = true;
                    song = songDao.updateArtistName(songId, songName);
                } if (albumName != null) {
                    flag = true;
                    song = songDao.updateAlbumName(songId, albumName);
                } if (releaseYear != 0) {
                    flag = true;
                    song = songDao.updateReleaseYear(songId, releaseYear);
                } if (genres != null) {
                    flag = true;
                    song = songDao.updateGenres(songId, genres);
                } if (!flag) {
                    throw new ApiError("Nothing to update", 400);
                } if (song == null) {
                    throw new ApiError("Resource not found", 404);
                }

                return gson.toJson(song);
            } catch (DaoException | JsonSyntaxException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });

        // delete (remove) a song
        delete("/songs/:songId", (req, res) -> {
            try {
                String songId = req.params("songId");
                Song song = songDao.deleteSong(songId);
                if (song == null) {
                    throw new ApiError("Resource not found", 404); // Bad request
                }
                res.type("application/json");
                return gson.toJson(song);
            } catch (DaoException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });

        // Song of the Week Submission Api Routes
        // get (read) all submissions
        get("/submissions", (req, res) -> {
            try {
                List<SongOfTheWeekSubmission> submissions = sotw_submissionDao.readAll();
                return gson.toJson(submissions);
            } catch (DaoException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });

        // get (read) a submission given submissionId
        get("/submissions/:submissionId", (req, res) -> {
            try {
                String submissionId = req.params("submissionId");
                SongOfTheWeekSubmission s = sotw_submissionDao.read(submissionId);
                if (s == null) {
                    throw new ApiError("Resource not found", 404); // Bad request
                }
                res.type("application/json");
                return gson.toJson(s);
            } catch (DaoException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });

        // post (create) a submission from a user with musician_id
        post("/submissions", (req, res) -> {
            try {
                SongOfTheWeekSubmission submission = gson.fromJson(req.body(), SongOfTheWeekSubmission.class);
                Set<String> instruments = submission.getInstruments();

                if (instruments == null) { instruments = new HashSet<String>(); }
                sotw_submissionDao.create(submission.getSubmission_id(), submission.getMusician_id(), submission.getAVSubmission(), instruments);
                res.status(201);
                return gson.toJson(submission);
            } catch (DaoException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });

        // put (update) a submission with info
        put("/submissions/:submissionId", (req, res) -> {
            try {

                String submissionId = req.params("submissionId");
                SongOfTheWeekSubmission submission = gson.fromJson(req.body(), SongOfTheWeekSubmission.class);
                if (submission == null) {
                    throw new ApiError("Resource not found", 404);
                }

                if (! (submission.getSubmission_id().equals(submissionId))) {
                    throw new ApiError("submission ID does not match the resource identifier", 400);
                }

                String avSubmission = submission.getAVSubmission();
                Set<String> instruments = submission.getInstruments();

                // Update specific fields:
                boolean flag = false;
                if (avSubmission != null) {
                    flag = true;
                    submission = sotw_submissionDao.updateAVSubmission(submissionId, avSubmission);
                } if (instruments != null) {
                    flag = true;
                    submission = sotw_submissionDao.updateInstruments(submissionId, instruments);
                } if (!flag) {
                    throw new ApiError("Nothing to update", 400);
                } if (submission == null) {
                    throw new ApiError("Resource not found", 404);
                }

                return gson.toJson(submission);
            } catch (DaoException | JsonSyntaxException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });

        // delete (remove) a submission
        delete("/submissions/:submissionId", (req, res) -> {
            try {
                String submissionId = req.params("submissionId");
                SongOfTheWeekSubmission submission = sotw_submissionDao.delete(submissionId);
                if (submission == null) {
                    throw new ApiError("Resource not found", 404); // Bad request
                }
                res.type("application/json");
                return gson.toJson(submission);
            } catch (DaoException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });

        // Song of the Week Event Api Routes
        // get (read) all sotw events
        get("/events", (req, res) -> {
            try {
                List<SongOfTheWeekEvent> events = sotw_eventDao.readAll();
                return gson.toJson(events);
            } catch (DaoException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });

        // get (read) a sotw event given event id
        get("/events/:eventId", (req, res) -> {
            try {
                String eventId = req.params("eventId");
                SongOfTheWeekEvent s = sotw_eventDao.read(eventId);
                if (s == null) {
                    throw new ApiError("Resource not found", 404); // Bad request
                }
                res.type("application/json");
                return gson.toJson(s);
            } catch (DaoException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });

        // post (create) a sotw event
        post("/events", (req, res) -> {
            try {
                SongOfTheWeekEvent event = gson.fromJson(req.body(), SongOfTheWeekEvent.class);
                Set<String> submissions = event.getSubmissions();

                if (submissions == null) { submissions = new HashSet<String>(); }
                sotw_eventDao.create(event.getEventId(), event.getAdminId(), event.getStart_week(), event.getEnd_week(), event.getSongId(), submissions);
                res.status(201);
                return gson.toJson(event);
            } catch (DaoException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });

        // put (update) an sotw event
        put("/events/:eventId", (req, res) -> {
            // doesn't include adding or removing submissions
            try {

                String eventId = req.params("eventId");
                SongOfTheWeekEvent event = gson.fromJson(req.body(), SongOfTheWeekEvent.class);
                if (event == null) {
                    throw new ApiError("Resource not found", 404);
                }

                if (! (event.getEventId().equals(eventId))) {
                    throw new ApiError("event ID does not match the resource identifier", 400);
                }

                String start_week = event.getStart_week();
                String end_week = event.getEnd_week();
                String songId = event.getSongId();

                // Update specific fields:
                boolean flag = false;
                if (start_week != null) {
                    flag = true;
                    event = sotw_eventDao.updateStartWeek(eventId, start_week);
                } if (end_week != null) {
                    flag = true;
                    event = sotw_eventDao.updateEndWeek(eventId, end_week);
                } if (songId != null) {
                    flag = true;
                    event = sotw_eventDao.updateSong(eventId, songId);
                } if (!flag) {
                    throw new ApiError("Nothing to update", 400);
                } if (event == null) {
                    throw new ApiError("Resource not found", 404);
                }

                return gson.toJson(event);
            } catch (DaoException | JsonSyntaxException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });


        // get (read All) submissions given sotw event id

        // put (add) a submission to an sotw events

        // delete (remove) a submission from an sotw event



        // delete a sotw event
        delete("/events/:eventId", (req, res) -> {
            try {
                String eventId = req.params("eventId");
                SongOfTheWeekEvent event = sotw_eventDao.deleteEvent(eventId);
                if (event == null) {
                    throw new ApiError("Resource not found", 404); // Bad request
                }
                res.type("application/json");
                return gson.toJson(event);
            } catch (DaoException ex) {
                throw new ApiError(ex.getMessage(), 500);
            }
        });

        // options request to allow for CORS
        options("/*", (req, res) -> {
            String headers = req.headers("Access-Control-Request-Headers");
            if (headers != null) {
                res.header("Access-Control-Allow-Headers", headers);
            }
            String method = req.headers("Access-Control-Request-Method");
            if (method != null) {
                res.header("Access-Control-Allow-Methods", method);
            }
            return "OK";
        });

        // CORS
        before((req, res) -> {
            res.header("Access-Control-Allow-Origin", "*");
            res.header("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,OPTIONS");
            res.header("Access-Control-Request-Method", "*");
            res.header("Access-Control-Allow-Headers", "*");
            res.type("application/json");
        });
    }

    private static MusicianDao getMusicianDao() throws URISyntaxException{
        Sql2o sql2o = Database.getSql2o();
        //List<Musician> musicians = DataStore.sampleMusicians();
        //Database.createMusicianTablesWithSampleData(sql2o, musicians);
        return new Sql2oMusicianDao(sql2o);
    }

    private static BandDao getBandDao() throws URISyntaxException{
        Sql2o sql2o = Database.getSql2o();
        return new Sql2oBandDao(sql2o);
    }


    private static Sql2oSpeedDateEventDao getSpeedDateEventDao() throws URISyntaxException {
        Sql2o sql2o = Database.getSql2o();
        return new Sql2oSpeedDateEventDao(sql2o);
    }

    private static RequestDao getRequestDao() throws URISyntaxException{
        Sql2o sql2o = Database.getSql2o();
        return new Sql2oRequestDao(sql2o);
    }

    private static SongDao getSongDao() throws URISyntaxException{
        Sql2o sql2o = Database.getSql2o();
        return new Sql2oSongDao(sql2o);
    }

    private static SotwEventDao getEventDao() throws URISyntaxException{
        Sql2o sql2o = Database.getSql2o();
        return new Sql2oSotwEventDao(sql2o);
    }

    private static SotwSubmissionDao getSubmissionDao() throws URISyntaxException{
        Sql2o sql2o = Database.getSql2o();
        return new Sql2oSotwSubmissionDao(sql2o);
    }

    /**
     * Stop the server.
     */
    public static void stop() {
        Spark.stop();
    }
}
