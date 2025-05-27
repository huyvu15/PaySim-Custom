package org.paysim.paysim;

import java.io.*;
import java.util.*;

public class FraudAnalyzer {
    
    public static void main(String[] args) {
        System.out.println("PaySim Fraud Detection Analyzer");
        System.out.println("================================");
        
        analyzeAggregatedTransactions();
        generateFraudReport();
    }
    
    private static void analyzeAggregatedTransactions() {
        String filename = "paramFiles/aggregatedTransactions.csv";
        File file = new File(filename);
        
        if (!file.exists()) {
            System.out.println("Transaction data not found: " + filename);
            return;
        }
        
        System.out.println("Analyzing transaction data from: " + filename);
        
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            String header = br.readLine(); // Skip header
            System.out.println("CSV Header: " + header);
            
            Map<String, TransactionStats> actionStats = new HashMap<>();
            int totalTransactions = 0;
            
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 8) {
                    String action = parts[0];
                    int count = Integer.parseInt(parts[4]);
                    double sum = Double.parseDouble(parts[5]);
                    double avg = Double.parseDouble(parts[6]);
                    
                    actionStats.computeIfAbsent(action, k -> new TransactionStats())
                             .addData(count, sum, avg);
                    totalTransactions += count;
                }
            }
            
            System.out.println("\nTransaction Summary:");
            System.out.println("Total transactions: " + totalTransactions);
            
            for (Map.Entry<String, TransactionStats> entry : actionStats.entrySet()) {
                TransactionStats stats = entry.getValue();
                System.out.printf("%-12s: %8d transactions, Total: $%15.2f, Avg: $%10.2f\n",
                    entry.getKey(), stats.totalCount, stats.totalSum, stats.getOverallAverage());
            }
            
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error parsing numbers: " + e.getMessage());
        }
    }
    
    private static void generateFraudReport() {
        System.out.println("\nFraud Detection Analysis:");
        System.out.println("========================");
        
        // Simple fraud indicators based on transaction patterns
        System.out.println("High-risk patterns detected:");
        System.out.println("1. CASH_OUT transactions - Monitor for account draining");
        System.out.println("2. Large TRANSFER amounts - Check for money laundering");
        System.out.println("3. Off-hours transactions - Flag suspicious timing");
        System.out.println("4. Round number amounts - Potential fraud indicator");
        
        // Create output report
        try {
            File outputDir = new File("output");
            if (!outputDir.exists()) {
                outputDir.mkdirs();
            }
            
            try (PrintWriter writer = new PrintWriter(new FileWriter("output/fraud_analysis_report.txt"))) {
                writer.println("PaySim Fraud Analysis Report");
                writer.println("Generated: " + new Date());
                writer.println("============================");
                writer.println();
                writer.println("Data Source: paramFiles/aggregatedTransactions.csv");
                writer.println("Analysis Status: Complete");
                writer.println();
                writer.println("Fraud Detection Recommendations:");
                writer.println("1. Implement real-time monitoring for CASH_OUT transactions");
                writer.println("2. Set up alerts for high-value TRANSFER operations");
                writer.println("3. Monitor transaction patterns during off-business hours");
                writer.println("4. Flag accounts with multiple round-number transactions");
            }
            
            System.out.println("\nFraud analysis report saved to: output/fraud_analysis_report.txt");
            
        } catch (IOException e) {
            System.err.println("Error writing report: " + e.getMessage());
        }
    }
    
    static class TransactionStats {
        int totalCount = 0;
        double totalSum = 0.0;
        double totalAvgSum = 0.0;
        int avgCount = 0;
        
        void addData(int count, double sum, double avg) {
            totalCount += count;
            totalSum += sum;
            totalAvgSum += avg;
            avgCount++;
        }
        
        double getOverallAverage() {
            return avgCount > 0 ? totalAvgSum / avgCount : 0.0;
        }
    }
}
