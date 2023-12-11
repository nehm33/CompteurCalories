package db;

import beans.Aliment;
import beans.Plat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;

public class PlatDaoImpl extends PlatDao {

    public PlatDaoImpl(DaoFactory daoFactory) {
        super(daoFactory);
    }

    @Override
    public boolean create(Plat obj) throws DaoException {
        try (Connection connection = daoFactory.getConnection()) {
            if (connection != null) {
                try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO plats VALUES (?, ?);")) {
                    preparedStatement.setString(1, obj.getNom());
                    preparedStatement.setDouble(2, obj.getNbPortions());
                    int nbRows = preparedStatement.executeUpdate();
                    if (nbRows > 0) {
                        for (Map.Entry<Aliment, Double> entry: obj.getAliments().entrySet()) {
                            if (!createAliment(obj, entry.getKey(), entry.getValue())) {
                                connection.rollback();
                                return false;
                            }
                        }
                        connection.commit();
                        return true;
                    }
                    return false;
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
                        if (getAliments(plat)) {
                            return plat;
                        }
                    }
                    return null;
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
                try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE plats SET nomPlat = ?, portions = ? WHERE nomPlat = ?")) {
                    preparedStatement.setString(1, obj.getNom());
                    preparedStatement.setDouble(2, obj.getNbPortions());
                    preparedStatement.setString(3, Objects.requireNonNullElse(oldObj, obj).getNom());
                    preparedStatement.executeUpdate();
                    for (Aliment aliment : obj.getAliments().keySet()) {
                        if (oldObj.hasAliment(aliment)) {
                            if (oldObj.getQuantiteOf(aliment) != obj.getQuantiteOf(aliment)) {
                                if (!updateAliment(obj, aliment, obj.getQuantiteOf(aliment))) {
                                    connection.rollback();
                                    return false;
                                }
                            }
                        } else {
                            if (!createAliment(obj, aliment, obj.getQuantiteOf(aliment))) {
                                connection.rollback();
                                return false;
                            }
                        }
                    }
                    connection.commit();
                    return true;
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
                for (Aliment aliment : obj.getAliments().keySet()) {
                    if (!deleteAliment(obj, aliment)) {
                        connection.rollback();
                        return false;
                    }
                }
                try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM plats WHERE nomPlat = ?;")) {
                    preparedStatement.setString(1, obj.getNom());
                    int nbRows = preparedStatement.executeUpdate();
                    if (nbRows > 0) {
                        connection.commit();
                        return true;
                    } else {
                        connection.rollback();
                        return false;
                    }
                }
            } else {
                throw new DaoException("Impossible de se connecter à la base de données");
            }
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    protected boolean createAliment(Plat plat, Aliment aliment, double quantite) {
        try (Connection connection = daoFactory.getConnection()) {
            if (connection != null) {
                try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO recettes VALUES (?, ?, ?);")) {
                    preparedStatement.setString(1, plat.getNom());
                    preparedStatement.setString(2, aliment.getNom());
                    preparedStatement.setDouble(3, quantite);
                    int nbRows = preparedStatement.executeUpdate();
                    return nbRows > 0;
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    protected boolean getAliments(Plat plat) {
        try (Connection connection = daoFactory.getConnection()) {
            if (connection != null) {
                try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM recettes WHERE nomPlat = ?;")) {
                    preparedStatement.setString(1, plat.getNom());
                    ResultSet result = preparedStatement.executeQuery();
                    while (result.next()) {
                        String nomAliment = result.getString("nomAliment");
                        double quantite = result.getDouble("quantite");
                        Aliment aliment = DaoFactory.getInstance().getAlimentDao().get(nomAliment);
                        if (aliment != null) {
                            plat.addAliment(aliment, quantite);
                        } else {
                            return false;
                        }
                    }
                    return true;
                } catch (DaoException e) {
                    return false;
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    protected boolean updateAliment(Plat plat, Aliment aliment, double quantite) {
        try (Connection connection = daoFactory.getConnection()) {
            if (connection != null) {
                connection.setAutoCommit(true);
                try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE recettes SET quantite = ? WHERE nomPlat = ? AND nomAliment = ?")) {
                    preparedStatement.setDouble(1, quantite);
                    preparedStatement.setString(2, plat.getNom());
                    preparedStatement.setString(3, aliment.getNom());
                    int nbRows = preparedStatement.executeUpdate();
                    return nbRows > 0;
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
    }

    @Override
    protected boolean deleteAliment(Plat plat, Aliment aliment) {
        try (Connection connection = daoFactory.getConnection()) {
            if (connection != null) {
                try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM recettes WHERE nomPlat = ? AND nomAliment = ?;")) {
                    preparedStatement.setString(1, plat.getNom());
                    preparedStatement.setString(2, aliment.getNom());
                    int nbRows = preparedStatement.executeUpdate();
                    return nbRows > 0;
                }
            } else {
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
    }
}
