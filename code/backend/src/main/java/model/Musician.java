package model;

import java.util.*;

public class Musician extends Client {

    private String name;
    private Set<String> genres;
    private Set<String> instruments;
    private String experience;
    private String location;
    private Set<String> profileLinks;
    private Set<String> friends;

    public Musician(String id, String name, Set<String> genres) {
        super(id);
        this.name = name;
        this.genres = genres;
    }

    public Musician(String id, String name, Set<String> genres,
                    Set<String> instruments, String experience, String location, Set<String> profileLinks, Set<String> friends) {
        super(id);
        this.name = name;
        this.genres = genres;
        this.instruments = instruments;
        this.experience = experience;
        this.location = location;
        this.profileLinks = profileLinks;
        this.friends = friends;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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
                Objects.equals(friends, musician.friends);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, genres, instruments, experience, location, friends);
    }

    @Override
    public String toString() {
        return "Musician{" +
                "name='" + name + '\'' +
                ", genre='" + genres.toString() + '\'' +
                ", instrument='" + instruments.toString() + '\'' +
                ", experience='" + experience + '\'' +
                ", location=' " + location + '\'' +
                ", friends=' " + friends + '\'' +
                '}';
    }


}
