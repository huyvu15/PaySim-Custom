package org.paysim.paysim.utils;

import org.paysim.paysim.base.Transaction;

import java.sql.DriverManager;

import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Connection;


public class DatabaseHandler {

    private Connection con = null;
    private final String url, user, password;

    public DatabaseHandler(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
        try {
            con = DriverManager.getConnection(this.url, this.user, this.password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insert(String simulatorName, Transaction trans) {
        try {
            String sql = "INSERT INTO org.paysim.paysim.paysimLog (logName, pType, pAmount, cliFrom, pOldBalanceFrom, pNewBalanceFrom, " +
                    "cliTo, pOldBalanceTo, pNewBalanceTo, isFraud, isFlaggedFraud, step, isUnauthorizedOverdraft, " +
                    "ipAddress, deviceType, browser, country, geoLocation, sessionId, loginTime, accountType, " +
                    "kycLevel, bank, fraudScenario, aiAlert, externalBlacklist) " +
                    "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, simulatorName);
            st.setString(2, trans.getAction());
            st.setDouble(3, trans.getAmount());
            st.setString(4, trans.getNameOrig());
            st.setDouble(5, trans.getOldBalanceOrig());
            st.setDouble(6, trans.getNewBalanceOrig());
            st.setString(7, trans.getNameDest());
            st.setDouble(8, trans.getOldBalanceDest());
            st.setDouble(9, trans.getNewBalanceDest());
            st.setBoolean(10, trans.isFraud());
            st.setBoolean(11, trans.isFlaggedFraud());
            st.setLong(12, trans.getStep());
            st.setBoolean(13, trans.isUnauthorizedOverdraft());
            st.setString(14, trans.getIpAddress());
            st.setString(15, trans.getDeviceType());
            st.setString(16, trans.getBrowser());
            st.setString(17, trans.getCountry());
            st.setString(18, trans.getGeoLocation());
            st.setString(19, trans.getSessionId());
            st.setString(20, trans.getLoginTime());
            st.setString(21, trans.getAccountType());
            st.setString(22, trans.getKycLevel());
            st.setString(23, trans.getBank());
            st.setString(24, trans.getFraudScenario());
            st.setString(25, trans.getAiAlert());
            st.setString(26, trans.getExternalBlacklist());
            st.executeUpdate();
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void close() {
        try {
            this.con.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}

class DBase {
    public DBase() {
    }

    public Connection connect(String db_connect_str, String db_userid,
                              String db_password) {
        Connection conn;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();

            conn = DriverManager.getConnection(db_connect_str, db_userid,
                    db_password);

        } catch (Exception e) {
            e.printStackTrace();
            conn = null;
        }

        return conn;
    }

}
