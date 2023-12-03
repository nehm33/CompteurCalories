package db;

import beans.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDaoImpl extends UserDao {

    public UserDaoImpl(DaoFactory daoFactory) {
        super(daoFactory);
    }

    @Override
    public boolean create(User obj) throws DaoException {
        return false;
    }

    @Override
    public User get(String id) throws DaoException {
        try (Connection connection = daoFactory.getConnection()) {
            if (connection != null) {
                try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users WHERE username = ?;")) {
                    preparedStatement.setString(1, id);
                    ResultSet result = preparedStatement.executeQuery();
                    if (result.next()) {
                        User user = new User();
                        user.setUsername(result.getString("username"));
                        user.setPassword(result.getString("password"));
                        return user;
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
    public boolean update(User obj, String oldId) {
        return false;
    }

    @Override
    public boolean delete(User obj) {
        return false;
    }
}
