package model;

import java.util.List;

public class Band extends Client {

    //all of these should be serialized I think.
    private String name;
    private String genre;
    private int size;
    private int capacity;
    // should contain List<Musicians>

    public Band(String id, String name, String genre) {
        super(id);
        this.name = name;
        this.genre = genre;
    }

    public Band(String id, String name, String genre, int size, int capacity) {
        super(id);
        this.name = name;
        this.genre = genre;
        this.size = size;
        this.capacity = capacity;
    }
}