package pl.wildfire.api.db;

import pl.wildfire.api.Msg;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MySQL implements Db {

    private final String host, user, pass, name;
    private final int port;
    private final Executor executor;
    private Connection conn;

    public MySQL(String host, int port, String user, String pass, String name) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.pass = pass;
        this.name = name;

        this.executor = Executors.newSingleThreadExecutor();
    }

    public MySQL(String host, String user, String pass, String name) {
        this(host, 3306, user, pass, name);
    }

    public boolean connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.conn = DriverManager.getConnection("jdbc:mysql://" + this.host + ":" + this.port + "/" + this.name, this.user, this.pass);
            return true;
        } catch (Exception e) {
            Msg.exception(e);
        }
        return false;
    }

    public void update(final String update) {
        if (!isConnected()) {
            reconnect();
        }
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    conn.createStatement().executeUpdate(update.replace("OR REPLACE", "IGNORE").replace("AUTOINCREMENT", "AUTO_INCREMENT"));
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
            conn.createStatement().executeUpdate(update.replace("OR REPLACE", "IGNORE").replace("AUTOINCREMENT", "AUTO_INCREMENT"));
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
        if (!isConnected()) {
            reconnect();
        }
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
