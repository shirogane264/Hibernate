package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    public UserDaoJDBCImpl() {
    }

    private Statement createStatement() throws SQLException {
        return Util.getConnection().createStatement();
    }

    public void createUsersTable() {
        try (Statement statement = createStatement()) {
            statement.execute("CREATE TABLE if not exists UsersTable (\n" +
                    "  id BIGINT NOT NULL AUTO_INCREMENT,\n" +
                    "  name VARCHAR(45) NULL,\n" +
                    "  lastName VARCHAR(45) NULL,\n" +
                    "  age TINYINT NULL,\n" +
                    "  PRIMARY KEY (id),\n" +
                    "  UNIQUE INDEX id_UNIQUE (id ASC) VISIBLE);");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void dropUsersTable() {
        try (Statement statement = createStatement()) {
            statement.execute("DROP TABLE if EXISTS UsersTable;");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement preparedStatement = Util.getConnection().prepareStatement("INSERT INTO userstable (name, lastname, age) VALUES (?, ?, ?)")) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
            System.out.println("User с именем – " + name + " добавлен в базу данных");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeUserById(long id) {
        try (Statement statement = createStatement()) {
            statement.executeUpdate("DELETE FROM userstable WHERE id =" + id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUsers() {
        try (Statement statement = createStatement()) {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM userstable");
            List<User> list = new ArrayList<>();
            while (resultSet.next()) {
                User user = new User(resultSet.getLong("id"), resultSet.getString("name"), resultSet.getString("lastName"), resultSet.getByte("age"));
                list.add(user);
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void cleanUsersTable() {
        try (Statement statement = createStatement()) {
            statement.executeUpdate("DELETE FROM userstable");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
