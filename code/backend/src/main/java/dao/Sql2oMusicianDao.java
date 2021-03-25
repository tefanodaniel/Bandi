package dao;

import model.Musician;
import exceptions.DaoException;

import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;
import org.sql2o.data.Row;

import java.util.*;

public class Sql2oMusicianDao implements MusicianDao {

    private final Sql2o sql2o;

    /**
     * Construct Sql2oCourseDao.
     *
     * @param sql2o A Sql2o object is injected as a dependency;
     *   it is assumed sql2o is connected to a database that  contains a table called
     *   "Musicians" with columns: "id", "name", and "genre".
     */
    public Sql2oMusicianDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Musician create(String id, String name, Set<String> genres, Set<String> instruments,
                           String experience, String location, Set<String> profileLinks) throws DaoException {
        // TODO: re-implement? Yes -- DONE
        String musicianSQL = "INSERT INTO Musicians (id, name, experience, location) VALUES (:id, :name, :experience, :location)";
        String genresSQL = "INSERT INTO MusicianGenres (id, genre) VALUES (:id, :genre)";
        String instrumentsSQL = "INSERT INTO Instruments (id, instrument) VALUES (:id, :instrument)";
        String profileLinksSQL = "INSERT INTO ProfileAVLinks (id, link) VALUES (:id, :link)";
        try (Connection conn = sql2o.open()) {
            // Insert musician into database
            conn.createQuery(musicianSQL)
                    .addParameter("id", id)
                    .addParameter("name", name)
                    .addParameter("experience", experience)
                    .addParameter("location", location)
                    .executeUpdate();

            // Insert corresponding genres into database
            for (String genre : genres) {
                conn.createQuery(genresSQL)
                        .addParameter("id", id)
                        .addParameter("genre", genre)
                        .executeUpdate();
            }

            // Insert corresponding instruments into database
            for (String instrument : instruments) {
                conn.createQuery(instrumentsSQL)
                        .addParameter("id", id)
                        .addParameter("instrument", instrument)
                        .executeUpdate();
            }

            // Insert corresponding profile audio/video links into database
            for (String link : profileLinks) {
                conn.createQuery(profileLinksSQL)
                        .addParameter("id", id)
                        .addParameter("link", link)
                        .executeUpdate();
            }

            // Return musician
            return this.read(id);
        } catch (Sql2oException ex) {
            throw new DaoException(ex.getMessage(), ex);
        }
    }

    @Override
    public Musician create(String id, String name) throws DaoException {
        // TODO: re-implement? I don't think so
        String sql = "WITH inserted AS ("
                + "INSERT INTO Musicians(id, name) " +
                "VALUES(:id, :name) RETURNING *"
                + ") SELECT * FROM inserted;";
        try (Connection conn = sql2o.open()) {
            return conn.createQuery(sql)
                    .addParameter("id", id)
                    .addParameter("name", name)
                    .executeAndFetchFirst(Musician.class);
        } catch (Sql2oException ex) {
            throw new DaoException(ex.getMessage(), ex);
        }
    }

    @Override
    public Musician read(String id) throws DaoException {
        // TODO: re-implement? Yes -- DONE
        try (Connection conn = sql2o.open()) {
            //String sql = "SELECT * " +
              //      "FROM Musicians AS m, Instruments AS i, MusicianGenres AS g, ProfileAVLinks as l " +
                //    "WHERE m.id=:id AND m.id=i.id AND m.id=g.id AND m.id=l.id";

            String sql = "SELECT * FROM (SELECT m.id as MID, * FROM musicians as m) as R\n" +
                    "LEFT JOIN instruments as I ON R.MID=I.id\n" +
                    "LEFT JOIN musiciangenres as G ON R.MID=G.id\n" +
                    "LEFT JOIN profileavlinks as L ON R.MID=L.id\n" +
                    "WHERE R.mid=:id;";
            List<Map<String, Object>> queryResults = conn.createQuery(sql).addParameter("id", id).executeAndFetchTable().asList();

            if (queryResults.size() == 0) { // no such musician is present
                return null;
            }

            // Extract attributes
            String name = (String) queryResults.get(0).get("name");
            String exp = (String) queryResults.get(0).get("experience");
            String loc = (String) queryResults.get(0).get("location");
            String zipCode = (String) queryResults.get(0).get("zipCode");

            Musician m = new Musician(id, name, new HashSet<String>(), new HashSet<String>(),
                    exp, new HashSet<String>(), loc, zipCode);
            for (Map row : queryResults) {
                m.addGenre((String) row.get("genre"));
                m.addInstrument((String) row.get("instrument"));
                m.addProfileLink((String) row.get("link"));
            }

            return m;
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to read a musician with id " + id, ex);
        }
    }

