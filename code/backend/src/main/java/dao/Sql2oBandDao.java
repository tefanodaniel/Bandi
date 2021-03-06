package dao;

import model.Band;
import exceptions.DaoException;

import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.list;

public class Sql2oBandDao implements BandDao {

    private final Sql2o sql2o;

    /**
     * Construct Sql2oBandDao.
     *
     * @param sql2o An sql2o object is injected as a dependency;
     *              it is assumed sql2o is connected to a database that contains a table called
     *              "bands" with three columns: "name", "genre", and "members"
     */
    public Sql2oBandDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Band create() throws DaoException {

    }

    @Override
    Band read(int id) throws DaoException {

    }

    @Override
    List<Band> readAll() throws DaoException {

    }

    @Override
    List<Band> readAll(String genreQuery) throws DaoException {

    }

    @Override
    Band update(int id, String name) throws DaoException{

    }

    @Override
    Band add(int id, Musician newMem) throws DaoException {

    }

    @Override
    Band remove(int id, Musician member, int musID) throws DaoException {

    }

    @Override
    Band delete(int id) throws DaoException {

    }
}