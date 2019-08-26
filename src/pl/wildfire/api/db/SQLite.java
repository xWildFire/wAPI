package pl.wildfire.api.db;

import pl.wildfire.api.Msg;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SQLite implements Db {
    private final File file;
    private final Executor executor;
    private Connection conn;

    public SQLite(File file) {
        this.file = file;

        this.executor = Executors.newSingleThreadExecutor();
    }

    public boolean connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            this.conn = DriverManager.getConnection("jdbc:sqlite:" + file.getAbsolutePath());
            return true;
        } catch (Exception e) {
            Msg.exception(e);
        }
        return false;
    }

    public void update(final String update) {
        if (!isConnected())
            reconnect();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    conn.createStatement().executeUpdate(update.replace("IGNORE", "OR REPLACE").replace("AUTO_INCREMENT", "AUTOINCREMENT"));
                } catch (SQLException e) {
                    Msg.exception(e);
                }
            }
        });
    }

    public void updateNow(final String update) {
        if (!isConnected()) {
            reconnect();
        }
        try {
            conn.createStatement().executeUpdate(update.replace("IGNORE", "OR REPLACE").replace("AUTO_INCREMENT", "AUTOINCREMENT"));
        } catch (SQLException e) {
            Msg.exception(e);
        }
    }

    public void disconnect() {
        if (this.conn != null) {
            try {
                this.conn.close();
            } catch (SQLException e) {

            }
        }
    }

    public boolean reconnect() {
        return this.connect();
    }

    public boolean isConnected() {
        try {
            return !(this.conn.isClosed()) || (this.conn == null);
        } catch (SQLException e) {

        }
        return false;
    }

    public ResultSet query(String query) {
        if (!isConnected())
            reconnect();
        try {
            return conn.createStatement().executeQuery(query);
        } catch (SQLException e) {
            Msg.exception(e);
        }
        return null;
    }

    public Connection getConnection() {
        return this.conn;
    }

}
