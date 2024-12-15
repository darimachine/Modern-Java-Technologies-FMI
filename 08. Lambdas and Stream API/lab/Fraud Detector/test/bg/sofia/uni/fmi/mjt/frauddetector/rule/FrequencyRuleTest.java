package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Channel;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FrequencyRuleTest {

    @Test
    void testWithInvalidWeight() {
        assertThrows(IllegalArgumentException.class, () -> new FrequencyRule(4, Period.ofWeeks(4), 1.2),
            "Expected IllegalArgument for weight out of bound");
    }

    @Test
    void testWithTransactionsNull() {
        Rule rule = new FrequencyRule(0, Period.ofWeeks(4), 0.3);
        assertThrows(IllegalArgumentException.class, () -> rule.applicable(null),
            "Expected IllegalArgument for null list");
    }

    @Test
    void testWithTransactionsEmpty() {
        Rule rule = new FrequencyRule(0, Period.ofWeeks(4), 0.3);
        List<Transaction> l = List.of();
        assertThrows(IllegalArgumentException.class, () -> rule.applicable(l),
            "Expected IllegalArgument for empty list");
    }

    @Test
    void testApplicableWIthTransactionsBelowThreshold() {
        Rule rule = new FrequencyRule(3, Period.ofDays(1), 0.3);
        List<Transaction> transactions = List.of(
            new Transaction("1", "acount1",
                100.0, LocalDateTime.now(), "Online", Channel.ATM),
            new Transaction("2", "acount2",
                130.0, LocalDateTime.now().plusSeconds(500),
                "Online", Channel.ATM),
            new Transaction("3", "account3",
                852.0, LocalDateTime.now().minusHours(24),
                "Diego", Channel.BRANCH)
        );
        assertFalse(rule.applicable(transactions), "Expecting to return false");
        assertEquals(0.3, rule.weight());
    }

    @Test
    void testApplicableWithTransactionAtThreshold() {
        Rule rule = new FrequencyRule(3, Period.ofDays(1), 0.3);
        List<Transaction> transactions = List.of(
            new Transaction("1", "acount1",
                100.0, LocalDateTime.now(), "Online", Channel.ATM),
            new Transaction("2", "acount2",
                130.0, LocalDateTime.now().plusSeconds(500),
                "Online", Channel.ATM),
            new Transaction("3", "account3",
                852.0, LocalDateTime.now().minusHours(23),
                "Diego", Channel.BRANCH)
        );
        assertTrue(rule.applicable(transactions), "Expecting to return false");
        assertEquals(0.3, rule.weight());
    }

    @Test
    void testApplicableWithTransactionAboveThreshold() {
        Rule rule = new FrequencyRule(2, Period.ofDays(1), 0.3);
        List<Transaction> transactions = List.of(
            new Transaction("1", "acount1",
                100.0, LocalDateTime.now(), "Online", Channel.ATM),
            new Transaction("2", "acount2",
                130.0, LocalDateTime.now().plusSeconds(500),
                "Online", Channel.ATM),
            new Transaction("3", "account3",
                852.0, LocalDateTime.now().minusHours(23),
                "Diego", Channel.BRANCH)
        );
        assertTrue(rule.applicable(transactions), "Expecting to return false");
        assertEquals(0.3, rule.weight());
    }
    @Test
    void testApplicableWithOverlappingTimeWindows() {
        Rule rule = new FrequencyRule(3, Period.ofDays(1), 0.3);
        List<Transaction> transactions = List.of(
            new Transaction("1", "acc1", 100.0, LocalDateTime.now(), "loc1", Channel.ATM),
            new Transaction("2", "acc1", 200.0, LocalDateTime.now().minusHours(23), "loc1", Channel.ATM),
            new Transaction("3", "acc1", 300.0, LocalDateTime.now().minusHours(22), "loc1", Channel.ATM)
        );


        boolean result = rule.applicable(transactions);
        assertEquals(true, result, "Expected applicable to return true when transactions are within overlapping windows");
        assertEquals(0.3, rule.weight());
    }
    @Test
    void testApplicableWithNonOverlappingTimeWindows() {
        Rule rule = new FrequencyRule(3, Period.ofDays(1), 0.3);
        List<Transaction> transactions = List.of(
            new Transaction("1", "acc1", 100.0, LocalDateTime.now(), "loc1", Channel.ATM),
            new Transaction("2", "acc1", 200.0, LocalDateTime.now().minusDays(2), "loc1", Channel.ATM),
            new Transaction("3", "acc1", 300.0, LocalDateTime.now().minusDays(3), "loc1", Channel.ATM)
        );

        assertFalse(rule.applicable(transactions), "Expected applicable to return false when transactions are in non-overlapping windows");
        assertEquals(0.3, rule.weight());
    }

}
