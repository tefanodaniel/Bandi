package model;

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
}
