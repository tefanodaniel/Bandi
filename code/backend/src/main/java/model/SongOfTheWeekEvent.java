package model;
//import model.SongOfTheWeekSubmission;
import java.util.HashSet;
import java.util.Set;

public class SongOfTheWeekEvent {
    private String eventId;
    private String adminId;
    private String start_week;
    private String end_week;
    private String songId;
    private Set<String> submissions;


    public SongOfTheWeekEvent(String eventId, String adminId, String start_week, String end_week, String songId){
        this.eventId = eventId;
        this.adminId = adminId;
        this.start_week = start_week;
        this.end_week = end_week;
        this.songId = songId;
    }

    public SongOfTheWeekEvent(String eventId, String adminId, String start_week, String end_week, String songId, Set<String> submissions){
        this.eventId = eventId;
        this.adminId = adminId;
        this.start_week = start_week;
        this.end_week = end_week;
        this.songId = songId;
        this.submissions = submissions;
    }

    public String getEventId() {
        return this.eventId;
    }

    public String getAdminId() {
        return this.adminId;
    }

    public String getStart_week() {
        return this.start_week;
    }

    public String getEnd_week() {
        return this.end_week;
    }

    public String getSongId() {
        return this.songId;
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
    public String toString() {
        return "SongOfTheWeek Event {" +
                ", eventId = '" + this.eventId + '\''+
                ", adminId = '" + this.adminId + '\''+
                ", songId = '" + this.songId + '\''+
                ", submissions = '" + this.submissions.toString() + '\''+
                '}';
    }

}
