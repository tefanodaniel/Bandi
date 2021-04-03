package util;

import model.*;

import java.util.*;

/**
 * A utility class with methods to create sample data.
 */
public final class DataStore {

    private DataStore() {
        // This class should not be instantiated.
    }

    /**
     * Create a list of sample musicians.
     *
     * @return a list of sample musicians.
     */
    public static List<Musician> sampleMusicians() {
        // Create lists of genres for the test musicians
        Set<String> genres1 = new HashSet<String>(Arrays.asList("Progressive Rock", "Psychedelic Rock"));
        Set<String> genres2 = new HashSet<String>(Arrays.asList("Blues", "Rock", "Classic Rock"));
        Set<String> genres3 = new HashSet<String>(Arrays.asList("Jazz"));

        // Create lists of instruments for the test musicians
        Set<String> instruments1 = new HashSet<String>(Arrays.asList("Guitar"));
        Set<String> instruments2 = new HashSet<String>(Arrays.asList("Guitar", "Vocals"));
        Set<String> instruments3 = new HashSet<String>(Arrays.asList("Saxophone"));
        Set<String> instruments4 = new HashSet<String>(Arrays.asList("Drums"));
        Set<String> profileLinks = new HashSet<String>();

        // Create lists of friends for the test musicians
        Set<String> davidFriends = new HashSet<String>(Arrays.asList());
        Set<String> ericFriends = new HashSet<String>(Arrays.asList("00001fakeid"));
        Set<String> kennyFriends = new HashSet<String>(Arrays.asList("00001fakeid", "00002fakeid"));
        Set<String> rogerFriends = new HashSet<String>(Arrays.asList("00001fakeid", "00002fakeid", "00003fakeid"));
        Set<String> nickFriends = new HashSet<String>(Arrays.asList("00001fakeid", "00002fakeid", "00003fakeid", "00004fakeid"));

        List<Musician> samples = new ArrayList<>();
        samples.add(new Musician("00001fakeid","David Gilmour", genres1, instruments1, "Expert",
                profileLinks, "WNY, NJ", "07093", davidFriends, false));
        samples.add(new Musician("00002fakeid","Eric Clapton", genres2, instruments2, "Expert",
                profileLinks, "Edgewater, NJ", "07020", ericFriends,false));
        samples.add(new Musician("00003fakeid","Kenny G", genres3, instruments3, "Expert",
                profileLinks, "San Diego, CA", "92168",kennyFriends, false));
        samples.add(new Musician("00004fakeid","Roger Waters", genres1, instruments2, "Expert",
                profileLinks, "Grand Prairie, TX", "75050", rogerFriends, false));
        samples.add(new Musician("00005fakeid","Nick Mason", genres1, instruments4, "Expert",
                                        profileLinks,"Cambridge, MA", "02138", nickFriends, false));

        samples.add(new Musician("00006fakeid","Michael Jackson", genres3, instruments4, "Expert",
                profileLinks,"Cambridge, MA", "02138", rogerFriends, false));

        samples.add(new Musician("00007fakeid","George Michael", genres1, instruments2, "Expert",
                profileLinks,"San Diego, CA", "92168", nickFriends, false));

        samples.add(new Musician("00008fakeid","Mariah Carey", genres2, instruments1, "Expert",
                profileLinks,"San Diego, CA", "92168", kennyFriends, false));

        samples.add(new Musician("00009fakeid","Robin Williams", genres1, instruments3, "Expert",
                profileLinks,"Grand Praire, TX", "75050", davidFriends, false));

        samples.add(new Musician("000010fakeid","Prince", genres1, instruments1, "Expert",
                profileLinks,"Cambridge, MA", "02138", nickFriends, false));


        // To Do : Add all Bandi Admins to enable testing admin functionality
        Set<String> maxLinks = new HashSet<String>();
        maxLinks.add("https://www.youtube.com/watch?v=NPBCbTZWnq0");

        Set<String> og_link = new HashSet<String>();
        og_link.add("https://youtu.be/dQw4w9WgXcQ");

        samples.add(new Musician("22zcnk76clvox7mifcwgz3tha","Max Torres", genres2, instruments2, "Expert",
                maxLinks, "Freehold, NJ", "07728",  kennyFriends,true)); // Max and Kenny G are best friends
      
        samples.add(new Musician("thegreatbelow1","Stefano Tusa", genres2, instruments2, "Intermediate",
                new HashSet<String>(),  "Orlando, FL", "32825", kennyFriends, true)); // Max, Kenny G, and I are all best friends.

        Musician Nick = new Musician("12101628937","Nick Xitco",
                genres2, instruments2, "Expert", maxLinks, "New York, NY", "32825", kennyFriends, true);
        samples.add(Nick);

        samples.add(new Musician("22xpmsx47uendfh4kafp3zjmi", "Ramchandran Muthukumar", genres3, instruments3, "Expert",
                og_link, "Baltimore, MD", "21218", kennyFriends, true));



        return samples;
    }

