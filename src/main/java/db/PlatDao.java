package db;

import beans.Aliment;
import beans.Plat;

public abstract class PlatDao extends Dao<Plat> {
    public PlatDao(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    public abstract boolean createAliment(Plat plat, Aliment aliment) throws DaoException;
    public abstract boolean getAliment(Plat plat, String idAliment) throws DaoException;
    public abstract boolean update(Plat plat, Aliment aliment) throws DaoException;
    public abstract boolean delete(Plat plat, Aliment aliment) throws DaoException;
}
