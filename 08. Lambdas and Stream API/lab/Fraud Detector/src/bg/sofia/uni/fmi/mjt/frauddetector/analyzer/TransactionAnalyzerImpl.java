package bg.sofia.uni.fmi.mjt.frauddetector.analyzer;

import bg.sofia.uni.fmi.mjt.frauddetector.rule.Rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Channel;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class TransactionAnalyzerImpl implements TransactionAnalyzer {

    List<Transaction> transactions;
    List<Rule> rules;

    public TransactionAnalyzerImpl(Reader reader, List<Rule> rules) {
        validateReaderAndRules(reader, rules);
        transactions = new ArrayList<>();
        readDataFromFile(reader);

        this.rules = rules;

    }

    @Override
    public List<Transaction> allTransactions() {
        return transactions;
    }

    @Override
    public List<String> allAccountIDs() {
        List<String> uniqueIds = transactions.stream()
            .map(Transaction::accountID)
            .distinct()
            .toList();
        return uniqueIds;

    }

    @Override
    public Map<Channel, Integer> transactionCountByChannel() {
        Map<Channel, Integer> transactionCountChannel = transactions.stream()
            .collect(Collectors.groupingBy(Transaction::channel, Collectors.summingInt(t -> 1)));
        return transactionCountChannel;
    }

    @Override
    public double amountSpentByUser(String accountID) {
        if (accountID == null || accountID.isBlank()) {
            throw new IllegalArgumentException("AccountId is null or empty");
        }
        double totalAmountSpend = transactions.stream()
            .filter(e -> e.accountID().equals(accountID))
            .mapToDouble(Transaction::transactionAmount)
            .sum();
        return totalAmountSpend;
    }

    @Override
    public List<Transaction> allTransactionsByUser(String accountId) {
        if (accountId == null || accountId.isBlank()) {
            throw new IllegalArgumentException("AccountId is null or empty");
        }
        List<Transaction> allTransByUser = transactions.stream()
            .filter(e -> e.accountID().equals(accountId))
            .toList();
        return allTransByUser;
    }

    @Override
    public double accountRating(String accountId) {
        if (accountId == null || accountId.isBlank()) {
            throw new IllegalArgumentException("AccountId is null or empty");
        }
        List<Transaction> accountTransactions = transactions.stream()
            .filter(e -> e.accountID().equals(accountId))
            .toList();
        if (accountTransactions.isEmpty()) {
            return 0.0; // No transactions mean no risk
        }
        double riskRating = rules.stream()
            .filter(e -> e.applicable(accountTransactions))
            .mapToDouble(Rule::weight)
            .sum();

        return Math.min(riskRating, 1.00);
    }

    @Override
    public SortedMap<String, Double> accountsRisk() {

        Map<String, Double> riskScores = new HashMap<>();
        for (var transaction : transactions.stream().map(Transaction::accountID).distinct().toList()) {
            String accountId = transaction;

            List<Transaction> allTransactionOfThisId = transactions.stream()
                .filter(e -> e.accountID().equals(accountId))
                .toList();

            double riskRate = rules.stream()
                .filter(e -> e.applicable(allTransactionOfThisId))
                .mapToDouble(Rule::weight)
                .sum();
            riskRate = Math.min(riskRate, 1.00);
            riskScores.put(accountId, riskRate);
        }

        SortedMap<String, Double> sortedMap = new TreeMap<>(
            (key1, key2) -> {
                int result = Double.compare(riskScores.get(key2), riskScores.get(key1)); // Compare by value
                return (result != 0) ? result : key1.compareTo(key2); // Break ties using keys
            }
        );
        sortedMap.putAll(riskScores);
        return sortedMap;
    }

    private void readDataFromFile(Reader reader) {

        try (BufferedReader bufferedReader = new BufferedReader(reader)) {
            String line;
            line = bufferedReader.readLine(); // skiping first line
            while ((line = bufferedReader.readLine()) != null) {
                transactions.add(Transaction.of(line));
            }
        } catch (EOFException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private void validateReaderAndRules(Reader reader, List<Rule> rules) {
        if (reader == null || rules == null) {
            throw new IllegalArgumentException("reader or rules are null");
        }
        double sum = rules.stream()
            .mapToDouble(Rule::weight)
            .sum();
        if (sum != 1.00) {
            throw new IllegalArgumentException("Sum cannot be more than 1.00");
        }
    }
}
