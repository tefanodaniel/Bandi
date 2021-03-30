package model;

import java.util.HashSet;
import java.util.Set;

public class Event {

    private String id;
    private String name;
    private String link;
    private String date;
    private int minusers;
    private Set<String> participants;

    public Event(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Event(String id, String name, String link,
                 String date, int minusers, Set<String> participants) {
        this.id = id;
        this.name = name;
        this.link = link;
        this.date = date;
        this.minusers = minusers;
        this.participants = participants;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return this.link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getMinusers() {
        return this.minusers;
    }

    public void setMinusers(int minusers) {
        this.minusers = minusers;
    }

    public Set<String> getParticipants() {
        return this.participants;
    }

    public void setParticipants(Set<String> participants) {
        this.participants = participants;
    }

    public void addParticipant(String participant) {
        if (this.participants == null) {
            this.participants = new HashSet<String>();
        }
        this.participants.add(participant);
    }

    @Override
    public String toString() {
        return "Event {" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", link='" + link + '\'' +
                ", date='" + date + '\'' +
                ", minUsers='" + minusers + '\'' +
                '}';
    }
}
