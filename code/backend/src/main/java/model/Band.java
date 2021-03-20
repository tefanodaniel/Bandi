package model;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class Band extends Client {

    //all of these should be serialized I think.
    private String name;
    //private int size;
    private int capacity;

    private Set<String> members;
    private Set<String> genres;

    public Band(String name, int capacity, Set<String> genres, Set<String> members) {
        super(UUID.randomUUID().toString());
        this.name = name;
        //this.size = size;
        this.capacity = capacity;

        this.genres = genres;
        this.members = members;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setGenres(Set<String> genres) {
        this.genres = genres;
    }

    public Set<String> getGenres() {
        return this.genres;
    }
/*
    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }*/

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    public Set<String> getMembers () {
        return this.members;
    }

    public void setMembers (Musician member) {
        members.add(member.getId());
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
        return this.getId().equals(band.getId());
    }

    @Override
    public String toString () {
        return "Band{name=\'" + name + "\'}";
    }
}