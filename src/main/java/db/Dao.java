package db;

public abstract class Dao<T> {
    protected DaoFactory daoFactory;


    public abstract boolean create(T obj) throws DaoException;
    public abstract T get(String id) throws DaoException;
    public abstract boolean update(T obj, String oldId) throws DaoException;
    public abstract boolean delete(T obj) throws DaoException;
}
