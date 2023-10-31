package db;

public interface Dao<T> {

    boolean create(T obj) throws DaoException;
    T get(String id) throws DaoException;
    boolean update(T obj) throws DaoException;
    boolean delete(T obj) throws DaoException;
}
