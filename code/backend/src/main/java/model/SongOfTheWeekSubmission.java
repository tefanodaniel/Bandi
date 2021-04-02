package model;
import java.util.Set;

public class SongOfTheWeekSubmission {
    private String submission_id;
    private String musician_id;
    //private String event_id;
    private String avSubmission;
    private Set<String> instruments;


    public SongOfTheWeekSubmission(String submission_id, String musician_id, String avSubmission, Set<String> instruments) {
        this.submission_id = submission_id;
        this.musician_id = musician_id;
        //this.event_id = event_id;
        this.avSubmission = avSubmission;
        this.instruments = instruments;
    }

    public String getSubmission_id () {
        return this.submission_id;
    }

    public String getMusician_id() {
        return this.musician_id;
    }

    //public String getEvent_id(){
    //    return this.event_id;
    //}
    public String getAVSubmission() {
        return this.avSubmission;
    }

    public void setAVSubmission(String avSubmission){
        this.avSubmission = avSubmission;
    }

    public Set<String> getInstrument(){
        return this.instruments;
    }

    public void setInstruments(Set<String> instruments){
        this.instruments = instruments;
    }

    @Override
    public String toString() {
        return "SongOfTheWeek Submission {" +
                ", submission id = '" + this.submission_id + '\''+
                ", musician id = '" + this.musician_id + '\''+
                ", audio/video Submission = '" + this.avSubmission + '\''+
                ", instruments = '" + this.instruments + '\'' + '}';
    }
}
