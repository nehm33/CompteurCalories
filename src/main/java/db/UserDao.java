package db;

import beans.User;

public abstract class UserDao extends Dao<User> {

    public UserDao(DaoFactory daoFactory) {
        this.daoFactory = daoFactory;
    }
}
