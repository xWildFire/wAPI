package pl.wildfire.api.db;

import java.sql.Connection;
import java.sql.ResultSet;

public interface Db {

    public Connection getConnection();

    public boolean connect();

    public void disconnect();

    public boolean reconnect();

    public boolean isConnected();

    public ResultSet query(String query);

    public void update(String update);

    public void updateNow(String update);

}