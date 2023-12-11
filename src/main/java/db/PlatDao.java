package db;

import beans.Aliment;
import beans.Plat;

public abstract class PlatDao extends Dao<Plat> {
    public PlatDao(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    protected abstract boolean createAliment(Plat plat, Aliment aliment, double quantite) throws DaoException;
    protected abstract boolean getAliments(Plat plat) throws DaoException;
    protected abstract boolean updateAliment(Plat plat, Aliment aliment, double quantite) throws DaoException;
    protected abstract boolean deleteAliment(Plat plat, Aliment aliment) throws DaoException;
}