    /**
     * Create a list of sample bands (based on the above sample musicians).
     *
     * @return a list of sample bands.
     */
    public static List<Band> sampleBands() {
        Set<String> members = new HashSet<>();
        members.add("00001fakeid");
        members.add("00004fakeid");
        members.add("00005fakeid");

        Set<String> members2 = new HashSet<>();
        members.add("00001fakeid");
        members.add("22zcnk76clvox7mifcwgz3tha");

        Set<String> genres = new HashSet<>();
        genres.add("Rock");
        genres.add("Pop");

        String bandId = UUID.randomUUID().toString();
        String id2 = UUID.randomUUID().toString();

        List<Band> sample_bands = new ArrayList<>();
        sample_bands.add(new Band(bandId,"Pink Floyd",4, genres, members));
        sample_bands.add(new Band(id2, "Imagine Dragons",4, genres, members2));
        return sample_bands;
    }

    /**
     * Create a list of sample events
     *
     * @return a list of sample events.
     */
    public static List<SpeedDateEvent> sampleSpeedDateEvents() {

        Set<String> participants = new HashSet<>();
        participants.add("22zcnk76clvox7mifcwgz3tha");

        SpeedDateEvent event1 = new SpeedDateEvent(UUID.randomUUID().toString(), "Speed-Dating Part 1",
                "https://zoom.us/", "April 15, 2021 8-9pm", 4, participants);
        SpeedDateEvent event2 = new SpeedDateEvent(UUID.randomUUID().toString(), "Speed-Dating Part 2",
                "https://zoom.us/", "April 16, 2021 4-5pm", 6, participants);

        List<SpeedDateEvent> sample_events = new ArrayList<>();
        sample_events.add(event1);
        sample_events.add(event2);
        return sample_events;
    }

    /**
     *  Create a list of sample songs for song-of-the-week events
     * @return
     */
    public static List<Song> sampleSongs() {
        List<Song> sample_songs = new ArrayList<>();

        Set<String> g1 = new HashSet<>();
        g1.add("Pop");
        g1.add("R&B");
        Song s1 = new Song("00001fakesongid", "Peaches", "Justin Bieber", "null", 2021, g1);

        Set<String> g2 = new HashSet<>();
        g2.add("Hip Hop");
        Song s2 = new Song("00002fakesongid", "Up", "Cardi B", "null", 2021, g2);

        Set<String> g3 = new HashSet<>();
        g3.add("Pop");
        g3.add("R&B");
        g3.add("Funk");
        g3.add("Soul");
        g3.add("Rock");
        g3.add("Hip Hop");
        Song s3 = new Song("00003fakesongid", "Leave The Door Open", "Silk Sonic", "null", 2021, g3);

        Set<String> g4 = new HashSet<>();
        g4.add("Pop");
        Song s4 = new Song("00004fakesongid", "Drivers License", "Olivia Rodrigo", "null", 2021, g4);

        Set<String> g5 = new HashSet<>();
        g5.add("Pop");
        g5.add("R&B");
        g5.add("ElectroPop");
        Song s5 = new Song("00005fakesongid", "Save Your Tears", "The Weeknd", "null", 2021, g5);

        Set<String> g6 = new HashSet<>();
        g6.add("Pop");
        g6.add("R&B");
        g6.add("ElectroPop");
        Song s6 = new Song("00006fakesongid", "Blinding Lights", "The Weeknd", "null", 2021, g6);

        Set<String> g7 = new HashSet<>();
        g7.add("Pop");
        g7.add("Disco");
        g7.add("R&B");
        Song s7 = new Song("00007fakesongid", "Levitating", "Dua Lipa", "null", 2021, g7);

        Set<String> g8 = new HashSet<>();
        g8.add("Pop");
        g8.add("R&B");
        g8.add("Trap");
        g8.add("Hip Hop");
        Song s8 = new Song("00008fakesongid", "What's Next", "Drake", "null", 2021, g8);

        Set<String> g9 = new HashSet<>();
        g9.add("Hip Hop");
        Song s9 = new Song("00009fakesongid", "What You Know Bout Love", "Pop Smoke", "null", 2021, g9);

        Set<String> g10 = new HashSet<>();
        g10.add("Pop");
        g10.add("R&B");
        g10.add("Trap");
        g10.add("Hip Hop");
        Song s10 = new Song("000010fakesongid", "Mood", "24kGoldn", "null", 2021, g10);

        Set<String> g11 = new HashSet<>();
        g11.add("Pop");
        g11.add("R&B");
        Song s11 = new Song("000011fakesongid", "34+35", "Ariana Grande", "null", 2021, g11);

        Set<String> g12 = new HashSet<>();
        g12.add("Pop");
        g12.add("R&B");
        g12.add("Hip Hop");
        Song s12 = new Song("000012fakesongid", "Go Crazy", "Chris Brown", "null", 2021, g12);

        sample_songs.add(s1);
        sample_songs.add(s2);
        sample_songs.add(s3);
        sample_songs.add(s4);
        sample_songs.add(s5);
        sample_songs.add(s6);
        sample_songs.add(s7);
        sample_songs.add(s8);
        sample_songs.add(s9);
        sample_songs.add(s10);
        sample_songs.add(s11);
        sample_songs.add(s12);

        return sample_songs;
    }

