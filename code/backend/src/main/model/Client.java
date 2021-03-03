public class Client {

    private static int idGen = 0;

    private int id;
    private String name;
    private String genre;

    public Client(String name, String genre) {
        this.id = idGen;
        idGen++;
        this.name = name;
        this.genre = genre;
    }
}
