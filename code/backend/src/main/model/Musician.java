public class Musician extends Client {

    private String instrument;
    private String experience;

    public Musician(String name, String genre) {
        super(name, genre);
    }

    public Musician(String name, String genre, String instrument, String experience) {
        super(name, genre);
        this.instrument = instrument;
        this.experience = experience;
    }
}
