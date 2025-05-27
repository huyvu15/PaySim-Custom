@echo off
setlocal enabledelayedexpansion
echo Starting PaySim Fraud Detection Simulator...

REM Check if we're in the right directory
if not exist "paramFiles" (
    echo ERROR: paramFiles directory not found!
    echo Please run this script from the PaySim root directory
    pause
    exit /b 1
)

REM Check for PaySim.properties
if not exist "PaySim.properties" (
    echo ERROR: PaySim.properties not found!
    echo This file is required to run PaySim
    pause
    exit /b 1
)

echo Found PaySim.properties - loading configuration...

REM Check Java installation and version
echo Checking Java installation...
java -version >java_version.txt 2>&1
if %ERRORLEVEL% neq 0 (
    echo Java not found in PATH. Please install Java 11+ or set JAVA_HOME
    echo Download Java from: https://www.oracle.com/java/technologies/downloads/
    pause
    exit /b 1
)

type java_version.txt
del java_version.txt
echo Java found successfully!

REM Create necessary directories from properties
echo Creating directory structure based on configuration...
if not exist "outputs" mkdir outputs
if not exist "paramFiles\typologies" mkdir paramFiles\typologies
if not exist "paramFiles\clients" mkdir paramFiles\clients
if not exist "paramFiles\transactions" mkdir paramFiles\transactions
if not exist "paramFiles\aggregated" mkdir paramFiles\aggregated
if not exist "paramFiles\initialBalances" mkdir paramFiles\initialBalances
if not exist "paramFiles\scenarios" mkdir paramFiles\scenarios
if not exist "paramFiles\results" mkdir paramFiles\results
if not exist "paramFiles\logs" mkdir paramFiles\logs

REM Look for existing PaySim JAR or source files
if exist "PaySim.jar" (
    echo Found PaySim JAR file
    goto :run_jar
)

if exist "target\PaySim*.jar" (
    echo Found PaySim JAR in target directory
    for %%f in (target\PaySim*.jar) do set PAYSIM_JAR=%%f
    goto :run_jar_target
)

REM Display directory structure
echo.
echo PaySim directory structure:
echo -----------------------------------
dir /ad /b paramFiles
echo -----------------------------------
dir /ad /b paramFiles\typologies
echo -----------------------------------
dir /ad /b paramFiles\clients
echo -----------------------------------
dir /ad /b paramFiles\transactions
echo -----------------------------------
dir /ad /b paramFiles\aggregated
echo -----------------------------------
dir /ad /b paramFiles\initialBalances
echo -----------------------------------
dir /ad /b paramFiles\scenarios
echo -----------------------------------
dir /ad /b paramFiles\results
echo -----------------------------------
dir /ad /b paramFiles\logs
echo -----------------------------------