    /**
     * Create a list of sample sotw submissions for song-of-the-week events
     * @return
     */
    public static List<SongOfTheWeekSubmission> sampleSotwSubmissions() {
        List<SongOfTheWeekSubmission> sample_submissions = new ArrayList<>();

        // Create lists of instruments for the test submissions
        Set<String> instruments1 = new HashSet<String>(Arrays.asList("Guitar"));
        Set<String> instruments2 = new HashSet<String>(Arrays.asList("Guitar", "Vocals"));
        Set<String> instruments3 = new HashSet<String>(Arrays.asList("Saxophone"));
        Set<String> instruments4 = new HashSet<String>(Arrays.asList("Drums"));

        String avLink = "https://youtu.be/dQw4w9WgXcQ";

        sample_submissions.add(new SongOfTheWeekSubmission("00001fakesubmissionid","00001fakeid", avLink, instruments1));
        sample_submissions.add(new SongOfTheWeekSubmission("00002fakesubmissionid","00002fakeid", avLink, instruments2));
        sample_submissions.add(new SongOfTheWeekSubmission("00003fakesubmissionid","00003fakeid", avLink, instruments3));
        sample_submissions.add(new SongOfTheWeekSubmission("00004fakesubmissionid","00004fakeid", avLink, instruments4));
        sample_submissions.add(new SongOfTheWeekSubmission("00005fakesubmissionid","00005fakeid", avLink, instruments1));
        sample_submissions.add(new SongOfTheWeekSubmission("00006fakesubmissionid","00006fakeid", avLink, instruments2));
        sample_submissions.add(new SongOfTheWeekSubmission("00007fakesubmissionid","00007fakeid", avLink, instruments3));
        sample_submissions.add(new SongOfTheWeekSubmission("00008fakesubmissionid","00008fakeid", avLink, instruments4));
        sample_submissions.add(new SongOfTheWeekSubmission("00009fakesubmissionid","00009fakeid", avLink, instruments1));
        sample_submissions.add(new SongOfTheWeekSubmission("000010fakesubmissionid","000010fakeid", avLink, instruments2));
        sample_submissions.add(new SongOfTheWeekSubmission("000011fakesubmissionid","00001fakeid", avLink, instruments3));
        sample_submissions.add(new SongOfTheWeekSubmission("000012fakesubmissionid","00002fakeid", avLink, instruments4));
        sample_submissions.add(new SongOfTheWeekSubmission("000013fakesubmissionid","00003fakeid", avLink, instruments1));
        sample_submissions.add(new SongOfTheWeekSubmission("000014fakesubmissionid","00004fakeid", avLink, instruments2));
        sample_submissions.add(new SongOfTheWeekSubmission("000015fakesubmissionid","00005fakeid", avLink, instruments3));
        sample_submissions.add(new SongOfTheWeekSubmission("000016fakesubmissionid","00006fakeid", avLink, instruments4));
        sample_submissions.add(new SongOfTheWeekSubmission("000017fakesubmissionid","00007fakeid", avLink, instruments1));
        sample_submissions.add(new SongOfTheWeekSubmission("000018fakesubmissionid","00008fakeid", avLink, instruments2));
        sample_submissions.add(new SongOfTheWeekSubmission("000019fakesubmissionid","00009fakeid", avLink, instruments3));
        sample_submissions.add(new SongOfTheWeekSubmission("000020fakesubmissionid","000010fakeid", avLink, instruments4));
        sample_submissions.add(new SongOfTheWeekSubmission("000021fakesubmissionid","00001fakeid", avLink, instruments1));
        sample_submissions.add(new SongOfTheWeekSubmission("000022fakesubmissionid","00002fakeid", avLink, instruments2));
        sample_submissions.add(new SongOfTheWeekSubmission("000023fakesubmissionid","00003fakeid", avLink, instruments3));
        sample_submissions.add(new SongOfTheWeekSubmission("000024fakesubmissionid","00004fakeid", avLink, instruments4));

        return sample_submissions;
    }

