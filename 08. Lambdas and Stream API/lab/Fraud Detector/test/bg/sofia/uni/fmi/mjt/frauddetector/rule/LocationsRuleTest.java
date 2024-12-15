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

public class LocationsRuleTest {
    @Test
    void testWithInvalidWeight() {
        assertThrows(IllegalArgumentException.class, () -> new LocationsRule(4, 1.2),
            "Expected IllegalArgument for weight out of bound");
    }

    @Test
    void testWithTransactionsNull() {
        Rule rule = new LocationsRule(3, 0.3);
        assertThrows(IllegalArgumentException.class, () -> rule.applicable(null),
            "Expected IllegalArgument for null list");
    }

    @Test
    void testWithTransactionsEmpty() {
        Rule rule = new LocationsRule(3, 0.3);
        List<Transaction> l = List.of();
        assertThrows(IllegalArgumentException.class, () -> rule.applicable(l),
            "Expected IllegalArgument for empty list");
    }
    @Test
    void getTestApplicableWithTransactionBelowThreshold() {
        Rule rule = new LocationsRule(3,0.5);
        List<Transaction> transactions = List.of(
            new Transaction("1","account1",
                312.0, LocalDateTime.now(),
                "San Diego", Channel.BRANCH),
            new Transaction("2","account2",
                632.0,
                LocalDateTime.now(),"San Diego", Channel.BRANCH),
            new Transaction("3","account3",
                352.0,
                LocalDateTime.now(),"San Diego", Channel.ATM),
            new Transaction("4","account4",
                342.0,
                LocalDateTime.now(),"San Diego Random", Channel.ONLINE)
        );
        assertFalse(rule.applicable(transactions));
        assertEquals(0.5, rule.weight());
    }
    @Test
    void testApplicableWithTransactionAtThreshold() {
        Rule rule = new LocationsRule(3,0.5);
        List<Transaction> transactions = List.of(
            new Transaction("1","account1",
                312.0,
                LocalDateTime.now(), "San Diego", Channel.BRANCH),
            new Transaction("2","account2",
                632.0,
                LocalDateTime.now(),"San Diego", Channel.BRANCH),
            new Transaction("3","account3",
                352.0,
                LocalDateTime.now(),"San Diego Maradona", Channel.ATM),
            new Transaction("4","account4",
                342.0,
                LocalDateTime.now(),"San Diego Random", Channel.ONLINE)
        );
        assertTrue(rule.applicable(transactions));
        assertEquals(0.5, rule.weight());
    }

    @Test
    void testApplicableWithTransactionAboveThreshold() {
        Rule rule = new LocationsRule(3,0.5);
        List<Transaction> transactions = List.of(
            new Transaction("1","account1",
                312.0,
                LocalDateTime.now(), "San Diego", Channel.BRANCH),
            new Transaction("2","account2",
                632.0,
                LocalDateTime.now(),"San Diego RandomV2", Channel.BRANCH),
            new Transaction("3","account3",
                352.0,
                LocalDateTime.now(),"San Diego Maradona", Channel.ATM),
            new Transaction("4","account4",
                342.0,
                LocalDateTime.now(),"San Diego Random", Channel.ONLINE)
        );
        assertTrue(rule.applicable(transactions));
        assertEquals(0.5, rule.weight());
    }
}
