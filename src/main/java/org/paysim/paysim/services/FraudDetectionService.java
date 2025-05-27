package org.paysim.paysim.services;

import org.paysim.paysim.base.Transaction;
import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;

public class FraudDetectionService {
    
    private static final Map<String, Integer> COUNTRY_RISK_SCORES = new HashMap<>();
    private static final Set<String> BLACKLISTED_ACCOUNTS = new HashSet<>();
    private static final Set<String> BLACKLISTED_IPS = new HashSet<>();
    
    static {
        // Initialize country risk scores
        COUNTRY_RISK_SCORES.put("VN", 5);
        COUNTRY_RISK_SCORES.put("US", 10);
        COUNTRY_RISK_SCORES.put("XX", 50);
        COUNTRY_RISK_SCORES.put("YY", 45);
        
        // Initialize blacklists
        BLACKLISTED_ACCOUNTS.add("C1001");
        BLACKLISTED_ACCOUNTS.add("M9999");
        BLACKLISTED_IPS.add("192.168.1.100");
        BLACKLISTED_IPS.add("10.0.0.1");
    }
    
    public static boolean detectFraud(Transaction transaction) {
        int riskScore = 0;
        
        riskScore += assessAmountRisk(transaction);
        riskScore += assessPatternRisk(transaction);
        riskScore += assessGeographicRisk(transaction);
        riskScore += assessTimeRisk(transaction);
        riskScore += assessAccountBehaviorRisk(transaction);
        
        return riskScore >= 50;
    }
    
    private static int assessAmountRisk(Transaction transaction) {
        int score = 0;
        double amount = transaction.getAmount();
        
        if (amount > 1000000) score += 30;
        else if (amount > 500000) score += 20;
        else if (amount > 100000) score += 10;
        
        if (amount % 1000 == 0 && amount > 10000) score += 15;
        
        return score;
    }
    
    private static int assessPatternRisk(Transaction transaction) {
        int score = 0;
        String action = transaction.getAction();
        
        if ("CASH_OUT".equals(action) && 
            transaction.getNewBalanceDest() == 0 && 
            transaction.getOldBalanceDest() == 0) {
            score += 35;
        }
        
        if ("TRANSFER".equals(action) && 
            transaction.getNewBalanceOrig() == 0 && 
            transaction.getOldBalanceOrig() > 50000) {
            score += 30;
        }
        
        return score;
    }
    
    private static int assessGeographicRisk(Transaction transaction) {
        String country = transaction.getCountry();
        return COUNTRY_RISK_SCORES.getOrDefault(country, 0);
    }
    
    private static int assessTimeRisk(Transaction transaction) {
        try {
            long timestamp = Long.parseLong(transaction.getLoginTime());
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTimeInMillis(timestamp);
            int hour = cal.get(java.util.Calendar.HOUR_OF_DAY);
            
            if (hour >= 22 || hour <= 6) return 15;
            if (hour >= 20 || hour <= 8) return 10;
            
        } catch (NumberFormatException e) {
            return 5;
        }
        
        return 0;
    }
    
    private static int assessAccountBehaviorRisk(Transaction transaction) {
        int score = 0;
        
        if ("1".equals(transaction.getKycLevel()) && transaction.getAmount() > 100000) {
            score += 20;
        }
        
        if ("business".equals(transaction.getAccountType()) && 
            "CASH_OUT".equals(transaction.getAction()) && 
            transaction.getAmount() < 1000) {
            score += 10;
        }
        
        return score;
    }
    
    public static boolean isBlacklisted(Transaction transaction) {
        return BLACKLISTED_ACCOUNTS.contains(transaction.getNameOrig()) ||
               BLACKLISTED_ACCOUNTS.contains(transaction.getNameDest()) ||
               BLACKLISTED_IPS.contains(transaction.getIpAddress());
    }
    
    public static String determineFraudScenario(Transaction transaction) {
        if (isBlacklisted(transaction)) return "BLACKLIST_MATCH";
        if (transaction.isUnauthorizedOverdraft()) return "OVERDRAFT_ABUSE";
        if ("CASH_OUT".equals(transaction.getAction()) && 
            transaction.getNewBalanceDest() == 0) return "CASH_OUT_FRAUD";
        if ("TRANSFER".equals(transaction.getAction()) && 
            transaction.getAmount() > 500000) return "HIGH_VALUE_TRANSFER";
        if (detectFraud(transaction)) return "AI_DETECTED_ANOMALY";
        
        return "NORMAL";
    }
}
