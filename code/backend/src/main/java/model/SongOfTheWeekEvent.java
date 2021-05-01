package model;
//import model.SongOfTheWeekSubmission;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class SongOfTheWeekEvent {
    private String eventId;
    private String adminId;
    private String startDay; // style : Sunday April 18, 2021
    private String endDay;
    private String songId;
    private String genre;
    private Set<String> submissions;


    public SongOfTheWeekEvent(String eventId, String adminId, String startDay, String endDay, String songId, String genre){
        this.eventId = eventId;
        this.adminId = adminId;
        this.startDay = startDay;
        this.endDay = endDay;
        this.songId = songId;
        this.genre = genre;
    }

    public SongOfTheWeekEvent(String eventId, String adminId, String startDay, String endDay, String songId, String genre, Set<String> submissions){
        this.eventId = eventId;
        this.adminId = adminId;
        this.startDay = startDay;
        this.endDay = endDay;
        this.songId = songId;
        this.submissions = submissions;
        this.genre = genre;
    }

    public String getEventId() {
        return this.eventId;
    }

    public String getAdminId() {
        return this.adminId;
    }

    public String getStartDay() {
        return this.startDay;
    }

    public String getEndDay() {
        return this.endDay;
    }

    public String getSongId() {
        return this.songId;
    }

    public String getGenre() {
        return this.genre;
    }

    public Set<String> getSubmissions() {
        return this.submissions;
    }

    public void setSubmission(Set<String> submission_set) {
        this.submissions = submission_set;
    }

    public void addSubmissions(String newSubmission) {
        if(this.submissions == null) {
            this.submissions = new HashSet<String>();
        }
        this.submissions.add(newSubmission);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId,adminId,startDay,endDay,songId,genre,submissions);
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        SongOfTheWeekEvent e = (SongOfTheWeekEvent) o;
        if((e.submissions != null) && (submissions != null)) {
            if(!submissions.equals(e.submissions))
                return false;
        }
        return eventId.equals(e.eventId) && adminId.equals(e.adminId) && startDay.equals(e.startDay) &&
                endDay.equals(e.endDay) && songId.equals(e.songId) && genre.equals(e.genre);
    }

    @Override
    public String toString() {
        return "SongOfTheWeek Event {" +
                "eventId = '" + this.eventId + '\''+
                ", adminId = '" + this.adminId + '\''+
                ", startDay = '" + this.startDay + '\''+
                ", endDay = '" + this.endDay + '\''+
                ", songId = '" + this.songId + '\''+
                ", genre = '" + this.genre + '\''+
                '}';
    }

}
