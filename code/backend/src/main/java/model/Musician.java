package model;

import java.util.Objects;

public class Musician extends Client {

    private String name;
    private String genre;
    private String instrument;
    private String experience;

    public Musician(String name, String genre) {
        super();
        this.name = name;
        this.genre = genre;
    }

    public Musician(String name, String genre, String instrument, String experience) {
        super();
        this.name = name;
        this.genre = genre;
        this.instrument = instrument;
        this.experience = experience;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getInstrument() {
        return instrument;
    }

    public void setInstrument(String instrument) {
        this.instrument = instrument;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Musician musician = (Musician) o;
        return name.equals(musician.name) && genre.equals(musician.genre) && Objects.equals(instrument, musician.instrument) && Objects.equals(experience, musician.experience);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, genre, instrument, experience);
    }

    @Override
    public String toString() {
        return "Musician{" +
                "name='" + name + '\'' +
                ", genre='" + genre + '\'' +
                ", instrument='" + instrument + '\'' +
                ", experience='" + experience + '\'' +
                '}';
    }
}