    @Override
    public List<Musician> readAll() throws DaoException {
        // TODO: re-implement? Yes -- DONE
        String sql = "SELECT * FROM (SELECT m.id as MID, * FROM musicians as m) as R\n" +
                "LEFT JOIN instruments as I ON R.MID=I.id\n" +
                "LEFT JOIN musiciangenres as G ON R.MID=G.id\n" +
                "LEFT JOIN profileavlinks as L ON R.MID=L.id;";
        try (Connection conn = sql2o.open()) {
            List<Musician> musicians = this.extractMusiciansFromDatabase(sql, conn);
            return musicians;
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to read musicians from the database", ex);
        }
    }

    @Override
    public List<Musician> readAll(Map<String, String[]> query) throws DaoException {
        // TODO: re-implement? Yes -- DONE FOR NOW. See comment below.
        /* TODO Note that this may need work when it comes to handling queries with multiple values for the same query param, for example.
            We need to loop over query.get(key) around line 132.
         */
        try (Connection conn = sql2o.open()) {
            Set<String> keys = query.keySet();
            Iterator<String> iter = keys.iterator();
            String key = iter.next();
            String filterOn = "UPPER(" + key + ") LIKE '%" + query.get(key)[0].toUpperCase() + "%'"; // if we always assume 0, we're missing alternate values for the param
            if (query.size() > 1) {
                while (iter.hasNext()) {
                    String attribute = iter.next();
                    String filter = query.get(attribute)[0];
                    filterOn = filterOn + " AND UPPER(" + attribute + ") LIKE '%" +
                            filter.toUpperCase() + "%'";
                }
            }
            filterOn = filterOn + ";";

            String filterSQL = "SELECT * FROM (SELECT m.id as MID, * FROM musicians as m) as R\n" +
                    "LEFT JOIN instruments as I ON R.MID=I.id\n" +
                    "LEFT JOIN musiciangenres as G ON R.MID=G.id\n" +
                    "WHERE " + filterOn;

            String resultSQL = "SELECT * FROM (SELECT m.id as MID, * FROM musicians as m) as R\n" +
                    "LEFT JOIN instruments as I ON R.MID=I.id\n" +
                    "LEFT JOIN musiciangenres as G ON R.MID=G.id\n" +
                    "LEFT JOIN profileavlinks as L ON R.MID=L.id\n" +
                    "WHERE R.mid IN (";

            List<Musician> partialMusicians = this.extractMusiciansFromDatabase(filterSQL, conn);
            for (int i=0; i < partialMusicians.size(); i++) {
                if (i == partialMusicians.size() - 1) {
                    resultSQL += "'" + partialMusicians.get(i).getId() + "');";
                } else {
                    resultSQL += "'" + partialMusicians.get(i).getId() + "', ";
                }
            }
            return this.extractMusiciansFromDatabase(resultSQL, conn);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to read musicians from the database by filters", ex);
        }
    }

    @Override
    public Musician updateName(String id, String name) throws DaoException {
        // TODO: re-implement? No -- DONE
        String sql = "WITH updated AS ("
                + "UPDATE Musicians SET name = :name WHERE id = :id RETURNING *"
                + ") SELECT * FROM updated;";
        try (Connection conn = sql2o.open()) {
            return conn.createQuery(sql)
                    .addParameter("id", id)
                    .addParameter("name", name)
                    .executeAndFetchFirst(Musician.class);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to update the musician name", ex);
        }
    }

