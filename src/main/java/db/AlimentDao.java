package db;

import beans.Aliment;

public abstract class AlimentDao extends Dao<Aliment> {
    public AlimentDao(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
}
