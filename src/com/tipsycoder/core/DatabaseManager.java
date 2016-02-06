package com.tipsycoder.core;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.tipsycoder.helper.DatabaseHelper;
import com.tipsycoder.helper.Kit;
import com.tipsycoder.helper.VehicleHandler;
import com.tipsycoder.ui.MainWindow;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by TipsyCoder on 2/5/15.
 */
public class DatabaseManager {
    private String url = "jdbc:mysql://localhost/fleet_management";//"jdbc:mysql://sql3.freesqldatabase.com:3306/sql369964";
    private String user = "si";//sql369964";//"si";
    private String password = "";//xP3%fS1!";
    private String driver = "com.mysql.jdbc.Driver";
    public MainWindow mainWindow;

    private String logUsername;

    private static DatabaseManager instance = null;

    protected DatabaseManager() {
        // Exists only to defeat instantiation.
    }

    public static DatabaseManager getInstance() {
        if(instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private Connection connectDatabase() {
        Connection connection = null;
        try {
            Class.forName(driver).newInstance();
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("MySQL Now Connected");
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DatabaseManager.class.getName());
            lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return connection;
    }

    public void closeConnection(Connection con) {
        closeConnection(con, null, null);
    }

    public void closeConnection(Connection con, Statement st, ResultSet rs) {
        try {
            if(st != null)
                st.close();
            if(rs != null)
                rs.close();
            if (con != null) {
                con.close();
                System.out.println("MySQL Now Closed");
            } else {
                System.err.println("MySQL Already Closed");
            }
        } catch (SQLException ex) {
            Logger lgr = Logger.getLogger(DatabaseManager.class.getName());
            lgr.log(Level.WARNING, ex.getMessage(), ex);
        }
    }

    public void insertSQL(String sqlQuery, String notes) {
        Connection con = connectDatabase();

        if(!isServerAlive(con)) return;

        System.out.println(sqlQuery);
        try {
            Statement stmt = con.createStatement();
            int result = stmt.executeUpdate( sqlQuery );
            if(result == 1) updateAdminLog(notes);
            System.err.println("Update Code: " + result);
            stmt.close();

        } catch (MySQLIntegrityConstraintViolationException e) {
            Kit.warningBox("Error", "Failed Because Of Constraint Issue.", mainWindow);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        closeConnection(con);

    }

    public boolean isExist(String primaryKey, String tableName, String fieldName) {
        String sql = "SELECT * FROM " + tableName + " WHERE " + fieldName + " = '" + primaryKey + "'";
        return commonExist(sql);
    }

    public boolean isExist(Integer primaryKey, String tableName, String fieldName) {
        String sql = "SELECT * FROM " + tableName + " WHERE " + fieldName + " = " + primaryKey;
        return commonExist(sql);

    }

    public boolean commonExist(String sql) {
        Connection con = connectDatabase();

        Statement st = null;
        ResultSet rs = null;

        boolean exist = false;

        if(!isServerAlive(con)) return false;

        try {
            st = con.createStatement();
            rs = st.executeQuery(sql);

            exist = rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        closeConnection(con, st, rs);

        return exist;
    }

    public boolean loginAdmin(String username, char[] password) {
        Connection con = connectDatabase();
        Statement st = null;
        ResultSet rs = null;

        if(!isServerAlive(con)) System.exit(0);

        try {
            st = con.createStatement();
            rs = st.executeQuery("SELECT passw FROM Admin WHERE username = '" + username + "'");

            if(rs.next()) {
                System.out.println("Password: " + rs.getString("passw").toCharArray());
                if(isPasswordCorrect(rs.getObject("passw").toString().toCharArray(), password)) {
                    logUsername = username;
                    st.executeUpdate("UPDATE Admin SET lastLoginDate = NOW() WHERE username = '" + username + "'");
                    updateAdminLog("Logged In.");
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        closeConnection(con, st, rs);
        return false;
    }

    private static boolean isPasswordCorrect(char[] toBeCheckedAgainst, char[] inputted) {
        boolean isCorrect = true;

        if (inputted.length != toBeCheckedAgainst.length) {
            isCorrect = false;
        } else {
            isCorrect = Arrays.equals(inputted, toBeCheckedAgainst);
        }

        //Zero out the password.
        Arrays.fill(toBeCheckedAgainst, '0');
        Arrays.fill(inputted, '0');

        return isCorrect;
    }

    private boolean isServerAlive(Connection con) {
        if(con == null) {
            Kit.warningBox("Server Error", "Could Not Establish A Connection With The Server", null);
            return false;
        }
        return true;
    }

    public ArrayList<Object> cbQuery(String query, String columnName) {
        Connection con = connectDatabase();
        ArrayList<Object> arrayList = new ArrayList<Object>();

        if(!isServerAlive(con)) return null;

        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            while (rs.next()) {
                String value = rs.getString(columnName);
                arrayList.add(value);
            }

            st.close();
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        closeConnection(con);
        return arrayList;
    }

    public DatabaseHelper runQuery(String sqlQuery, String notes) {
        Connection con = connectDatabase();

        if(!isServerAlive(con)) return null;

        DatabaseHelper dsh = new DatabaseHelper();
        int columns;

        System.out.println(sqlQuery);

        try {
            dsh.setStatement(con.createStatement());
            dsh.setResultSet(dsh.getStatement().executeQuery(sqlQuery));
            dsh.setMetaData(dsh.getResultSet().getMetaData());
            columns = dsh.getMetaData().getColumnCount();

            //  Get column names

            for (int i = 1; i <= columns; i++)
            {
                dsh.getColumnNames().addElement(dsh.getMetaData().getColumnName(i));
            }

            //  Get row data

            while (dsh.getResultSet().next())
            {
                Vector<Object> row = new Vector<Object>(columns);

                for (int i = 1; i <= columns; i++)
                {
                    row.addElement( dsh.getResultSet().getObject(i) );
                }

                dsh.getData().addElement( row );
            }

            updateAdminLog(notes);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        dsh.destroySQL();
        closeConnection(con);

        return dsh;
    }

    private void updateAdminLog(String notes) {
        if(notes.isEmpty())
            return;

        Connection con = connectDatabase();

        if(!isServerAlive(con)) return;

        Statement st = null;

        try {
            st = con.createStatement();
            st.executeUpdate("INSERT INTO Admin_Logs VALUES ('" + logUsername + "', NOW(), '" + notes + "')");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        closeConnection(con, st, null);
    }

    public List getVehicleInfo(String chassisNum) {
        DatabaseHelper dsh = runQuery("SELECT vCondition, gasAmount, color FROM Vehicle WHERE chassisNum = " + chassisNum, "Select All From Table Vehicle");

        if(dsh == null)
            return null;

        System.out.println(dsh.getData().firstElement().toString());

        return new ArrayList((Vector) dsh.getData().firstElement());
    }

    public void AddFleet(Fleet fleet, List<VehicleHandler> vehicleHandlers) {
        Connection con = connectDatabase();
        if(!isServerAlive(con)) return;

        Statement st;

        try {
            st = con.createStatement();
            st.executeUpdate("INSERT INTO Fleet VALUES ('" + fleet.getAlias() + "','" + fleet.getArea() +
                    "', '" + fleet.getDivision() + "', '" + fleet.getStation() + "', " + "NOW(), " + "NOW(), '" + logUsername + "')");

            String sql_FV = "INSERT INTO Fleet_Vehicle_Map (alias, chassisNum)" +
                    "VALUES (?, ?)";

            String sql_VO = "INSERT INTO Vehicle_Officer_Map (chassisNum, badgeNum)" +
                    "VALUES (?, ?)";

            PreparedStatement pStmFleetVehicle = con.prepareStatement(sql_FV, Statement.RETURN_GENERATED_KEYS);
            PreparedStatement pStmVehicleOfficer = con.prepareStatement(sql_VO, Statement.RETURN_GENERATED_KEYS);
            con.setAutoCommit(false);

            for (VehicleHandler handler : vehicleHandlers) {
                pStmFleetVehicle.setString(1, fleet.getAlias());
                pStmFleetVehicle.setInt(2, Integer.parseInt(handler.getChassisNum()));


                for (String id : handler.getOfficersID()) {
                    pStmVehicleOfficer.setInt(1, Integer.parseInt(handler.getChassisNum()));
                    pStmVehicleOfficer.setInt(2, Integer.parseInt(id));
                    pStmVehicleOfficer.addBatch();
                }

                pStmFleetVehicle.addBatch();
            }

            pStmFleetVehicle.executeBatch();
            pStmVehicleOfficer.executeBatch();
            con.commit();

            closeConnection(con, st, null);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteFleet(String alias) {
        Connection con = connectDatabase();
        if(!isServerAlive(con)) return;

        try {
            Statement st = con.createStatement();

            st.addBatch("DELETE FROM Fleet_Vehicle_Map WHERE alias = '" + alias + "'");
            st.addBatch("DELETE FROM Fleet WHERE alias = '" + alias + "'");
            st.executeBatch();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        closeConnection(con);
    }

    public void logOffAdmin() {
        updateAdminLog("Logs Off");
    }

    public String getLogUsername() {
        return logUsername;
    }
}
