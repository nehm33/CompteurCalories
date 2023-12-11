package db;

import beans.Aliment;
import beans.Journal;
import beans.Plat;

public class JournalDaoImpl extends JournalDao {
    public JournalDaoImpl(DaoFactory daoFactory) {
        super(daoFactory);
    }

    @Override
    public boolean create(Journal obj) throws DaoException {
        return false;
    }

    @Override
    public Journal get(String id) throws DaoException {
        return null;
    }

    @Override
    public boolean update(Journal obj, Journal oldObj) throws DaoException {
        return false;
    }

    @Override
    public boolean delete(Journal obj) throws DaoException {
        return false;
    }

    @Override
    protected boolean createAliment(Journal journal, Aliment aliment, double quantite) {
        return false;
    }

    @Override
    protected boolean getAliments(Journal journal) {
        return false;
    }

    @Override
    protected boolean updateAliment(Journal journal, Aliment aliment, double quantite) {
        return false;
    }

    @Override
    protected boolean deleteAliment(Journal journal, Aliment aliment) {
        return false;
    }

    @Override
    protected boolean createPlat(Journal journal, Plat plat, double quantite) {
        return false;
    }

    @Override
    protected boolean getPlats(Journal journal) {
        return false;
    }

    @Override
    protected boolean updatePlat(Journal journal, Plat plat, double quantite) {
        return false;
    }

    @Override
    protected boolean deletePlat(Journal journal, Plat plat) {
        return false;
    }
}
