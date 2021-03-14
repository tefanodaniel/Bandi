package model;

public abstract class Client {

    private String id; // to generate a unique id for each client.

    public Client(String idString) {
        this.id = idString;
    }

    public String getId() {
        return this.id;
    }
}
