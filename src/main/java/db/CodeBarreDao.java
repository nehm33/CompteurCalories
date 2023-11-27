package db;

import beans.CodeBarre;

public abstract class CodeBarreDao extends Dao<CodeBarre> {

    public CodeBarreDao(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
}
