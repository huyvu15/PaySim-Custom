## Project Leader

Dr. Edgar Lopez-Rojas
http://edgarlopez.net 

Dataset sample: https://www.kaggle.com/ealaxi/paysim1

## Description

PaySim, a Mobile Money Payment Simulator The Mobile Money Payment Simulation case study is based on a real company that has developed a mobile money implementation that provides mobile phone users with the ability to transfer money between themselves using the phone as a sort of electronic wallet. The task at hand is to develop an approach that detects suspicious activities that are indicative of fraud. Unfortunately, during the initial part of our research this service was only been running in a demo mode. This prevented us from collecting any data that could had been used for analysis of possible detection methods. The development of PaySim covers two phases. During the first phase, we modelled and implemented a MABS that used the schema of the real mobile money service and generated synthetic data following scenarios that were based on predictions of what could be possible when the real system starts operating. During the second phase we got access to transactional financial logs of the system and developed a new version of the simulator which uses aggregated transactional data to generate financial information more alike the original source. Kaggle has featured PaySim1 as dataset of the week of april 2018. See the full article: http://blog.kaggle.com/2017/05/01/datasets-of-the-week-april-2017/ 

## PaySim first paper of the simulator:

Please refer to this dataset using the following citations:

E. A. Lopez-Rojas , A. Elmir, and S. Axelsson. "PaySim: A financial mobile money simulator for fraud detection". In: The 28th European Modeling and Simulation Symposium-EMSS, Larnaca, Cyprus. 2016


## Acknowledgements
This work is part of the research project ”Scalable resource-efficient systems for big data analytics” funded by the Knowledge Foundation (grant: 20140032) in Sweden.

Master's thesis: Elmir A. PaySim Financial Simulator : PaySim Financial Simulator [Internet] [Dissertation]. 2016. Available from: http://urn.kb.se/resolve?urn=urn:nbn:se:bth-14061

2016 PhD Thesis Dr. Edgar Lopez-Rojas
http://bth.diva-portal.org/smash/record.jsf?pid=diva2%3A955852&dswid=-1552

2019 Contribution by Camille Barneaud (https://github.com/gadcam) and the company Flaminem (https://www.flaminem.com/) implementation of Money Laundering cases

# PaySim Enhanced Fraud Detection

A comprehensive financial transaction simulator with advanced fraud detection capabilities.

## Features

- Multi-agent simulation of financial transactions
- Real-time fraud detection with risk scoring
- Comprehensive transaction logging with 25+ fields
- AI-based anomaly detection
- External blacklist integration
- Geographic and temporal risk assessment

## Prerequisites

- Java 11 or higher
- Maven 3.6+ (optional, for dependency management)
- MySQL 8.0+ (for database logging)

## Quick Start

### Option 1: Using Batch File (Windows)
```bash
run.bat
```

### Option 2: Using Maven
```bash
# Compile and run
mvn clean compile exec:java

# Or package and run
mvn clean package
java -jar target/paysim-fraud-detection-1.0.0.jar
```

### Option 3: Manual Compilation
```bash
# Compile
javac -cp "lib/*" -d target/classes src/main/java/org/paysim/paysim/*.java

# Run
java -cp "target/classes;lib/*" org.paysim.paysim.PaySim
```

## Configuration

### Database Setup (Optional)
1. Create MySQL database:
```sql
CREATE DATABASE paysim_fraud;
USE paysim_fraud;

CREATE TABLE paysimLog (
    id INT AUTO_INCREMENT PRIMARY KEY,
    logName VARCHAR(255),
    pType VARCHAR(50),
    pAmount DECIMAL(15,2),
    cliFrom VARCHAR(255),
    pOldBalanceFrom DECIMAL(15,2),
    pNewBalanceFrom DECIMAL(15,2),
    cliTo VARCHAR(255),
    pOldBalanceTo DECIMAL(15,2),
    pNewBalanceTo DECIMAL(15,2),
    isFraud BOOLEAN,
    isFlaggedFraud BOOLEAN,
    step BIGINT,
    isUnauthorizedOverdraft BOOLEAN,
    ipAddress VARCHAR(45),
    deviceType VARCHAR(50),
    browser VARCHAR(50),
    country VARCHAR(10),
    geoLocation VARCHAR(100),
    sessionId VARCHAR(255),
    loginTime VARCHAR(50),
    accountType VARCHAR(50),
    kycLevel VARCHAR(10),
    bank VARCHAR(255),
    fraudScenario VARCHAR(100),
    aiAlert VARCHAR(10),
    externalBlacklist VARCHAR(10),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

2. Update database connection in PaySim configuration

### Simulation Parameters
Edit `PaySim.properties` to customize:
- Number of simulation steps
- Client population size
- Fraud probability rates
- Transaction limits

## Output Files

The simulation generates several output files:
- `{simulatorName}_rawLog.csv` - Complete transaction log
- `{simulatorName}_aggregatedTransactions.csv` - Step aggregates
- `{simulatorName}_fraudsters.csv` - Fraudster analysis
- `{simulatorName}_clientsProfiles.csv` - Client behavior profiles
- `summary.csv` - Global simulation summary

## Fraud Detection Features

### Risk Scoring Algorithm
- Amount-based risk (high value transactions)
- Pattern analysis (round numbers, account draining)
- Geographic risk assessment
- Temporal anomaly detection
- Account behavior analysis

### Fraud Scenarios Detected
- `CASH_OUT_FRAUD` - Suspicious cash withdrawals
- `HIGH_VALUE_TRANSFER` - Large transfers
- `OVERDRAFT_ABUSE` - Unauthorized overdrafts
- `AI_DETECTED_ANOMALY` - ML-flagged transactions
- `BLACKLIST_MATCH` - Known fraudulent entities

## Troubleshooting

### Common Issues
1. **OutOfMemoryError**: Increase heap size with `-Xmx4g`
2. **ClassNotFoundException**: Ensure all JAR files are in lib/ directory
3. **Database Connection Failed**: Check MySQL service and credentials

### Performance Optimization
- Use SSD storage for output files
- Increase JVM heap size for large simulations
- Consider database indexing for large datasets

## License

This project is licensed under the MIT License.
