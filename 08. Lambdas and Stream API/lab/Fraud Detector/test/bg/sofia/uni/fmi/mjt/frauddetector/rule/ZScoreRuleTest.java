package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Channel;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ZScoreRuleTest {

    @Test
    void testWithInvalidWeight() {
        assertThrows(IllegalArgumentException.class, () -> new ZScoreRule(4, 1.2),
            "Expected IllegalArgument for weight out of bound");
    }

    @Test
    void testWithTransactionsNull() {
        Rule rule = new ZScoreRule(3, 0.3);
        assertThrows(IllegalArgumentException.class, () -> rule.applicable(null),
            "Expected IllegalArgument for null list");
    }

    @Test
    void testWithTransactionsEmpty() {
        Rule rule = new ZScoreRule(3, 0.3);
        List<Transaction> l = List.of();
        assertThrows(IllegalArgumentException.class, () -> rule.applicable(l),
            "Expected IllegalArgument for empty list");
    }

    @Test
    void testCalculateMiddleValue() {
        ZScoreRule rule = new ZScoreRule(1.5, 0.5);
        List<Transaction> transactions = List.of(
            new Transaction("1", "account1", 100.0,
                LocalDateTime.now(), "location1", Channel.ATM),
            new Transaction("2", "account2", 200.0,
                LocalDateTime.now(), "location2", Channel.BRANCH),
            new Transaction("3", "account3", 300.0,
                LocalDateTime.now(), "location3", Channel.ONLINE)
        );

        double middleValue = rule.calculateMiddleValue(transactions);
        assertEquals(200.0, middleValue,
            "Expected the middle value (mean) to be correctly calculated");
    }

    @Test
    void testApplicableWithZeroStandardDeviation() {
        Rule rule = new ZScoreRule(1.0, 0.5);
        List<Transaction> transactions = List.of(
            new Transaction("1", "account1", 100.0, LocalDateTime.now(), "location1", Channel.ATM),
            new Transaction("2", "account2", 100.0, LocalDateTime.now(), "location2", Channel.BRANCH),
            new Transaction("3", "account3", 100.0, LocalDateTime.now(), "location3", Channel.ONLINE)
        );

        assertFalse(rule.applicable(transactions),
            "Expected applicable to return false when all transactions have the same amount (zero standard deviation)");
    }

    @Test
    void testcalculateZscore() {
        ZScoreRule rule = new ZScoreRule(1.0, 0.5);
        assertEquals(5.00, rule.calculateZscore(20, 5, 3));
    }
    @Test
    void testApplicableWithZScoreBelowThreshold() {
        Rule rule = new ZScoreRule(3.0, 0.5);
        List<Transaction> transactions = List.of(
            new Transaction("1", "account1", 100.0, LocalDateTime.now(), "location1", Channel.ATM),
            new Transaction("2", "account2", 150.0, LocalDateTime.now(), "location2", Channel.BRANCH),
            new Transaction("3", "account3", 200.0, LocalDateTime.now(), "location3", Channel.ONLINE)
        );

        assertFalse(rule.applicable(transactions),
            "Expected applicable to return false for all transactions with Z-Score below the threshold");
    }
    @Test
    void testApplicableWithZScoreAboveThreshold() {
        Rule rule = new ZScoreRule(1.22, 0.5);
        List<Transaction> transactions = List.of(
            new Transaction("1", "account1", 100.0, LocalDateTime.now(), "location1", Channel.ATM),
            new Transaction("2", "account2", 150.0, LocalDateTime.now(), "location2", Channel.BRANCH),
            new Transaction("3", "account3", 200.0, LocalDateTime.now(), "location3", Channel.ONLINE)
        );

        assertTrue(rule.applicable(transactions),
            "Expected applicable to return true for at least one transaction with Z-Score above the threshold");
    }
    @Test
    void testApplicableWithTransactionAtThreshold() {
        Rule rule = new ZScoreRule(1.22, 0.5);
        List<Transaction> transactions = List.of(
            new Transaction("1", "account1", 100.0, LocalDateTime.now(), "location1", Channel.ATM),
            new Transaction("2", "account2", 120.0, LocalDateTime.now(), "location2", Channel.BRANCH),
            new Transaction("3", "account3", 140.0, LocalDateTime.now(), "location3", Channel.ONLINE)
        );

        // Mean = (100 + 120 + 140) / 3 = 120
        // Std Dev = sqrt(((100-120)^2 + (120-120)^2 + (140-120)^2) / 3)  16 probably
        // Z-Score for 140 = |140 - 120| / std dev 20/16 = 2.neshto
        boolean result = rule.applicable(transactions);

        assertTrue(result, "Expected applicable to return true when a transaction's Z-Score equals the threshold");
    }
}
