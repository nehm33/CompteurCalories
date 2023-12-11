package db;

import beans.Aliment;
import beans.Journal;
import beans.Plat;

public abstract class JournalDao extends Dao<Journal> {
    public JournalDao(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    protected abstract boolean createAliment(Journal journal, Aliment aliment, double quantite);
    protected abstract boolean getAliments(Journal journal);
    protected abstract boolean updateAliment(Journal journal, Aliment aliment, double quantite);
    protected abstract boolean deleteAliment(Journal journal, Aliment aliment);
    protected abstract boolean createPlat(Journal journal, Plat plat, double quantite);
    protected abstract boolean getPlats(Journal journal);
    protected abstract boolean updatePlat(Journal journal, Plat plat, double quantite);
    protected abstract boolean deletePlat(Journal journal, Plat plat);
}
