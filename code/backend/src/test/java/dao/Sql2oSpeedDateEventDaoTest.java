package dao;

import model.SpeedDateEvent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.sql2o.Sql2o;
import util.Database;
import java.net.URISyntaxException;
import java.util.*;

public class Sql2oSpeedDateEventDaoTest {
    private static Sql2o sql2o;
    private static List<SpeedDateEvent> speedDateEvents;
    private Sql2oSpeedDateEventDao speedDateEventDao;

    @BeforeAll
    static void connectToDatabase() throws URISyntaxException {
        sql2o = Database.getSql2o();
    }

    @BeforeAll
    static void setSampleSpeedDateEvents() {
        speedDateEvents = new ArrayList<>();

    }

    @BeforeEach
    void injectDependency() {
        
    }

}
