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
        samples.add(new Musician("fakeid1","Kenny G", "Jazz"));
        samples.add(new Musician("fakeid2","Bruno Mars", "Pop"));
        return samples;
    }

    /**
     * Create a list of sample bands (based on the above sample musicians).
     *
     * @return a list of sample bands.
     */
    public static List<Band> sampleBands() {
        List<String> members = new ArrayList<>();
        members.add("fakeid1");
        members.add("fakeid2");

        List<Band> sample_bands = new ArrayList<>();
        sample_bands.add(new Band("fake_band_id1","Pink Floyd",
                "Rock", 2, 2, members));
        sample_bands.add(new Band("fake_band_id2","Imagine Dragons",
                "Pop", 2, 2, members));
        return sample_bands;
    }
}