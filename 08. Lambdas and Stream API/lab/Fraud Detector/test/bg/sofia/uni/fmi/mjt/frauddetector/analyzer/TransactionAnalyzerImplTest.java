package bg.sofia.uni.fmi.mjt.frauddetector.analyzer;

import bg.sofia.uni.fmi.mjt.frauddetector.rule.Rule;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Channel;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.Reader;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TransactionAnalyzerImplTest {

    private static final String VALID_CSV = """
        TransactionID,AccountID,TransactionAmount,TransactionDate,Location,Channel
        TX001,ACC001,100.50,2024-01-01 10:00:00,Location1,ATM
        TX002,ACC001,200.75,2024-01-01 11:00:00,Location2,Branch
        TX003,ACC002,300.25,2024-01-01 12:00:00,Location3,Online
        TX004,ACC002,400.00,2024-01-01 13:00:00,Location4,ATM
        """;

    private Rule mockRule;
    private Reader reader;

    @BeforeEach
    void setUp() {

        reader = new StringReader(VALID_CSV);
        mockRule = mock(Rule.class);
    }

    @Test
    void testConstructorWithNullReaderThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new TransactionAnalyzerImpl(null, List.of(mockRule)),
            "Constructor should throw IllegalArgumentException when Reader is null");
    }

    @Test
    void testConstructorWithNullRulesThrowsException() {

        assertThrows(IllegalArgumentException.class, () -> new TransactionAnalyzerImpl(reader, null),
            "Constructor should throw IllegalArgumentException when Rules list is null");
    }

    @Test
    void testConstructorWithInvalidRuleWeightsThrowsException() {

        Rule invalidMockRule = mock(Rule.class);
        when(invalidMockRule.weight()).thenReturn(0.8);
        assertThrows(IllegalArgumentException.class,
            () -> new TransactionAnalyzerImpl(reader, List.of(mockRule, invalidMockRule)),
            "Constructor should throw IllegalArgumentException when Rule weights sum exceeds 1.0");
    }

    @Test
    void testConstructorWithGreaterRuleWeightsThrowsException() {

        Rule invalidMockRule = mock(Rule.class);
        when(invalidMockRule.weight()).thenReturn(0.8);
        when(mockRule.weight()).thenReturn(0.3);
        assertThrows(IllegalArgumentException.class,
            () -> new TransactionAnalyzerImpl(reader, List.of(mockRule, invalidMockRule)),
            "Constructor should throw IllegalArgumentException when Rule weights sum exceeds 1.0");
    }

    @Test
    void testAllTransactionsReturnsCorrectList() {

        when(mockRule.weight()).thenReturn(1.00);
        TransactionAnalyzer analyzer = new TransactionAnalyzerImpl(reader, List.of(mockRule));

        List<Transaction> transactions = analyzer.allTransactions();
        List<Transaction> expectedTransactions = List.of(
            new Transaction("TX001", "ACC001", 100.50,
                LocalDateTime.of(2024, 1, 1, 10, 0), "Location1", Channel.ATM),
            new Transaction("TX002", "ACC001", 200.75,
                LocalDateTime.of(2024, 1, 1, 11, 0), "Location2", Channel.BRANCH),
            new Transaction("TX003", "ACC002", 300.25,
                LocalDateTime.of(2024, 1, 1, 12, 0), "Location3", Channel.ONLINE),
            new Transaction("TX004", "ACC002", 400.00,
                LocalDateTime.of(2024, 1, 1, 13, 0), "Location4", Channel.ATM)
        );

        assertEquals(expectedTransactions.size(), transactions.size(),
            "Expected 4 transactions loaded from the dataset");
        assertEquals(expectedTransactions, transactions, "Loaded transactions should match the expected transactions");
        assertIterableEquals(expectedTransactions, transactions, "Loaded transactions must match each other");
    }

    @Test
    void testallAccountIDsReturnsUniqueID() {
        when(mockRule.weight()).thenReturn(1.00);

        TransactionAnalyzer analyzer = new TransactionAnalyzerImpl(reader, List.of(mockRule));
        List<String> transactions = analyzer.allAccountIDs();
        assertEquals(2, transactions.size(), "AllAccountsIDS doesnt work properly");
        assertEquals("ACC001", transactions.get(0), "AllAccountsIDS doesnt work properly");
        assertEquals("ACC002", transactions.get(1), "AllAccountsIDS doesnt work properly");
        verify(mockRule).weight();
    }

    @Test
    void testTransactionCountByChannelWorkingCorrectly() {
        Map<Channel, Integer> expected = Map.of(Channel.ATM, 2, Channel.BRANCH, 1, Channel.ONLINE, 1);
        when(mockRule.weight()).thenReturn(1.00);

        TransactionAnalyzer analyzer = new TransactionAnalyzerImpl(reader, List.of(mockRule));
        Map<Channel, Integer> output = analyzer.transactionCountByChannel();
        assertEquals(expected, output, "Channel grouping isnt working correctly");
    }

    @Test
    void testAmountSpentByUserWithNullAccountID() {
        when(mockRule.weight()).thenReturn(1.00);

        TransactionAnalyzer analyzer = new TransactionAnalyzerImpl(reader, List.of(mockRule));
        assertThrows(IllegalArgumentException.class, () -> analyzer.amountSpentByUser(null),
            "It should throw IllegelArgument Exception for null accountID");
    }

    @Test
    void testAmountSpentByUserWithEmptyAccountID() {
        when(mockRule.weight()).thenReturn(1.00);

        TransactionAnalyzer analyzer = new TransactionAnalyzerImpl(reader,
            List.of(mockRule));
        assertThrows(IllegalArgumentException.class, () -> analyzer.amountSpentByUser(""),
            "It should throw IllegelArgument Exception for empty accountID");
    }

    @Test
    void testAmountSpentByUserCalculatesCorrectly() {
        when(mockRule.weight()).thenReturn(1.00);

        TransactionAnalyzer analyzer = new TransactionAnalyzerImpl(reader, List.of(mockRule));
        assertEquals(100.50 + 200.75, analyzer.amountSpentByUser("ACC001"));
        assertEquals(300.25 + 400, analyzer.amountSpentByUser("ACC002"));
    }

    @Test
    void testAllTransactionsByUserWithNull() {
        when(mockRule.weight()).thenReturn(1.00);

        TransactionAnalyzer analyzer = new TransactionAnalyzerImpl(reader, List.of(mockRule));
        assertThrows(IllegalArgumentException.class,
            () -> analyzer.allTransactionsByUser(null),
            "It should throw IllegelArgument Exception for null accountID");

    }

    @Test
    void testAllTransactionsByUserWithEmpty() {
        when(mockRule.weight()).thenReturn(1.00);

        TransactionAnalyzer analyzer = new TransactionAnalyzerImpl(reader, List.of(mockRule));
        assertThrows(IllegalArgumentException.class,
            () -> analyzer.allTransactionsByUser(""),
            "It should throw IllegelArgument Exception for empty accountID");

    }

    @Test
    void testAllTransactionsByUserCorrectly() {
        when(mockRule.weight()).thenReturn(1.00);

        TransactionAnalyzer analyzer = new TransactionAnalyzerImpl(reader,
            List.of(mockRule));
        List<Transaction> user1 = analyzer.allTransactionsByUser("ACC001");
        List<Transaction> user2 = analyzer.allTransactionsByUser("ACC002");
        assertEquals(2, user1.size(), "Expected 2 transactions for user1");
        assertEquals(2, user2.size(), "Expected 2 transactions for user2");
        assertEquals(100.50, user1.get(0).transactionAmount(), "Expected 100.50 transitionAmount from user1[0] ");

    }

    @Test
    void testAccountRatingWithNullAccountId() {
        when(mockRule.weight()).thenReturn(1.00);

        TransactionAnalyzer analyzer = new TransactionAnalyzerImpl(reader, List.of(mockRule));
        assertThrows(IllegalArgumentException.class,
            () -> analyzer.accountRating(null),
            "It should throw IllegelArgument Exception for null accountID");
    }

    @Test
    void testAccountRatingWithEmptyAccountId() {
        when(mockRule.weight()).thenReturn(1.00);

        TransactionAnalyzer analyzer = new TransactionAnalyzerImpl(reader, List.of(mockRule));
        assertThrows(IllegalArgumentException.class,
            () -> analyzer.accountRating(""),
            "It should throw IllegelArgument Exception for empty accountID");

    }

    @Test
    void testAccountRatingWithZeroAccountIdOccurencies() {
        when(mockRule.weight()).thenReturn(1.00);

        TransactionAnalyzer analyzer = new TransactionAnalyzerImpl(reader, List.of(mockRule));
        assertEquals(0, analyzer.accountRating("RandomIdDoesntExits"),
            "It should throw IllegalArgument Exception for null accountID");
    }

    @Test
    void testAccountRatingCorrectly() {
        when(mockRule.weight()).thenReturn(0.3);

        Rule secondRule = mock(Rule.class);
        when(secondRule.weight()).thenReturn(0.5);

        Rule thirdRule = mock(Rule.class);
        when(thirdRule.weight()).thenReturn(0.2);
        TransactionAnalyzer analyzer = new TransactionAnalyzerImpl(reader, List.of(mockRule, secondRule, thirdRule));
        List<Transaction> transactions = analyzer.allTransactions().stream()
            .filter(e -> e.accountID().equals("ACC001"))
            .toList();
        when(mockRule.applicable(transactions)).thenReturn(true);
        when(secondRule.applicable(transactions)).thenReturn(true);
        assertEquals(0.8, analyzer.accountRating("ACC001"));
        assertNotEquals(0.8, analyzer.accountRating("ACC002"));

    }

    @Test
    void testAccountsRiskSortsCorrectly() {
        when(mockRule.weight()).thenReturn(0.3);

        Rule secondRule = mock(Rule.class);
        when(secondRule.weight()).thenReturn(0.5);

        Rule thirdRule = mock(Rule.class);
        when(thirdRule.weight()).thenReturn(0.2);
        TransactionAnalyzer analyzer = new TransactionAnalyzerImpl(reader, List.of(mockRule, secondRule, thirdRule));
        List<Transaction> transactionsUser1 = analyzer.allTransactions().stream()
            .filter(e -> e.accountID().equals("ACC001"))
            .toList();
        when(mockRule.applicable(transactionsUser1)).thenReturn(true);
        when(secondRule.applicable(transactionsUser1)).thenReturn(true);

        List<Transaction> transactionsUser2 = analyzer.allTransactions().stream()
            .filter(e -> e.accountID().equals("ACC002"))
            .toList();
        when(secondRule.applicable(transactionsUser2)).thenReturn(true);
        when(thirdRule.applicable(transactionsUser2)).thenReturn(true);
        assertEquals(0.7, analyzer.accountRating("ACC002"),"Expected 0.7 for account rating on ACC002");
        assertEquals(0.8, analyzer.accountRating("ACC001"),"Expected 0.8 for account rating on ACC001");

        SortedMap<String,Double> sortedRatings = analyzer.accountsRisk();
        assertEquals(2,sortedRatings.size(),"Expected size to be a 2");
        assertEquals("ACC001",sortedRatings.firstKey(),"Expected ACC001 to be in the first place in the map");
        assertEquals(0.7,sortedRatings.get("ACC002"),"Expected 0.7 for account rating on ACC002");
    }

}
