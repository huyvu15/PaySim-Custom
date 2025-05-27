package org.paysim.paysim.base;

import java.io.Serializable;
import java.util.ArrayList;

import org.paysim.paysim.output.Output;
import org.paysim.paysim.services.FraudDetectionService;

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
        
        // Apply comprehensive business logic after construction
        applyAdvancedFraudDetectionLogic();
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

    public boolean isUnauthorizedOverdraft() { return isUnauthorizedOverdraft; }

    // Enhanced business logic methods using FraudDetectionService
    private void applyAdvancedFraudDetectionLogic() {
        // 1. Detect unauthorized overdraft with advanced logic
        this.isUnauthorizedOverdraft = detectAdvancedUnauthorizedOverdraft();
        
        // 2. Use FraudDetectionService for comprehensive fraud detection
        boolean fraudDetected = FraudDetectionService.detectFraud(this);
        if (fraudDetected && !this.isFraud) {
            this.isFraud = true;
        }
        
        // 3. AI-based fraud detection with risk scoring
        boolean aiDetectedFraud = FraudDetectionService.detectFraud(this);
        this.aiAlert = aiDetectedFraud ? "1" : "0";
        
        // 4. External blacklist check using service
        boolean isBlacklisted = FraudDetectionService.isBlacklisted(this);
        this.externalBlacklist = isBlacklisted ? "1" : "0";
        
        // 5. Determine comprehensive fraud scenario
        this.fraudScenario = FraudDetectionService.determineFraudScenario(this);
        
        // 6. Apply additional business rules
        applyAdditionalBusinessRules();
    }

    private boolean detectAdvancedUnauthorizedOverdraft() {
        // Enhanced overdraft detection logic
        boolean hasOverdraft = newBalanceOrig < 0 && oldBalanceOrig >= 0;
        
        if (!hasOverdraft) return false;
        
        // Business rules for authorized overdraft
        boolean isHighKyc = "3".equals(kycLevel);
        boolean isBusinessAccount = "business".equals(accountType) || "premium".equals(accountType);
        boolean isSmallAmount = amount < 50000; // Small amounts may be auto-authorized
        
        // Overdraft is authorized if high KYC + business account OR small amount
        boolean isAuthorized = (isHighKyc && isBusinessAccount) || isSmallAmount;
        
        return !isAuthorized;
    }

    private void applyAdditionalBusinessRules() {
        // Flag suspicious patterns
        if (isSuspiciousPattern()) {
            this.isFlaggedFraud = true;
        }
        
        // Cross-validate fraud indicators
        if (isMultipleFraudIndicators()) {
            this.isFraud = true;
            this.aiAlert = "1";
        }
    }

    private boolean isSuspiciousPattern() {
        // Check for suspicious transaction patterns
        boolean isRoundAmount = amount % 1000 == 0 && amount > 50000;
        boolean isCashOutToEmpty = "CASH_OUT".equals(action) && newBalanceDest == 0 && oldBalanceDest == 0;
        boolean isCompleteTransfer = "TRANSFER".equals(action) && newBalanceOrig == 0 && oldBalanceOrig > 100000;
        boolean isHighRiskCountry = isHighRiskCountry(country);
        boolean isUnusualTime = isUnusualTransactionTime();
        
        // Flag if multiple suspicious indicators
        int suspiciousCount = 0;
        if (isRoundAmount) suspiciousCount++;
        if (isCashOutToEmpty) suspiciousCount++;
        if (isCompleteTransfer) suspiciousCount++;
        if (isHighRiskCountry) suspiciousCount++;
        if (isUnusualTime) suspiciousCount++;
        
        return suspiciousCount >= 2;
    }

    private boolean isMultipleFraudIndicators() {
        // Check if multiple fraud indicators are present
        int fraudScore = 0;
        
        if (isUnauthorizedOverdraft) fraudScore += 2;
        if ("1".equals(aiAlert)) fraudScore += 2;
        if ("1".equals(externalBlacklist)) fraudScore += 3;
        if (amount > 1000000) fraudScore += 2;
        if (isHighRiskCountry(country)) fraudScore += 1;
        if (isUnusualTransactionTime()) fraudScore += 1;
        
        return fraudScore >= 4;
    }

    private boolean isHighRiskCountry(String country) {
        String[] highRiskCountries = {"XX", "YY", "ZZ"};
        for (String riskCountry : highRiskCountries) {
            if (riskCountry.equals(country)) return true;
        }
        return false;
    }

    private boolean isUnusualTransactionTime() {
        try {
            long timestamp = Long.parseLong(loginTime);
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTimeInMillis(timestamp);
            int hour = cal.get(java.util.Calendar.HOUR_OF_DAY);
            return hour < 6 || hour > 22; // Unusual if between 10 PM and 6 AM
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
