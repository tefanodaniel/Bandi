package model;

public class Event {

    private String id;
    private String name;
    private String link;
    private String date;
    private int minUsers;

    public Event(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Event(String id, String name, String link, String date, int minUsers) {
        this.id = id;
        this.name = name;
        this.link = link;
        this.date = date;
        this.minUsers = minUsers;
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

    @Override
    public String toString() {
        return "Event {" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", link='" + link + '\'' +
                ", date='" + date + '\'' +
                ", minUsers='" + minUsers + '\'' +
                '}';
    }
}
