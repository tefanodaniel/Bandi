package api;

import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.model_objects.specification.User;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.data.personalization.simplified.GetUsersTopTracksRequest;
import com.wrapper.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;
import model.Musician;
import spark.Route;
import java.net.URI;
import static api.ApiServer.*;

public class SpotifyController {

    // Redirects the window for Spotify login dialog
    public static Route loginWithSpotify = (req, res) -> {
        URI uri_for_code = auth_code_uri_req.execute();
        String uriString = uri_for_code.toString();
        res.redirect(uriString);
        return null;
    };

    // Spotify will redirect to here after successful login
    public static Route callbackFromSpotify = (req, res) -> {

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

        // get user's top tracks
        GetUsersTopTracksRequest trackReq = spotifyApi.getUsersTopTracks()
                .limit(10)
                .offset(0)
                .time_range("medium_term")
                .build();
        Paging<Track> pagingOfTracks = trackReq.execute();
        for (Track track : pagingOfTracks.getItems()) {
            System.out.println(track.getName());
            System.out.println(track.getIsExplicit());
        }

        res.redirect(frontend_url + "/?id=" + id);

        // Create user in database if not already existent
        Musician musician = musicianDao.read(id);
        if (musician == null) { // user has not been added to database yet
            musicianDao.create(id, name);
        }
        return null;
    };
}
