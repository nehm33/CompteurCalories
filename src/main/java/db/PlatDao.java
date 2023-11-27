package db;

import beans.Plat;

public abstract class PlatDao extends Dao<Plat> {
    public PlatDao(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
}
