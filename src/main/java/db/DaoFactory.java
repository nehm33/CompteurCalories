package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DaoFactory {
    private final String url;
    private final String username;
    private final String password;

    DaoFactory(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public static DaoFactory getInstance() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ignored) {

        }

        return new DaoFactory(
                "jdbc:postgresql://localhost:5432/compteur_calories", "postgres", "ornithorynque");
    }

    public Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            connection.setAutoCommit(false);
            return connection;
        } catch (SQLException e) {
            return null;
        }
    }

    // Récupération du Dao
    public UserDao getUserDao() {
        return new UserDaoImpl(this);
    }

    public CodeBarreDao getCodeBarreDao() {return new CodeBarreDaoImpl(this); }

    public AlimentDao getAlimentDao() {
        return new AlimentDaoImpl(this);
    }

    public PlatDao getPlatDao() {
        return new PlatDaoImpl(this);
    }

    public JournalDao getJournalDao() {
        return new JournalDaoImpl(this);
    }
}