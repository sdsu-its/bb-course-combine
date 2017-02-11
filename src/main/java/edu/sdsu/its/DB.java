package edu.sdsu.its;

import edu.sdsu.its.API.Models.Semester;
import edu.sdsu.its.API.Models.User;
import org.apache.log4j.Logger;
import org.jasypt.util.password.StrongPasswordEncryptor;

import java.sql.*;

/**
 * TODO JavaDoc
 *
 * @author Tom Paulus
 *         Created on 1/27/17.
 */
public class DB {
    private static final Logger LOGGER = Logger.getLogger(DB.class);
    private static final StrongPasswordEncryptor PASSWORD_ENCRYPTOR = new StrongPasswordEncryptor();


    private static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection(
                    Vault.getParam("db-url"),
                    Vault.getParam("db-user"),
                    Vault.getParam("db-password"));
        } catch (Exception e) {
            LOGGER.fatal("Problem Initializing DB Connection", e);
        }

        return connection;
    }

    /**
     * Execute a SQL Statement
     *
     * @param sql {@link String} SQL Statement to Execute
     */
    private static void executeStatement(final String sql) {
        Statement statement = null;
        Connection connection = getConnection();

        try {
            statement = connection.createStatement();
            LOGGER.info(String.format("Executing SQL Statement - \"%s\"", sql));
            statement.execute(sql);

        } catch (SQLException e) {
            LOGGER.error("Problem Executing Statement \"" + sql + "\"", e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                    connection.close();
                } catch (SQLException e) {
                    LOGGER.warn("Problem Closing Statement", e);
                }
            }
        }
    }

    // ====================== Semester ======================


    public static Semester[] getSemesters() {
        // TODO
//        return null;
        return null;
    }

    public static Semester getActiveSemester() {
        Connection connection = getConnection();
        Statement statement = null;
        Semester semester = null;

        try {
            statement = connection.createStatement();
            final String sql = "SELECT *\n" +
                    "FROM semester\n" +
                    "WHERE active = 1;";
            LOGGER.info(String.format("Executing SQL Query - \"%s\"", sql));
            ResultSet resultSet = statement.executeQuery(sql);

            if (resultSet.next()) {
                semester = new Semester(resultSet.getString("name"),
                        resultSet.getBoolean("active"));
            }

            resultSet.close();
        } catch (SQLException e) {
            LOGGER.error("Problem querying DB for UserID", e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                    connection.close();
                } catch (SQLException e) {
                    LOGGER.warn("Problem Closing Statement", e);
                }
            }
        }

        return semester;
    }

    public static void setActiveSemester(Semester s) {
        Statement statement = null;
        Connection connection = null;

        try {
            connection = getConnection();
            connection.setAutoCommit(false);
            statement = connection.createStatement();

            final String s1 = "UPDATE semester\n" +
                    "SET active = 0\n" +
                    "WHERE active = 1;";

            LOGGER.info(String.format("Executing SQL Statement as Batch - \"%s\"", s1));
            statement.addBatch(s1);

            final String s2 = "UPDATE semester\n" +
                    "SET active = 1\n" +
                    "WHERE name = '" + s.name + "';";
            LOGGER.info(String.format("Executing SQL Statement as Batch - \"%s\"", s2));
            statement.addBatch(s2);

            LOGGER.info("Executing Batch");
            statement.executeBatch();
        } catch (SQLException e) {
            LOGGER.error("Problem changing active semester", e);
        } finally {
            if (statement != null) {
                try {
                    connection.setAutoCommit(true);
                    statement.close();
                    connection.close();
                } catch (SQLException e) {
                    LOGGER.warn("Problem Closing Statement", e);
                }
            }
        }
    }

    // ====================== Users ======================

    public static User[] getUser(String restriction) {
        return null;
    }

    public static void createUser(User user) {
        // TODO
    }

    public static void updateUser(User user) {
        // TODO
    }

    public static void deleteUser(User user) {
        // TODO
    }

}
