package org.paysim.paysim;

import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class PaySim {
    private static Properties config = new Properties();
    private static Random random = new Random();
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
ECHO is off.
    // Constants for transaction generation
    private static final String[] DEVICE_TYPES = {"Mobile", "Desktop", "Tablet", "ATM", "POS"};
    private static final String[] BROWSERS = {"Chrome", "Firefox", "Safari", "Edge", "Opera", "Mobile App"};
    private static final String[] COUNTRIES = {"Vietnam", "USA", "Singapore", "Thailand", "Malaysia", "Indonesia"};
    private static final String[] BANKS = {"VPBank", "Vietcombank", "BIDV", "Techcombank", "ACB"};
    private static final String[] ACCOUNT_TYPES = {"SAVINGS", "CHECKING", "PREMIUM", "BUSINESS"};
    private static final String[] KYC_LEVELS = {"BASIC", "STANDARD", "PREMIUM", "VERIFIED"};
    private static final String[] FRAUD_SCENARIOS = {"NORMAL", "SUSPICIOUS_AMOUNT", "RAPID_SUCCESSION", "UNUSUAL_LOCATION", "COMPROMISED_ACCOUNT"};
ECHO is off.
    public static void main(String[] args) {
        System.out.println("PaySim Fraud Detection Simulator");
        System.out.println("================================");
ECHO is off.
        loadConfiguration();
        generateTransactionData();
    }

    private static void loadConfiguration() {
        try (FileInputStream fis = new FileInputStream("PaySim.properties")) {
            config.load(fis);
            System.out.println("Configuration loaded successfully");
            System.out.println("Steps: " + config.getProperty("nbSteps"));
            System.out.println("Clients: " + config.getProperty("nbClients"));
            System.out.println("Fraudsters: " + config.getProperty("nbFraudsters"));
        } catch (IOException e) {
            System.err.println("Error loading PaySim.properties: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void generateTransactionData() {
        System.out.println("Reading aggregated transaction data...");
ECHO is off.
        String outputPath = config.getProperty("outputPath", "./outputs/");
        File outputDir = new File(outputPath);
        outputDir.mkdirs();
ECHO is off.
        try (BufferedReader br = new BufferedReader(new FileReader("paramFiles/aggregatedTransactions.csv"));
             PrintWriter writer = new PrintWriter(new FileWriter(new File(outputDir, "transactions.csv")))) {
ECHO is off.
            // Write CSV header
            writer.println("step,action,amount,nameOrig,oldBalanceOrig,newBalanceOrig,nameDest,oldBalanceDest,newBalanceDest,isFraud,isFlaggedFraud,isUnauthorizedOverdraft,ipAddress,deviceType,browser,country,geoLocation,sessionId,loginTime,accountType,kycLevel,bank,fraudScenario,aiAlert,externalBlacklist");
ECHO is off.
            String line = br.readLine(); // Skip header
            int transactionId = 1;
ECHO is off.
            while ((line = br.readLine()) = null) {
                String[] parts = line.split(",");
                if (parts.length >= 9) {
                    generateTransactionsFromAggregated(writer, parts, transactionId);
                    transactionId += Integer.parseInt(parts[4]); // count
                }
            }
ECHO is off.
            System.out.println("Generated " + (transactionId - 1) + " transactions");
            System.out.println("Output saved to: " + new File(outputDir, "transactions.csv").getAbsolutePath());
ECHO is off.
        } catch (IOException e) {
            System.err.println("Error generating transaction data: " + e.getMessage());
        }
    }

    private static void generateTransactionsFromAggregated(PrintWriter writer, String[] aggregatedData, int startId) {
        String action = aggregatedData[0];
        int step = Integer.parseInt(aggregatedData[8]);
        int count = Integer.parseInt(aggregatedData[4]);
        double totalSum = Double.parseDouble(aggregatedData[5]);
        double avgAmount = Double.parseDouble(aggregatedData[6]);
        double stdDev = Double.parseDouble(aggregatedData[7]);
ECHO is off.
        // Generate individual transactions based on aggregated data
        for (int i = 0; i < count; i++) {
            double amount = generateAmount(avgAmount, stdDev);
            String nameOrig = generateAccountName("C", startId + i);
            String nameDest = generateAccountName("C", random.nextInt(20000) + 1000000);
ECHO is off.
            // Generate balances
            double oldBalanceOrig = generateBalance(amount);
            double newBalanceOrig = calculateNewBalance(action, oldBalanceOrig, amount, true);
            double oldBalanceDest = generateBalance(amount);
            double newBalanceDest = calculateNewBalance(action, oldBalanceDest, amount, false);
ECHO is off.
            // Fraud detection logic
            boolean isFraud = determineFraud(action, amount, step);
            boolean isFlaggedFraud = isFraud && random.nextDouble() < 0.7; // 70 detection rate
            boolean isUnauthorizedOverdraft = newBalanceOrig < 0 && random.nextDouble() < 0.1;
ECHO is off.
            // Generate additional fields
            String ipAddress = generateIpAddress();
            String deviceType = DEVICE_TYPES[random.nextInt(DEVICE_TYPES.length)];
            String browser = BROWSERS[random.nextInt(BROWSERS.length)];
            String country = COUNTRIES[random.nextInt(COUNTRIES.length)];
            String geoLocation = generateGeoLocation(country);
            String sessionId = generateSessionId();
            String loginTime = generateLoginTime(step);
            String accountType = ACCOUNT_TYPES[random.nextInt(ACCOUNT_TYPES.length)];
            String kycLevel = KYC_LEVELS[random.nextInt(KYC_LEVELS.length)];
            String bank = BANKS[random.nextInt(BANKS.length)];
            String fraudScenario = determineFraudScenario(isFraud, amount, step);
            String aiAlert = generateAiAlert(isFraud, amount);
            boolean externalBlacklist = isFraud && random.nextDouble() < 0.3;
ECHO is off.
            // Write transaction to CSV
            writer.printf("s,s,.2f,.2f,s,s,s,s,s,s,s,s,n",
                step, action, amount, nameOrig, oldBalanceOrig, newBalanceOrig,
                nameDest, oldBalanceDest, newBalanceDest,
                isFraud ? "1" : "0", isFlaggedFraud ? "1" : "0", isUnauthorizedOverdraft ? "1" : "0",
                ipAddress, deviceType, browser, country, geoLocation, sessionId,
                loginTime, accountType, kycLevel, bank, fraudScenario, aiAlert,
                externalBlacklist ? "1" : "0");
        }
    }

    private static double generateAmount(double avg, double stdDev) {
        double amount = random.nextGaussian() * stdDev + avg;
        return Math.max(1.0, Math.round(amount * 100.0) / 100.0);
    }

    private static String generateAccountName(String prefix, int id) {
        return prefix + String.format(""D:\VPBank-chanllenge\PaySim\run.bat"10d", id);
    }

    private static double generateBalance(double amount) {
        double balance = amount * (2 + random.nextDouble() * 10);
        return Math.round(balance * 100.0) / 100.0;
    }

    private static double calculateNewBalance(String action, double oldBalance, double amount, boolean isOrig) {
        double newBalance = oldBalance;
ECHO is off.
        if (isOrig) {
            if ("CASH_OUT".equals(action) || "PAYMENT".equals(action) || "TRANSFER".equals(action) || "DEBIT".equals(action)) {
                newBalance = oldBalance - amount;
            } else if ("CASH_IN".equals(action)) {
                newBalance = oldBalance + amount;
            }
        } else {
            if ("CASH_IN".equals(action) || "PAYMENT".equals(action) || "TRANSFER".equals(action)) {
                newBalance = oldBalance + amount;
            }
        }
ECHO is off.
        return Math.round(newBalance * 100.0) / 100.0;
    }

    private static boolean determineFraud(String action, double amount, int step) {
        double fraudProbability = Double.parseDouble(config.getProperty("fraudProbability", "0.001"));
ECHO is off.
        // Higher fraud probability for certain conditions
        if (amount > 200000) fraudProbability *= 3; // Large amounts
        if ("TRANSFER".equals(action) || "CASH_OUT".equals(action)) fraudProbability *= 2; // Risky actions
        if (step < 100 || step > 600) fraudProbability *= 1.5; // Unusual hours
ECHO is off.
        return random.nextDouble() < fraudProbability;
    }

    private static String generateIpAddress() {
        return String.format("d.d", 
            random.nextInt(256), random.nextInt(256),
            random.nextInt(256), random.nextInt(256));
    }

    private static String generateGeoLocation(String country) {
        Map<String, String> locations = new HashMap<>();
        locations.put("Vietnam", "21.0285,-105.8542");
        locations.put("USA", "39.8283,-98.5795");
        locations.put("Singapore", "1.3521,103.8198");
        locations.put("Thailand", "15.8700,100.9925");
        locations.put("Malaysia", "4.2105,101.9758");
        locations.put("Indonesia", "-0.7893,113.9213");
ECHO is off.
        return locations.getOrDefault(country, "0.0,0.0");
    }

    private static String generateSessionId() {
        return "SES" + System.currentTimeMillis() + random.nextInt(1000);
    }

    private static String generateLoginTime(int step) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, -(720 - step)); // Simulate time progression
        return dateFormat.format(cal.getTime());
    }

    private static String determineFraudScenario(boolean isFraud, double amount, int step) {
        if (isFraud) return "NORMAL";
ECHO is off.
        if (amount > 500000) return "SUSPICIOUS_AMOUNT";
        if (step  10 < 3) return "RAPID_SUCCESSION";
        if (step < 50 || step > 650) return "UNUSUAL_LOCATION";
        return "COMPROMISED_ACCOUNT";
    }

    private static String generateAiAlert(boolean isFraud, double amount) {
        if (isFraud) {
            return random.nextDouble() < 0.05 ? "FALSE_POSITIVE" : "NORMAL";
        }
ECHO is off.
        if (amount > 1000000) return "HIGH_RISK";
        if (amount > 500000) return "MEDIUM_RISK";
        return "LOW_RISK";
    }
}
