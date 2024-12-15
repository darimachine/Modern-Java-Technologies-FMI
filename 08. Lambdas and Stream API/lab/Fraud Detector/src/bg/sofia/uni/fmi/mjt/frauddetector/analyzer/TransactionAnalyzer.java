package bg.sofia.uni.fmi.mjt.frauddetector.analyzer;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Channel;
import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;

public interface TransactionAnalyzer {

    /**
     * Retrieves all transactions loaded into the analyzer.
     *
     * @return a list of all transactions.
     */
    List<Transaction> allTransactions();

    /**
     * Retrieves all unique account IDs from the loaded transactions.
     *
     * @return a list of unique account IDs as strings.
     */
    List<String> allAccountIDs();

    /**
     * Retrieves a map of transaction counts grouped by the channel of the transaction.
     *
     * @return a map where keys are Channel values and values are the count of transactions for each channel.
     */
    Map<Channel, Integer> transactionCountByChannel();

    /**
     * Calculates the total amount spent by a specific user, identified by their account ID.
     *
     * @param accountID the account ID for which the total amount spent is calculated.
     * @return the total amount spent by the user as a double.
     * @throws IllegalArgumentException if the accountID is null or empty.
     */
    double amountSpentByUser(String accountID);

    /**
     * Retrieves all transactions associated with a specific account ID.
     *
     * @param accountId the account ID for which transactions are retrieved.
     * @return a list of Transaction objects associated with the specified account.
     * @throws IllegalArgumentException if the account ID is null or empty.
     */
    List<Transaction> allTransactionsByUser(String accountId);

    /**
     * Returns the risk rating of an account with the specified ID.
     *
     * @return the risk rating as a double-precision floating-point number in the interval [0.0, 1.0].
     * @throws IllegalArgumentException if the account ID is null or empty.
     */
    double accountRating(String accountId);

    /**
     * Calculates the risk score for each account based on the loaded rules.
     * The score for each account is a double-precision floating-point number in the interval [0, 1] and is
     * formed by summing up the weights of all applicable rules to the account transactions.
     * The result is sorted in descending order, with the highest-risk accounts listed first.
     *
     * @return a map where keys are account IDs (strings) and values are risk scores (doubles).
     */
    SortedMap<String, Double> accountsRisk();

}