    @Override
    public Musician updateGenres(String id, Set<String> newGenres) throws DaoException {
        // TODO: re-implement? Yes -- DONE
        String getCurrentGenresSQL = "SELECT * FROM MusicianGenres WHERE id=:id";
        String deleteGenreSQL = "DELETE FROM MusicianGenres WHERE id=:id AND genre=:genre";
        String insertGenreSQL = "INSERT INTO MusicianGenres (id, genre) VALUES (:id, :genre)";
        try (Connection conn = sql2o.open()) {
            // Get current genres stored in DB for this musician
            List<Map<String, Object>> rows = conn.createQuery(getCurrentGenresSQL).addParameter("id", id).executeAndFetchTable().asList();
            HashSet<String> currentGenres = new HashSet<String>();
            for (Map row : rows) {
                currentGenres.add((String) row.get("genre"));
            }

            // Delete any values currently in the database that aren't in the new set of genres to store
            for (String genre : currentGenres) {
                if (!newGenres.contains(genre)) {
                    conn.createQuery(deleteGenreSQL).addParameter("id", id).addParameter("genre", genre).executeUpdate();
                }
            }

            // Add new genres to the database, if they aren't already in there
            for (String genre : newGenres) {
                if (!currentGenres.contains(genre)) {
                    conn.createQuery(insertGenreSQL).addParameter("id", id).addParameter("genre", genre).executeUpdate();
                }
            }

            // Return Musician object for this particular id
            return this.read(id);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to update the musician genre", ex);
        }
    }

    @Override
    public Musician updateInstruments(String id, Set<String> newInstruments) throws DaoException {
        // TODO: re-implement? Yes -- DONE
        String getCurrentInstrumentsSQL = "SELECT * FROM Instruments WHERE id=:id";
        String deleteInstrumentSQL = "DELETE FROM Instruments WHERE id=:id AND instrument=:instrument";
        String insertInstrumentSQL = "INSERT INTO Instruments (id, instrument) VALUES (:id, :instrument)";
        try (Connection conn = sql2o.open()) {
            // Get current instruments stored in DB for this musician
            List<Map<String, Object>> rows = conn.createQuery(getCurrentInstrumentsSQL).addParameter("id", id).executeAndFetchTable().asList();
            HashSet<String> currentInstruments = new HashSet<String>();
            for (Map row : rows) {
                currentInstruments.add((String) row.get("instrument"));
            }

            // Delete any values currently in the database that aren't in the new set of genres to store
            for (String instrument : currentInstruments) {
                if (!newInstruments.contains(instrument)) {
                    conn.createQuery(deleteInstrumentSQL).addParameter("id", id).addParameter("instrument", instrument).executeUpdate();
                }
            }

            // Add new genres to the database, if they aren't already in there
            for (String instrument : newInstruments) {
                if (!currentInstruments.contains(instrument)) {
                    conn.createQuery(insertInstrumentSQL).addParameter("id", id).addParameter("instrument", instrument).executeUpdate();
                }
            }

            // Return Musician object for this particular id
            return this.read(id);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to update the musician instrument", ex);
        }
    }

    @Override
    public Musician updateExperience(String id, String experience) throws DaoException {
        // TODO: re-implement? No -- DONE
        String sql = "WITH updated AS ("
                + "UPDATE Musicians SET experience = :experience WHERE id = :id RETURNING *"
                + ") SELECT * FROM updated;";
        try (Connection conn = sql2o.open()) {
            return conn.createQuery(sql)
                    .addParameter("id", id)
                    .addParameter("experience", experience)
                    .executeAndFetchFirst(Musician.class);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to update the musician experience", ex);
        }
    }

    @Override
    public Musician updateLocation(String id, String location) throws DaoException {
        // TODO: re-implement? No -- DONE
        String sql = "WITH updated AS ("
                + "UPDATE Musicians SET location = :location WHERE id = :id RETURNING *"
                + ") SELECT * FROM updated;";
        try (Connection conn = sql2o.open()) {
            return conn.createQuery(sql)
                    .addParameter("id", id)
                    .addParameter("location", location)
                    .executeAndFetchFirst(Musician.class);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to update the musician location", ex);
        }
    }

