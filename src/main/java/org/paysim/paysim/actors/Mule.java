package org.paysim.paysim.actors;

import org.paysim.paysim.PaySim;
import org.paysim.paysim.base.Transaction;

public class Mule extends Client {
    
    public Mule(String name, Bank bank) {
        super(name, bank);
    }

    void fraudulentCashOut(PaySim paysim, int step, double amount) {
        String action = "CASH_OUT";

        Merchant merchantTo = paysim.pickRandomMerchant();
        String nameOrig = this.getName();
        String nameDest = merchantTo.getName();
        double oldBalanceOrig = this.getBalance();
        double oldBalanceDest = merchantTo.getBalance();

        // Perform the withdrawal
        boolean isUnauthorizedOverdraft = this.withdraw(amount);

        double newBalanceOrig = this.getBalance();
        double newBalanceDest = merchantTo.getBalance();

        // Generate realistic session data for mule transactions
        java.util.Random rand = paysim.random;
        Transaction t = new Transaction(step, action, amount, nameOrig, oldBalanceOrig,
                newBalanceOrig, nameDest, oldBalanceDest, newBalanceDest,
                generateMuleIp(rand), generateMuleDevice(rand), generateMuleBrowser(rand), 
                generateMuleCountry(rand), generateMuleGeo(rand), generateMuleSessionId(rand), 
                generateMuleLoginTime(rand), generateMuleAccountType(rand), generateMuleKycLevel(rand),
                this.getBank().getName(), "MULE_CASHOUT", "1", "0");
        
        // Set fraud indicators for mule transactions
        t.setFraud(true); // Mule transactions are inherently fraudulent
        t.setUnauthorizedOverdraft(isUnauthorizedOverdraft);
        
        paysim.getTransactions().add(t);
    }

    private String getBank() {
        // Access the bank field from parent Client class
        return "MULE_BANK"; // Default bank name for mules
    }

    // Generate suspicious patterns typical of mule accounts
    private static String generateMuleIp(java.util.Random rand) {
        // Mules often use VPNs or suspicious IP ranges
        String[] suspiciousRanges = {"10.0.0.", "192.168.1.", "203.0.113.", "198.51.100."};
        String base = suspiciousRanges[rand.nextInt(suspiciousRanges.length)];
        return base + rand.nextInt(256);
    }

    private static String generateMuleDevice(java.util.Random rand) {
        // Mules often use mobile devices for quick access
        String[] devices = {"Mobile", "Mobile", "Mobile", "Desktop"}; // Bias toward mobile
        return devices[rand.nextInt(devices.length)];
    }

    private static String generateMuleBrowser(java.util.Random rand) {
        String[] browsers = {"Chrome", "Firefox", "Safari"};
        return browsers[rand.nextInt(browsers.length)];
    }

    private static String generateMuleCountry(java.util.Random rand) {
        // Mix of high-risk and normal countries
        String[] countries = {"VN", "XX", "YY", "US", "TH"};
        return countries[rand.nextInt(countries.length)];
    }

    private static String generateMuleGeo(java.util.Random rand) {
        // Random coordinates that might not match claimed country
        double lat = -90 + 180 * rand.nextDouble();
        double lon = -180 + 360 * rand.nextDouble();
        return String.format("%.6f,%.6f", lat, lon);
    }

    private static String generateMuleSessionId(java.util.Random rand) {
        return "MULE_" + System.currentTimeMillis() + String.format("%04d", rand.nextInt(10000));
    }

    private static String generateMuleLoginTime(java.util.Random rand) {
        long now = System.currentTimeMillis();
        // Mules often operate at unusual hours
        long randomOffset = rand.nextInt(3600000); // Within last hour for quick cashout
        return String.valueOf(now - randomOffset);
    }

    private static String generateMuleAccountType(java.util.Random rand) {
        // Mule accounts are typically personal accounts opened for fraud
        return "personal";
    }

    private static String generateMuleKycLevel(java.util.Random rand) {
        // Mule accounts typically have low KYC to avoid detection
        String[] levels = {"1", "1", "2"}; // Bias toward low KYC
        return levels[rand.nextInt(levels.length)];
    }
}