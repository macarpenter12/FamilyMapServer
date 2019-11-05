package DAO;

import exception.DataAccessException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private Connection conn;

    public Connection getConnection() throws DataAccessException {
        if (conn == null) {
            return conn;
        }
        else {
            return openConnection();
        }
    }

    public Connection openConnection() throws DataAccessException {
        try {
            final String CONNECTION_URL = "jdbc:sqlite:familymap.sqlite";
            conn = DriverManager.getConnection(CONNECTION_URL);
            conn.setAutoCommit(false);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            throw new DataAccessException("Unable to open connection to database");
        }

        return conn;
    }

    public void closeConnection(boolean commit) throws DataAccessException {
        try {
            // Commit changes to database (save changes)
            if (commit) {
                conn.commit();
            }
            // Rollback changes made during connection (don't save changes)
            else {
                conn.rollback();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            throw new DataAccessException("Unable to close database connection");
        }
    }

    public void createTables() throws DataAccessException {
        // Create the database tables, if they don't exist already
        try (Statement stmt = conn.createStatement()) {
            // Person table
            String sql = "CREATE TABLE IF NOT EXISTS person_table " +
                    "(" +
                    "personID text not null unique, " +
                    "associatedUsername text, " +
                    "firstName text not null, " +
                    "lastName text not null, " +
                    "gender text not null, " +
                    "fatherID text, " +
                    "motherID text, " +
                    "spouseID text, " +
                    "primary key (personID), " +
                    "foreign key (associatedUsername) references user_table(userName)" +
                    ")";
            stmt.executeUpdate(sql);

            // Event table
            sql = "CREATE TABLE IF NOT EXISTS event_table " +
                    "(" +
                    "eventType text not null, " +
                    "personID text not null, " +
                    "city text not null, " +
                    "country text not null, " +
                    "latitude float not null, " +
                    "longitude float not null, " +
                    "year int not null, " +
                    "eventID text not null unique, " +
                    "associatedUsername text not null, " +
//                    "primary key (eventID), " +
                    "foreign key (AssociatedUsername) references user_table(userName), " +
                    "foreign key (personID) references person_table(personID)" +
                    ")";
            stmt.executeUpdate(sql);

            // User table
            sql = "CREATE TABLE IF NOT EXISTS user_table " +
                    "(" +
                    "userName text not null unique, " +
                    "password text not null, " +
                    "email text not null, " +
                    "firstName text not null, " +
                    "lastName text not null, " +
                    "gender text not null, " +
                    "personID text not null, " +
                    "primary key (userName)" +
                    ")";
            stmt.executeUpdate(sql);

            // AuthToken table
            sql = "CREATE TABLE IF NOT EXISTS authtoken_table " +
                    "(" +
                    "authToken text not null unique, " +
                    "userName text not null, " +
                    "primary key (authToken), " +
                    "foreign key (userName) references user_table(userName)" +
                    ")";
            stmt.executeUpdate(sql);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            throw new DataAccessException("SQL Error encountered while creating tables");
        }
    }

    public void clearTables() throws DataAccessException {
        try (Statement stmt = conn.createStatement()) {
            String sql = "DELETE FROM person_table";
            stmt.executeUpdate(sql);
            sql = "DELETE FROM event_table";
            stmt.executeUpdate(sql);
            sql = "DELETE FROM user_table";
            stmt.executeUpdate(sql);
            sql = "DELETE FROM authtoken_table";
            stmt.executeUpdate(sql);
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            throw new DataAccessException("SQL Error encountered while clearing tables");
        }
    }
}