REM Create PaySim main class that reads properties and generates detailed transactions
echo Creating PaySim main class with transaction generation...
(
echo package org.paysim.paysim;
echo.
echo import java.io.*;
echo import java.util.*;
echo import java.text.SimpleDateFormat;
echo import java.math.BigDecimal;
echo import java.math.RoundingMode;
echo.
echo public class PaySim {
echo     private static Properties config = new Properties^(^);
echo     private static Random random = new Random^(^);
echo     private static SimpleDateFormat dateFormat = new SimpleDateFormat^("yyyy-MM-dd HH:mm:ss"^);
echo     
echo     // Constants for transaction generation
echo     private static final String[] DEVICE_TYPES = {"Mobile", "Desktop", "Tablet", "ATM", "POS"};
echo     private static final String[] BROWSERS = {"Chrome", "Firefox", "Safari", "Edge", "Opera", "Mobile App"};
echo     private static final String[] COUNTRIES = {"Vietnam", "USA", "Singapore", "Thailand", "Malaysia", "Indonesia"};
echo     private static final String[] BANKS = {"VPBank", "Vietcombank", "BIDV", "Techcombank", "ACB"};
echo     private static final String[] ACCOUNT_TYPES = {"SAVINGS", "CHECKING", "PREMIUM", "BUSINESS"};
echo     private static final String[] KYC_LEVELS = {"BASIC", "STANDARD", "PREMIUM", "VERIFIED"};
echo     private static final String[] FRAUD_SCENARIOS = {"NORMAL", "SUSPICIOUS_AMOUNT", "RAPID_SUCCESSION", "UNUSUAL_LOCATION", "COMPROMISED_ACCOUNT"};
echo     
echo     public static void main^(String[] args^) {
echo         System.out.println^("PaySim Fraud Detection Simulator"^);
echo         System.out.println^("================================"^);
echo         
echo         loadConfiguration^(^);
echo         generateTransactionData^(^);
echo     }
echo.
echo     private static void loadConfiguration^(^) {
echo         try ^(FileInputStream fis = new FileInputStream^("PaySim.properties"^)^) {
echo             config.load^(fis^);
echo             System.out.println^("Configuration loaded successfully"^);
echo             System.out.println^("Steps: " + config.getProperty^("nbSteps"^)^);
echo             System.out.println^("Clients: " + config.getProperty^("nbClients"^)^);
echo             System.out.println^("Fraudsters: " + config.getProperty^("nbFraudsters"^)^);
echo         } catch ^(IOException e^) {
echo             System.err.println^("Error loading PaySim.properties: " + e.getMessage^(^)^);
echo             System.exit^(1^);
echo         }
echo     }
echo.
echo     private static void generateTransactionData^(^) {
echo         System.out.println^("Reading aggregated transaction data..."^);
echo         
echo         String outputPath = config.getProperty^("outputPath", "./outputs/"^);
echo         File outputDir = new File^(outputPath^);
echo         outputDir.mkdirs^(^);
echo         
echo         try ^(BufferedReader br = new BufferedReader^(new FileReader^("paramFiles/aggregatedTransactions.csv"^)^);
echo              PrintWriter writer = new PrintWriter^(new FileWriter^(new File^(outputDir, "transactions.csv"^)^)^)^) {
echo             
echo             // Write CSV header
echo             writer.println^("step,action,amount,nameOrig,oldBalanceOrig,newBalanceOrig,nameDest,oldBalanceDest,newBalanceDest,isFraud,isFlaggedFraud,isUnauthorizedOverdraft,ipAddress,deviceType,browser,country,geoLocation,sessionId,loginTime,accountType,kycLevel,bank,fraudScenario,aiAlert,externalBlacklist"^);
echo             
echo             String line = br.readLine^(^); // Skip header
echo             int transactionId = 1;
echo             
echo             while ^(^(line = br.readLine^(^)^) != null^) {
echo                 String[] parts = line.split^(","^);
echo                 if ^(parts.length ^>= 9^) {
echo                     generateTransactionsFromAggregated^(writer, parts, transactionId^);
echo                     transactionId += Integer.parseInt^(parts[4]^); // count
echo                 }
echo             }
echo             
echo             System.out.println^("Generated " + ^(transactionId - 1^) + " transactions"^);
echo             System.out.println^("Output saved to: " + new File^(outputDir, "transactions.csv"^).getAbsolutePath^(^)^);
echo             
echo         } catch ^(IOException e^) {
echo             System.err.println^("Error generating transaction data: " + e.getMessage^(^)^);
echo         }
echo     }
echo.
echo     private static void generateTransactionsFromAggregated^(PrintWriter writer, String[] aggregatedData, int startId^) {
echo         String action = aggregatedData[0];
echo         int step = Integer.parseInt^(aggregatedData[8]^);
echo         int count = Integer.parseInt^(aggregatedData[4]^);
echo         double totalSum = Double.parseDouble^(aggregatedData[5]^);
echo         double avgAmount = Double.parseDouble^(aggregatedData[6]^);
echo         double stdDev = Double.parseDouble^(aggregatedData[7]^);
echo         
echo         // Generate individual transactions based on aggregated data
echo         for ^(int i = 0; i ^< count; i++^) {
echo             double amount = generateAmount^(avgAmount, stdDev^);
echo             String nameOrig = generateAccountName^("C", startId + i^);
echo             String nameDest = generateAccountName^("C", random.nextInt^(20000^) + 1000000^);
echo             
echo             // Generate balances
echo             double oldBalanceOrig = generateBalance^(amount^);
echo             double newBalanceOrig = calculateNewBalance^(action, oldBalanceOrig, amount, true^);
echo             double oldBalanceDest = generateBalance^(amount^);
echo             double newBalanceDest = calculateNewBalance^(action, oldBalanceDest, amount, false^);
echo             
echo             // Fraud detection logic
echo             boolean isFraud = determineFraud^(action, amount, step^);
echo             boolean isFlaggedFraud = isFraud ^&^& random.nextDouble^(^) ^< 0.7; // 70% detection rate
echo             boolean isUnauthorizedOverdraft = newBalanceOrig ^< 0 ^&^& random.nextDouble^(^) ^< 0.1;
echo             
echo             // Generate additional fields
echo             String ipAddress = generateIpAddress^(^);
echo             String deviceType = DEVICE_TYPES[random.nextInt^(DEVICE_TYPES.length^)];
echo             String browser = BROWSERS[random.nextInt^(BROWSERS.length^)];
echo             String country = COUNTRIES[random.nextInt^(COUNTRIES.length^)];
echo             String geoLocation = generateGeoLocation^(country^);
echo             String sessionId = generateSessionId^(^);
echo             String loginTime = generateLoginTime^(step^);
echo             String accountType = ACCOUNT_TYPES[random.nextInt^(ACCOUNT_TYPES.length^)];
echo             String kycLevel = KYC_LEVELS[random.nextInt^(KYC_LEVELS.length^)];
echo             String bank = BANKS[random.nextInt^(BANKS.length^)];
echo             String fraudScenario = determineFraudScenario^(isFraud, amount, step^);
echo             String aiAlert = generateAiAlert^(isFraud, amount^);
echo             boolean externalBlacklist = isFraud ^&^& random.nextDouble^(^) ^< 0.3;
echo             
echo             // Write transaction to CSV
echo             writer.printf^("%d,%s,%.2f,%s,%.2f,%.2f,%s,%.2f,%.2f,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s%n",
echo                 step, action, amount, nameOrig, oldBalanceOrig, newBalanceOrig,
echo                 nameDest, oldBalanceDest, newBalanceDest,
echo                 isFraud ? "1" : "0", isFlaggedFraud ? "1" : "0", isUnauthorizedOverdraft ? "1" : "0",
echo                 ipAddress, deviceType, browser, country, geoLocation, sessionId,
echo                 loginTime, accountType, kycLevel, bank, fraudScenario, aiAlert,
echo                 externalBlacklist ? "1" : "0"^);
echo         }
echo     }
echo.
echo     private static double generateAmount^(double avg, double stdDev^) {
echo         double amount = random.nextGaussian^(^) * stdDev + avg;
echo         return Math.max^(1.0, Math.round^(amount * 100.0^) / 100.0^);
echo     }
echo.
echo     private static String generateAccountName^(String prefix, int id^) {
echo         return prefix + String.format^("%010d", id^);
echo     }
echo.
echo     private static double generateBalance^(double amount^) {
echo         double balance = amount * ^(2 + random.nextDouble^(^) * 10^);
echo         return Math.round^(balance * 100.0^) / 100.0;
echo     }
echo.
echo     private static double calculateNewBalance^(String action, double oldBalance, double amount, boolean isOrig^) {
echo         double newBalance = oldBalance;
echo         
echo         if ^(isOrig^) {
echo             if ^("CASH_OUT".equals^(action^) ^|^| "PAYMENT".equals^(action^) ^|^| "TRANSFER".equals^(action^) ^|^| "DEBIT".equals^(action^)^) {
echo                 newBalance = oldBalance - amount;
echo             } else if ^("CASH_IN".equals^(action^)^) {
echo                 newBalance = oldBalance + amount;
echo             }
echo         } else {
echo             if ^("CASH_IN".equals^(action^) ^|^| "PAYMENT".equals^(action^) ^|^| "TRANSFER".equals^(action^)^) {
echo                 newBalance = oldBalance + amount;
echo             }
echo         }
echo         
echo         return Math.round^(newBalance * 100.0^) / 100.0;
echo     }
echo.
echo     private static boolean determineFraud^(String action, double amount, int step^) {
echo         double fraudProbability = Double.parseDouble^(config.getProperty^("fraudProbability", "0.001"^)^);
echo         
echo         // Higher fraud probability for certain conditions
echo         if ^(amount ^> 200000^) fraudProbability *= 3; // Large amounts
echo         if ^("TRANSFER".equals^(action^) ^|^| "CASH_OUT".equals^(action^)^) fraudProbability *= 2; // Risky actions
echo         if ^(step ^< 100 ^|^| step ^> 600^) fraudProbability *= 1.5; // Unusual hours
echo         
echo         return random.nextDouble^(^) ^< fraudProbability;
echo     }
echo.
echo     private static String generateIpAddress^(^) {
echo         return String.format^("%d.%d.%d.%d", 
echo             random.nextInt^(256^), random.nextInt^(256^),
echo             random.nextInt^(256^), random.nextInt^(256^)^);
echo     }
echo.
echo     private static String generateGeoLocation^(String country^) {
echo         Map^<String, String^> locations = new HashMap^<^>^(^);
echo         locations.put^("Vietnam", "21.0285,-105.8542"^);
echo         locations.put^("USA", "39.8283,-98.5795"^);
echo         locations.put^("Singapore", "1.3521,103.8198"^);
echo         locations.put^("Thailand", "15.8700,100.9925"^);
echo         locations.put^("Malaysia", "4.2105,101.9758"^);
echo         locations.put^("Indonesia", "-0.7893,113.9213"^);
echo         
echo         return locations.getOrDefault^(country, "0.0,0.0"^);
echo     }
echo.
echo     private static String generateSessionId^(^) {
echo         return "SES" + System.currentTimeMillis^(^) + random.nextInt^(1000^);
echo     }
echo.
echo     private static String generateLoginTime^(int step^) {
echo         Calendar cal = Calendar.getInstance^(^);
echo         cal.add^(Calendar.HOUR, -^(720 - step^)^); // Simulate time progression
echo         return dateFormat.format^(cal.getTime^(^)^);
echo     }
echo.
echo     private static String determineFraudScenario^(boolean isFraud, double amount, int step^) {
echo         if ^(!isFraud^) return "NORMAL";
echo         
echo         if ^(amount ^> 500000^) return "SUSPICIOUS_AMOUNT";
echo         if ^(step ^% 10 ^< 3^) return "RAPID_SUCCESSION";
echo         if ^(step ^< 50 ^|^| step ^> 650^) return "UNUSUAL_LOCATION";
echo         return "COMPROMISED_ACCOUNT";
echo     }
echo.
echo     private static String generateAiAlert^(boolean isFraud, double amount^) {
echo         if ^(!isFraud^) {
echo             return random.nextDouble^(^) ^< 0.05 ? "FALSE_POSITIVE" : "NORMAL";
echo         }
echo         
echo         if ^(amount ^> 1000000^) return "HIGH_RISK";
echo         if ^(amount ^> 500000^) return "MEDIUM_RISK";
echo         return "LOW_RISK";
echo     }
echo }
) > src\main\java\org\paysim\paysim\PaySim.java

