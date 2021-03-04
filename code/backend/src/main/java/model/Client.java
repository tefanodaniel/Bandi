package model;

public abstract class Client {
    private static int idGen = 0;

    private int id; // to generate a unique id for each client.

    public Client() {
        this.id = idGen;
        idGen++;
    }
}
