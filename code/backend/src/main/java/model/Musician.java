package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Musician extends Client {

    private String name;
    private List<String> genres;
    private List<String> instruments;
    private String experience;
    private String location;

    public Musician(String id, String name, List<String> genres) {
        super(id);
        this.name = name;
        this.genres = genres;
    }

    public Musician(String id, String name, List<String> genres,
                    List<String> instruments, String experience, String location) {
        super(id);
        this.name = name;
        this.genres = genres;
        this.instruments = instruments;
        this.experience = experience;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genre) {
        this.genres = genres;
    }

    public void addGenres(String genre) {
        if (this.genres == null) {
            this.genres = new ArrayList<String>();
        }
        this.genres.add(genre);
    }

    public List<String> getInstruments() {
        return instruments;
    }

    public void setInstrument(List<String> instruments) {
        this.instruments = instruments;
    }

    public void addInstrument(String instrument) {
        if (this.instruments == null) {
            this.instruments = new ArrayList<String>();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Musician musician = (Musician) o;
        return name.equals(musician.name) &&
                genres.equals(musician.genres) &&
                Objects.equals(instruments, musician.instruments) &&
                Objects.equals(experience, musician.experience) &&
                Objects.equals(location, musician.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, genres, instruments, experience, location);
    }

    @Override
    public String toString() {
        return "Musician{" +
                "name='" + name + '\'' +
                ", genre='" + genres.toString() + '\'' +
                ", instrument='" + instruments.toString() + '\'' +
                ", experience='" + experience + '\'' +
                ", location=' " + location + '\'' +
                '}';
    }
}
