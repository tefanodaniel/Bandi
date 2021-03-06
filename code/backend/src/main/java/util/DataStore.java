package util;

import model.Musician;
import model.Band;

import java.util.ArrayList;
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
        List<Musician> samples = new ArrayList<>();
        samples.add(new Musician("00000fakeid","Kenny G", "Jazz"));
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