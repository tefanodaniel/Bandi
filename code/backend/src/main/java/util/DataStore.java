package util;

import model.SpeedDateEvent;
import model.Musician;
import model.Band;

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

        // Adding my own account for testing
        Set<String> maxLinks = new HashSet<String>();
        maxLinks.add("https://www.youtube.com/watch?v=NPBCbTZWnq0");

        samples.add(new Musician("22zcnk76clvox7mifcwgz3tha","Max Torres", genres2, instruments2, "Expert",
                maxLinks, "Freehold, NJ", "07728",  kennyFriends,true)); // Max and Kenny G are best friends
      
        samples.add(new Musician("thegreatbelow1","Stefano Tusa", genres2, instruments2, "Intermediate",
                new HashSet<String>(),  "Orlando, FL", "32825", kennyFriends, true)); // Max, Kenny G, and I are all best friends.

        // Adding Nick as an admin so he can test admin functionality
        Musician Nick = new Musician("12101628937","Nick Xitco",
                genres2, instruments2, "Expert", maxLinks, "New York, NY", "32825", kennyFriends, true);
        samples.add(Nick);

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
}
