package org.paysim.paysim.base;

import java.io.Serializable;
import java.util.ArrayList;

import org.paysim.paysim.output.Output;

public class Transaction implements Serializable {
    private static final long serialVersionUID = 1L;
    private final int step;
    private final String action;
    private final double amount;

    private final String nameOrig;
    private final double oldBalanceOrig, newBalanceOrig;

    private final String nameDest;
    private final double oldBalanceDest, newBalanceDest;

    private boolean isFraud = false;
    private boolean isFlaggedFraud = false;
    private boolean isUnauthorizedOverdraft = false;

    private String ipAddress;
    private String deviceType;
    private String browser;
    private String country;
    private String geoLocation;
    private String sessionId;
    private String loginTime;
    private String accountType;
    private String kycLevel;
    private String bank;
    private String fraudScenario;
    private String aiAlert;
    private String externalBlacklist;

    public Transaction(int step, String action, double amount, String nameOrig, double oldBalanceOrig,
                       double newBalanceOrig, String nameDest, double oldBalanceDest, double newBalanceDest,
                       String ipAddress, String deviceType, String browser, String country, String geoLocation,
                       String sessionId, String loginTime, String accountType, String kycLevel, String bank,
                       String fraudScenario, String aiAlert, String externalBlacklist) {
        this.step = step;
        this.action = action;
        this.amount = amount;
        this.nameOrig = nameOrig;
        this.oldBalanceOrig = oldBalanceOrig;
        this.newBalanceOrig = newBalanceOrig;
        this.nameDest = nameDest;
        this.oldBalanceDest = oldBalanceDest;
        this.newBalanceDest = newBalanceDest;
        this.ipAddress = ipAddress;
        this.deviceType = deviceType;
        this.browser = browser;
        this.country = country;
        this.geoLocation = geoLocation;
        this.sessionId = sessionId;
        this.loginTime = loginTime;
        this.accountType = accountType;
        this.kycLevel = kycLevel;
        this.bank = bank;
        this.fraudScenario = fraudScenario;
        this.aiAlert = aiAlert;
        this.externalBlacklist = externalBlacklist;
    }

    public Transaction(int step, String action, double amount, String nameOrig, double oldBalanceOrig,
                       double newBalanceOrig, String nameDest, double oldBalanceDest, double newBalanceDest) {
        this(step, action, amount, nameOrig, oldBalanceOrig, newBalanceOrig, nameDest, oldBalanceDest, newBalanceDest,
            "", "", "", "", "", "", "", "", "", "", "", "");
    }

    public boolean isFailedTransaction(){
        return isFlaggedFraud || isUnauthorizedOverdraft;
    }

    public void setFlaggedFraud(boolean isFlaggedFraud) {
        this.isFlaggedFraud = isFlaggedFraud;
    }

    public void setFraud(boolean isFraud) {
        this.isFraud = isFraud;
    }
    
    public void setUnauthorizedOverdraft(boolean isUnauthorizedOverdraft) {
        this.isUnauthorizedOverdraft = isUnauthorizedOverdraft;
    }

    public boolean isFlaggedFraud() {
        return isFlaggedFraud;
    }

    public boolean isFraud() {
        return isFraud;
    }

    public int getStep() {
        return step;
    }

    public String getAction() {
        return action;
    }

    public double getAmount() {
        return amount;
    }

    public String getNameOrig() {
        return nameOrig;
    }

    public double getOldBalanceOrig() {
        return oldBalanceOrig;
    }

    public double getNewBalanceOrig() {
        return newBalanceOrig;
    }

    public String getNameDest() {
        return nameDest;
    }

    public double getOldBalanceDest() {
        return oldBalanceDest;
    }

    public double getNewBalanceDest() {
        return newBalanceDest;
    }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public String getDeviceType() { return deviceType; }
    public void setDeviceType(String deviceType) { this.deviceType = deviceType; }
    public String getBrowser() { return browser; }
    public void setBrowser(String browser) { this.browser = browser; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public String getGeoLocation() { return geoLocation; }
    public void setGeoLocation(String geoLocation) { this.geoLocation = geoLocation; }
    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }
    public String getLoginTime() { return loginTime; }
    public void setLoginTime(String loginTime) { this.loginTime = loginTime; }
    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }
    public String getKycLevel() { return kycLevel; }
    public void setKycLevel(String kycLevel) { this.kycLevel = kycLevel; }
    public String getBank() { return bank; }
    public void setBank(String bank) { this.bank = bank; }
    public String getFraudScenario() { return fraudScenario; }
    public void setFraudScenario(String fraudScenario) { this.fraudScenario = fraudScenario; }
    public String getAiAlert() { return aiAlert; }
    public void setAiAlert(String aiAlert) { this.aiAlert = aiAlert; }
    public String getExternalBlacklist() { return externalBlacklist; }
    public void setExternalBlacklist(String externalBlacklist) { this.externalBlacklist = externalBlacklist; }

    @Override
    public String toString(){
        ArrayList<String> properties = new ArrayList<>();

        properties.add(String.valueOf(step));
        properties.add(action);
        properties.add(Output.fastFormatDouble(Output.PRECISION_OUTPUT, amount));
        properties.add(nameOrig);
        properties.add(Output.fastFormatDouble(Output.PRECISION_OUTPUT, oldBalanceOrig));
        properties.add(Output.fastFormatDouble(Output.PRECISION_OUTPUT, newBalanceOrig));
        properties.add(nameDest);
        properties.add(Output.fastFormatDouble(Output.PRECISION_OUTPUT, oldBalanceDest));
        properties.add(Output.fastFormatDouble(Output.PRECISION_OUTPUT, newBalanceDest));
        properties.add(Output.formatBoolean(isFraud));
        properties.add(Output.formatBoolean(isFlaggedFraud));
        properties.add(Output.formatBoolean(isUnauthorizedOverdraft));
        properties.add(ipAddress);
        properties.add(deviceType);
        properties.add(browser);
        properties.add(country);
        properties.add(geoLocation);
        properties.add(sessionId);
        properties.add(loginTime);
        properties.add(accountType);
        properties.add(kycLevel);
        properties.add(bank);
        properties.add(fraudScenario);
        properties.add(aiAlert);
        properties.add(externalBlacklist);

        return String.join(Output.OUTPUT_SEPARATOR, properties);
    }
}
