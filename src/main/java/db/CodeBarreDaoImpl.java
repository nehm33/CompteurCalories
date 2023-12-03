package db;

import beans.CodeBarre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CodeBarreDaoImpl extends CodeBarreDao {

    public CodeBarreDaoImpl(DaoFactory daoFactory) {
        super(daoFactory);
    }

    @Override
    public boolean create(CodeBarre obj) throws DaoException {
        try (Connection connection = daoFactory.getConnection()) {
            if (connection != null) {
                connection.setAutoCommit(true);
                try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO codeBarre VALUES (?, ?, ?);")) {
                    preparedStatement.setString(1, obj.getCodeBarre());
                    preparedStatement.setString(2, obj.getAliment());
                    preparedStatement.setString(3, obj.getMarque());
                    int nbRows = preparedStatement.executeUpdate();
                    if (nbRows > 0) {
                        return true;
                    } else {
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
    public CodeBarre get(String id) throws DaoException {
        try (Connection connection = daoFactory.getConnection()) {
            if (connection != null) {
                try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM codeBarre WHERE code_barre = ?;")) {
                    preparedStatement.setString(1, id);
                    ResultSet result = preparedStatement.executeQuery();
                    if (result.next()) {
                        CodeBarre codeBarre = new CodeBarre();
                        codeBarre.setCodeBarre(result.getString("code_barre"));
                        codeBarre.setAliment(result.getString("nomAliment"));
                        codeBarre.setMarque(result.getString("marque"));
                        return codeBarre;
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
    public boolean update(CodeBarre obj, String oldId) throws DaoException {
        try (Connection connection = daoFactory.getConnection()) {
            if (connection != null) {
                connection.setAutoCommit(true);
                try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE codeBarre SET code_barre = ?, nomAliment = ?, marque = ? WHERE code_barre = ?")) {
                    preparedStatement.setString(1, obj.getCodeBarre());
                    preparedStatement.setString(2, obj.getAliment());
                    preparedStatement.setString(3, obj.getMarque());
                    if (oldId != null && !oldId.isEmpty()) {
                        preparedStatement.setString(4, oldId);
                    } else {
                        preparedStatement.setString(4, obj.getCodeBarre());
                    }
                    int nbRows = preparedStatement.executeUpdate();
                    if (nbRows > 0) {
                        return true;
                    } else {
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
    public boolean delete(CodeBarre obj) throws DaoException {
        try (Connection connection = daoFactory.getConnection()) {
            if (connection != null) {
                try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM codeBarre WHERE code_barre = ?;")) {
                    preparedStatement.setString(1, obj.getCodeBarre());
                    int nbRows = preparedStatement.executeUpdate();
                    if (nbRows > 0) {
                        return true;
                    } else {
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
}
