package de.raidcraft.rcmultiworld.tables;

import de.raidcraft.RaidCraft;
import de.raidcraft.api.database.Table;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Philip Urban
 */
public class WorldInfoTable extends Table {

    public WorldInfoTable() {

        super("worlds", "rcmultiworld_");
    }

    @Override
    public void createTable() {

        try {
            executeUpdate(
                    "CREATE TABLE `" + getTableName() + "` (" +
                            "`id` INT NOT NULL AUTO_INCREMENT, " +
                            "`server` VARCHAR( 32 ) NOT NULL, " +
                            "`world` VARCHAR( 32 ) NOT NULL, " +
                            "PRIMARY KEY ( `id` )" +
                            ")");
        } catch (SQLException e) {
            RaidCraft.LOGGER.warning(e.getMessage());
        }
    }

    public void addWorld(String server, String world) {

        deleteWorld(world);
        try {
            String query = "INSERT INTO " + getTableName() + " (server, world) " +
                    "VALUES (" +
                    "'" + server + "'" + "," +
                    "'" + world + "'" +
                    ");";

            executeUpdate(query);
        } catch (SQLException e) {
            RaidCraft.LOGGER.warning(e.getMessage());
            e.printStackTrace();
        }
    }

    public String getWorldHost(String world) {

        try {
            ResultSet resultSet = executeQuery(
                    "SELECT * FROM " + getTableName() + " WHERE world = '" + world + "'");

            while (resultSet.next()) {
                String server = resultSet.getString("server");
                resultSet.close();
                return server;
            }
            resultSet.close();
        } catch (SQLException e) {
            RaidCraft.LOGGER.warning(e.getMessage());
        }
        return null;
    }

    public void deleteWorld(String world) {

        try {
            executeUpdate(
                    "DELETE FROM " + getTableName() + " WHERE world = '" + world + "'");
        } catch (SQLException e) {
            RaidCraft.LOGGER.warning(e.getMessage());
            e.printStackTrace();
        }
    }
}
