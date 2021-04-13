package model;
import java.util.HashSet;
import java.util.Set;

public class SongOfTheWeekSubmission {
    private String submission_id;
    private String musician_id;
    private String musician_name;
    private String avSubmission;
    private Set<String> instruments;

    public SongOfTheWeekSubmission(String submission_id, String musician_id, String musician_name, String avSubmission) {
        this.submission_id = submission_id;
        this.musician_id = musician_id;
        this.musician_name = musician_name;
        this.avSubmission = avSubmission;
    }


    public SongOfTheWeekSubmission(String submission_id, String musician_id, String musician_name, String avSubmission, Set<String> instruments) {
        this.submission_id = submission_id;
        this.musician_id = musician_id;
        this.musician_name = musician_name;
        this.avSubmission = avSubmission;
        this.instruments = instruments;
    }

    public String getSubmission_id () {
        return this.submission_id;
    }

    public String getMusician_id() {
        return this.musician_id;
    }

    public String getMusician_name() {
        return this.musician_name;
    }

    public String getAVSubmission() {
        return this.avSubmission;
    }

    public void setAVSubmission(String avSubmission){
        this.avSubmission = avSubmission;
    }

    public Set<String> getInstruments(){
        return this.instruments;
    }

    public void setInstruments(Set<String> instruments){
        this.instruments = instruments;
    }

    public void addInstrument(String instrument) {
        if (this.instruments == null) {
            this.instruments = new HashSet<String>();
        }
        this.instruments.add(instrument);
    }

    @Override
    public String toString() {
        return "SongOfTheWeek Submission {" +
                ", submission id = '" + this.submission_id + '\''+
                ", musician id = '" + this.musician_id + '\''+
                ", musician name = '" + this.musician_name + '\''+
                ", audio/video Submission = '" + this.avSubmission + '\''+
                ", instruments = '" + this.instruments + '\'' + '}';
    }
}