    @Override
    public Musician updateProfileLinks(String id, Set<String> newLinks) throws DaoException {
        String getCurrentLinksSQL = "SELECT * FROM ProfileAVLinks WHERE id=:id";
        String deleteLinkSQL = "DELETE FROM ProfileAVLinks WHERE id=:id AND link=:link";
        String insertLinkSQL = "INSERT INTO ProfileAVLinks (id, link) VALUES (:id, :link)";
        try (Connection conn = sql2o.open()) {
            // Get current instruments stored in DB for this musician
            List<Map<String, Object>> rows = conn.createQuery(getCurrentLinksSQL).addParameter("id", id).executeAndFetchTable().asList();
            HashSet<String> currentLinks = new HashSet<String>();
            for (Map row : rows) {
                currentLinks.add((String) row.get("link"));
            }

            // Delete any values currently in the database that aren't in the new set of genres to store
            for (String link : currentLinks) {
                if (!newLinks.contains(link)) {
                    conn.createQuery(deleteLinkSQL).addParameter("id", id).addParameter("link", link).executeUpdate();
                }
            }

            // Add new genres to the database, if they aren't already in there
            for (String link : newLinks) {
                if (!currentLinks.contains(link)) {
                    conn.createQuery(insertLinkSQL).addParameter("id", id).addParameter("link", link).executeUpdate();
                }
            }

            // Return Musician object for this particular id
            return this.read(id);
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to update the musician links", ex);
        }
    }

    @Override
    public Musician delete(String id) throws DaoException {
        // TODO: re-implement? Yes. Need to see about cascading deletes, though See note below
        /* TODO: I'm not worrying about cascading deletes here. We should later though, because this will get a little unwieldy. */
        String deleteGenresSQL = "DELETE FROM MusicianGenres WHERE id=:id;";
        String deleteInstrumentsSQL = "DELETE FROM Instruments WHERE id=:id;";
        String deleteProfileLinksSQL = "DELETE FROM ProfileAVLinks WHERE id=:id;";
        String deleteMusicianSQL = "DELETE FROM Musicians WHERE id=:id;";
        try (Connection conn = sql2o.open()) {
            // Get musician to return before we delete
            Musician musicianToDelete = this.read(id);

            // Delete associated instruments
            conn.createQuery(deleteInstrumentsSQL).addParameter("id", id).executeUpdate();

            // Delete associated profile links
            conn.createQuery(deleteProfileLinksSQL).addParameter("id", id).executeUpdate();

            // Delete associated genres
            conn.createQuery(deleteGenresSQL).addParameter("id", id).executeUpdate();

            // Delete musician
            conn.createQuery(deleteMusicianSQL).addParameter("id", id).executeUpdate();

            return musicianToDelete;
        } catch (Sql2oException ex) {
            throw new DaoException("Unable to delete the musician", ex);
        }
    }

    /**
     * Query the database and parse the results to create a list of musicians such that each one has the proper list attributes.
     * This is necessary because we store our database in normalized form.
     * @param sql The SQL query string
     * @return the list of musicians
     * @throws Sql2oException if query fails
     */
    private List<Musician> extractMusiciansFromDatabase(String sql, Connection conn) throws Sql2oException {
        List<Map<String, Object>> queryResults = conn.createQuery(sql).executeAndFetchTable().asList();

        HashSet<String> alreadyAdded = new HashSet<String>();
        Map<String, Musician> musicians = new HashMap<String, Musician>();
        for (Map row : queryResults) {
            // Extract data from this row
            String id = (String) row.get("mid");
            String name = (String) row.get("name");
            String exp = (String) row.get("experience");
            String loc = (String) row.get("location");
            String zipCode = (String) row.get("zipCode");
            String genre = (String) row.get("genre");
            String instrument = (String) row.get("instrument");
            String link = (String) row.get("link");

            // Check if we've seen this musician already. If not, create new Musician object
            if (!alreadyAdded.contains(id)) {
                alreadyAdded.add(id);
                musicians.put(id, new Musician(id, name, new HashSet<String>(),
                        new HashSet<String>(), exp, new HashSet<String>(), loc, zipCode));
            }
            // Add the genre and instrument from this row to the object lists
            Musician m = musicians.get(id);
            m.addGenre(genre);
            m.addInstrument(instrument);
            m.addProfileLink(link);
        }
        return new ArrayList<Musician>(musicians.values());
    }
}
