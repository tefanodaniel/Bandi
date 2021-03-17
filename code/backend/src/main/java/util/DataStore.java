package util;

import model.Musician;
import model.Band;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
        List<String> genres1 = new ArrayList<>(Arrays.asList("Progressive Rock", "Psychedelic Rock"));
        List<String> genres2 = new ArrayList<>(Arrays.asList("Blues", "Rock", "Classic Rock"));
        List<String> genres3 = new ArrayList<>(Arrays.asList("Jazz"));

        // Create lists of instruments for the test musicians
        List<String> instruments1 = new ArrayList<>(Arrays.asList("Guitar"));
        List<String> instruments2 = new ArrayList<>(Arrays.asList("Guitar", "Vocals"));
        List<String> instruments3 = new ArrayList<>(Arrays.asList("Saxophone"));

        List<Musician> samples = new ArrayList<>();
        samples.add(new Musician("00001fakeid","David Gilmour", genres1, instruments1, "Expert", "England"));
        samples.add(new Musician("00002fakeid","Eric Clapton", genres2, instruments2, "Expert", "England"));
        samples.add(new Musician("00003fakeid","Kenny G", genres3, instruments3, "Expert", "Seattle"));

        return samples;
    }

    /**
     * Create a list of sample bands (based on the above sample musicians).
     *
     * @return a list of sample bands.
     */
    public static List<Band> sampleBands() {
        List<Musician> sample_musicians = sampleMusicians(); //use this to populate the bands.

        List<Band> sample_bands = new ArrayList<>();
        sample_bands.add(new Band("11111fakeid","Pink Floyd", "Rock"));
        return sample_bands;
    }
}