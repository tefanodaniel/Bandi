package util;

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
        Set<String> profileLinks = new HashSet<String>();

        List<Musician> samples = new ArrayList<>();
        samples.add(new Musician("00001fakeid","David Gilmour", genres1, instruments1, "Expert", "England", profileLinks));
        samples.add(new Musician("00002fakeid","Eric Clapton", genres2, instruments2, "Expert", "England", profileLinks));
        samples.add(new Musician("00003fakeid","Kenny G", genres3, instruments3, "Expert", "Seattle", profileLinks));

        return samples;
    }

    /**
     * Create a list of sample bands (based on the above sample musicians).
     *
     * @return a list of sample bands.
     */
    public static List<Band> sampleBands() {
        List<String> members = new ArrayList<>();
        members.add("id1");
        members.add("id2");

        List<Band> sample_bands = new ArrayList<>();
        sample_bands.add(new Band("id1","Pink Floyd", "Rock", 2, 2, members));
        sample_bands.add(new Band("id2","Imagine Dragons","Pop", 2, 2, members));
        return sample_bands;
    }
}