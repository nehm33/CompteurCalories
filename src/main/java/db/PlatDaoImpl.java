package db;

import beans.Aliment;
import beans.Plat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

public class PlatDaoImpl extends PlatDao {

    public PlatDaoImpl(DaoFactory daoFactory) {
        super(daoFactory);
    }

    @Override
    public boolean create(Plat obj) throws DaoException {
        try (Connection connection = daoFactory.getConnection()) {
            if (connection != null) {
                connection.setAutoCommit(true);
                try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO plats VALUES (?, ?);")) {
                    preparedStatement.setString(1, obj.getNom());
                    preparedStatement.setDouble(2, obj.getNbPortions());
                    int nbRows = preparedStatement.executeUpdate();
                    return nbRows > 0;
                }
            } else {
                throw new DaoException("Impossible de se connecter à la base de données");
            }
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public Plat get(String id) throws DaoException {
        try (Connection connection = daoFactory.getConnection()) {
            if (connection != null) {
                try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM plats WHERE nomPlat = ?;")) {
                    preparedStatement.setString(1, id);
                    ResultSet result = preparedStatement.executeQuery();
                    if (result.next()) {
                        Plat plat = new Plat();
                        plat.setNom(result.getString("nomPlat"));
                        plat.setNbPortions(result.getDouble("portions"));
                        return plat;
                    } else {
                        return null;
                    }
                }
            } else {
                throw new DaoException("Impossible de se connecter à la base de données");
            }
        } catch (SQLException e) {
            throw new DaoException("Impossible de communiquer avec la base de données");
        }
    }

    @Override
    public boolean update(Plat obj, Plat oldObj) throws DaoException {
        try (Connection connection = daoFactory.getConnection()) {
            if (connection != null) {
                connection.setAutoCommit(true);
                try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE plats SET nomPlat = ?, portions = ? WHERE nomPlat = ?")) {
                    preparedStatement.setString(1, obj.getNom());
                    preparedStatement.setDouble(2, obj.getNbPortions());
                    preparedStatement.setString(3, Objects.requireNonNullElse(oldObj, obj).getNom());
                    int nbRows = preparedStatement.executeUpdate();
                    return nbRows > 0;
                }
            } else {
                throw new DaoException("Impossible de se connecter à la base de données");
            }
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean delete(Plat obj) throws DaoException {
        try (Connection connection = daoFactory.getConnection()) {
            if (connection != null) {
                try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM plats WHERE nomPlat = ?;")) {
                    preparedStatement.setString(1, obj.getNom());
                    int nbRows = preparedStatement.executeUpdate();
                    return nbRows > 0;
                }
            } else {
                throw new DaoException("Impossible de se connecter à la base de données");
            }
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    public boolean createAliment(Plat plat, Aliment aliment) throws DaoException {
        return false;
    }

    @Override
    public boolean getAliment(Plat plat, String idAliment) throws DaoException {
        return false;
    }

    @Override
    public boolean update(Plat plat, Aliment aliment) throws DaoException {
        return false;
    }

    @Override
    public boolean delete(Plat plat, Aliment aliment) throws DaoException {
        return false;
    }
}