    /**
     * Create a list of sample song-of-the-week events
     * @return
     */
    public static List<SongOfTheWeekEvent> sampleSotwEvents() {
        List<SongOfTheWeekEvent> sample_events = new ArrayList<>();
        Set<String> submissions1 = new HashSet<String>(Arrays.asList("00001fakesubmissionid", "00002fakesubmissionid", "00003fakesubmissionid", "00004fakesubmissionid"));
        Set<String> submissions2 = new HashSet<String>(Arrays.asList("00005fakesubmissionid", "00006fakesubmissionid", "00007fakesubmissionid", "00008fakesubmissionid"));
        Set<String> submissions3 = new HashSet<String>(Arrays.asList("00009fakesubmissionid", "000010fakesubmissionid", "000011fakesubmissionid", "000012fakesubmissionid"));
        Set<String> submissions4 = new HashSet<String>(Arrays.asList("000013fakesubmissionid", "000014fakesubmissionid", "000015fakesubmissionid", "000016fakesubmissionid"));
        Set<String> submissions5 = new HashSet<String>(Arrays.asList("000017fakesubmissionid", "000018fakesubmissionid", "000019fakesubmissionid", "000020fakesubmissionid"));
        Set<String> submissions6 = new HashSet<String>(Arrays.asList("000021fakesubmissionid", "000022fakesubmissionid", "000023fakesubmissionid", "000024fakesubmissionid"));
        // get current week and choose a song from the billboard hot-100?
        Set<String> instruments2 = new HashSet<String>(Arrays.asList("Guitar", "Vocals"));

        String start_week = "Sunday 28th March";
        String end_week = "Saturday 3rd March";

        String admin1 = "22zcnk76clvox7mifcwgz3tha";
        String admin2 = "22xpmsx47uendfh4kafp3zjmi";

        String songid1 = "00001fakesongid";
        String songid2 = "00002fakesongid";
        String songid3 = "00003fakesongid";
        String songid4 = "00004fakesongid";
        String songid5 = "00005fakesongid";
        String songid6 = "00006fakesongid";

        sample_events.add(new SongOfTheWeekEvent("00001fakeeventid", admin1, start_week, end_week, songid1, submissions1));
        sample_events.add(new SongOfTheWeekEvent("00002fakeeventid", admin2, start_week, end_week, songid2, submissions2));
        sample_events.add(new SongOfTheWeekEvent("00003fakeeventid", admin1, start_week, end_week, songid3, submissions3));
        sample_events.add(new SongOfTheWeekEvent("00004fakeeventid", admin2, start_week, end_week, songid4, submissions4));
        sample_events.add(new SongOfTheWeekEvent("00005fakeeventid", admin1, start_week, end_week, songid5, submissions5));
        sample_events.add(new SongOfTheWeekEvent("00006fakeeventid", admin2, start_week, end_week, songid6, submissions6));

        return sample_events;
    }
}

