package db;

import beans.Journal;

public abstract class JournalDao extends Dao<Journal> {
    public JournalDao(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
}
