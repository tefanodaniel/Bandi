package model;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Band extends Client {

    //all of these should be serialized I think.
    private String name;
    private String genre;
    private int size;
    private int capacity;
    private String members;

    private List<String> memberIDs;


    public Band(String id, String name, String genre, int size, int capacity, List<String> members) {
        super(id);
        this.name = name;
        this.genre = genre;
        this.size = size;
        this.capacity = capacity;

        this.memberIDs = members;
        setMemberString();
    }

    public String getMemberString() {
        return this.members;
    }

    private void setMemberString() {
        //   Resulting text should be of form: '{\"id1\",\"id2\"}'
        String memberString = "\'{";
        for (String memberID : memberIDs) {
            memberString += "\\\"" + memberID + "\\\",";
        }
        members = memberString.substring(0,
                memberString.length() - 1) + "}\'";
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

    public void setSize(int size) {
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

    public List<String> getMembers () {
        return memberIDs;
    }

    public void setMembers (Musician member) {
        if (size != capacity) {
            memberIDs.add(member.getId());
        }
        setMemberString();
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
        return name.equals(band.name) && genre.equals(band.genre)
                && (size == band.size) && (capacity== band.capacity)
                && memberIDs.size() == band.memberIDs.size()
                && members.equals(band.members);
    }

    @Override
    public String toString () {

        return "Band {" + "name =' "
                + name
                + '\'' + ", genre = '" + genre
                + '\'' + ", size = '" + size + '\''
                + ", capacity = '" + capacity + '\'' +
                ", members = " + members + '}';
    }
}