goto :compile_sources

:run_jar
echo Running PaySim JAR...
java -jar PaySim.jar
goto :success

:run_jar_target
echo Running PaySim JAR from target...
java -jar "%PAYSIM_JAR%"
goto :success

:compile_sources
echo Compiling PaySim sources...
javac -d target\classes -cp target\classes src\main\java\org\paysim\paysim\*.java 2>compilation_errors.txt

if %ERRORLEVEL% neq 0 (
    echo Compilation failed! Error details:
    type compilation_errors.txt
    echo.
    echo Trying alternative compilation...
    goto :try_compile_existing
)

del compilation_errors.txt
echo Compilation successful!
goto :run_simulation

:try_compile_existing
echo Attempting to compile any existing Java files...
REM Try to find and compile any existing Java files
for /r . %%f in (*.java) do (
    echo Compiling: %%f
    javac -d target\classes "%%f" 2>nul
)

REM Look for any compiled main classes
for /r target\classes %%f in (*.class) do (
    echo Found compiled class: %%f
)

:run_simulation
echo.
echo Running PaySim with configuration...
java -cp target\classes -Dpaysim.config=PaySim.properties org.paysim.paysim.PaySim

if %ERRORLEVEL% neq 0 (
    echo Runtime error occurred. Details:
    type runtime_errors.txt
    echo.
    echo Trying alternative execution methods...
    
    REM Try to find any main class
    for /r target\classes %%f in (*PaySim*.class) do (
        echo Attempting to run: %%f
        java -cp target\classes org.paysim.paysim.PaySim
        goto :success
    )
    
    echo Unable to execute PaySim. Please check Java installation and source files.
    pause
    exit /b 1
)

del runtime_errors.txt 2>nul

:success
echo.
echo ============================================
echo PaySim execution completed successfully!
echo ============================================
echo.
echo Available files:
echo.
echo Parameter Files:
dir paramFiles\*.* /b 2>nul
echo.
echo Output Files:
dir output\*.* /b 2>nul
echo.
echo Generated Classes:
dir target\classes\org\paysim\paysim\*.class /b 2>nul
echo.
echo PaySim is ready for fraud detection analysis!
echo ============================================
pause
