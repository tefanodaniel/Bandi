package model;

import java.util.List;
import java.util.UUID;

public class Band extends Client {

    //all of these should be serialized I think.
    private String name;
    private String genre;
    private int size;
    private int capacity;
    private List<Musician> members;
    private UUID id;


    public Band(String name, String genre) {
        super();
        this.name = name;
        this.genre = genre;
    }

    public Band(String name, String genre, int size, int capacity, List<Musician> members) {
        super();
        this.name = name;
        this.genre = genre;
        this.size = size;
        this.capacity = capacity;
        this.members = members;
        this.id = new UUID();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getGenre() {
        return genre;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    public List<Musician> getMembers () {
        return members;
    }

    public void setMembers (Musician member) {
        if (size != capacity) {
            members.add(member);
        }
    }

    @Override
    public boolean equals (Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Band band = (Band) o;
        Collections.sort(members);
        Collections.sort(band.members);
        return name.equals(band.name) && genre.equals(band.genre)
                && size.equals(band.size) && capacity.equals(band.capacity)
                && members.size() = band.members.size()
                && members.equals(band.members);
    }

    @Override
    public String toString () {
        return "Band {" + "name =' "
                + name
                + '\'' + ", genre = '" + genre
                + '\'' + ", size = '" + size + '\''
                + ", capacity = '" + capacity + '\'' +
                ", members = '" + members + '\'' + '}';
    }
}