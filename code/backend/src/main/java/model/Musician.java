package model;

import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;

import java.util.*;

public class Musician extends Client {

    private String name;
    private Set<String> genres;
    private Set<String> instruments;
    private String experience;
    private Set<String> profileLinks;
    private String location;
    private String zipCode;
    private double latitude;
    private double longitude;
    private double distance;
    private Set<String> friends;
    private boolean admin;
    private boolean showtoptracks;
    private Set<String> topTracks;

    public Musician(String id, String name, Set<String> genres) {
        super(id);
        this.name = name;
        this.genres = genres;

        // false by default
        admin = false;
        // true by default
        showtoptracks = true;
        topTracks = new HashSet<>();
    }

    public Musician(String id, String name, Set<String> genres, Set<String> instruments,
                    String experience, Set<String> profileLinks, String location,
                    String zipCode, Set<String> friends, boolean isAdmin) {
        super(id);
        this.name = name;
        this.genres = genres;
        this.instruments = instruments;
        this.experience = experience;
        this.profileLinks = profileLinks;
        this.zipCode = zipCode;
        this.location = location;
        setLatitudeLongitude(zipCode);
        this.distance = 9999;
        this.friends = friends;
        this.admin = isAdmin;
        // true by default
        showtoptracks = true;
        topTracks = new HashSet<>();
    }

    public Musician(String id, String name, Set<String> genres, Set<String> instruments,
                    String experience, Set<String> profileLinks, String location,
                    String zipCode, double distance, Set<String> friends, boolean isAdmin) {
        super(id);
        this.name = name;
        this.genres = genres;
        this.instruments = instruments;
        this.experience = experience;
        this.profileLinks = profileLinks;
        this.zipCode = zipCode;
        this.location = location;
        setLatitudeLongitude(zipCode);
        this.distance = distance;
        this.friends = friends;
        this.admin = isAdmin;
        // true by default
        showtoptracks = true;
        topTracks = new HashSet<>();
    }

    private void setLatitudeLongitude(String zipCode) {
        /* TODO: This API no longer exists... Need to find replacement */
        /* if(zipCode == null || zipCode.equals("NULL")) {
            this.latitude = 0;
            this.longitude = 0;
        } else {
            final String BASE_URL = "https://public.opendatasoft.com/api/records/1.0/search/?dataset=us-zip-code-latitude-and-longitude";
            final String QUERY_PARAMS = "&facet=state&facet=timezone&facet=dst";
            final String ZIP_CODE = "&q=" + zipCode;
            String endpoint = BASE_URL + ZIP_CODE + QUERY_PARAMS;
            JSONObject fields = Unirest.get(endpoint).asJson().getBody().getObject()
                    .getJSONArray("records")
                    .getJSONObject(0)
                    .getJSONObject("fields");
            this.latitude = fields.getDouble("latitude");
            this.longitude = fields.getDouble("longitude");
        } */
        this.latitude = 0.0;
        this.longitude = 0.0;
    }

    public Set<String> getTopTracks() {
        return topTracks;
    }

    public void setTopTracks(Set<String> tracks) {
        this.topTracks = tracks;
    }

    public void addTopTrack(String track) {
        this.topTracks.add(track);
    }

    public boolean getShowtoptracks() {
        return showtoptracks;
    }

    public void setShowtoptracks(boolean show) {
        this.showtoptracks = show;
    }


    public boolean getAdmin() {
        return this.admin;
    }

    public void setAdmin(boolean isAdmin) {
        this.admin = isAdmin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genre) {
        this.genres = genres;
    }

    public void addGenre(String genre) {
        if (this.genres == null) {
            this.genres = new HashSet<String>();
        }
        this.genres.add(genre);
    }


    public Set<String> getInstruments() {
        return instruments;
    }

    public void setInstruments(Set<String> instruments) {
        this.instruments = instruments;
    }

    public void addInstrument(String instrument) {
        if (this.instruments == null) {
            this.instruments = new HashSet<String>();
        }
        this.instruments.add(instrument);
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public Set<String> getProfileLinks() {
        return profileLinks;
    }

    public void setProfileLinks(Set<String> links) {
        this.profileLinks = links;
    }

    public void addProfileLink(String link) {
        if (this.profileLinks == null) {
            this.profileLinks = new HashSet<String>();
        }
        this.profileLinks.add(link);
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getDistance() {
        return distance;
    }

    public Set<String> getFriends() { return friends; }

    public void setFriends(Set<String> friends) { this.friends = friends; }

    public void addFriend(String friendID) {
        if (this.friends == null) {
            this.friends = new HashSet<String>();
        }
        this.friends.add(friendID);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Musician musician = (Musician) o;
        return name.equals(musician.name) &&
                genres.equals(musician.genres) &&
                Objects.equals(instruments, musician.instruments) &&
                Objects.equals(experience, musician.experience) &&
                Objects.equals(location, musician.location) &&
                Objects.equals(friends, musician.friends) &&
                this.admin == musician.admin;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, genres, instruments, experience, location, zipCode, friends, admin);
    }

    @Override
    public String toString() {
        // TODO: update with new fields
        return "Musician{" +
                "name='" + name + '\'' +
                ", genre='" + genres.toString() + '\'' +
                ", instrument='" + instruments.toString() + '\'' +
                ", experience='" + experience + '\'' +
                ", location=' " + location + '\'' +
                ", distance=' " + distance + '\'' +
                ", friends=' " + friends + '\'' +
                ", admin=' " + admin + '\'' +
                '}';
    }


}
