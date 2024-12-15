package bg.sofia.uni.fmi.mjt.frauddetector.rule;

import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;

import java.util.List;

/**
 * Represents a generic rule that can be applied to a list of transactions.
 * This interface defines the structure for determining the applicability of
 * a rule and its associated weight. If you want to dig deeper,
 * you can check <a href="https://medium.com/swlh/rules-pattern-1c59854547b">this article</a>
 * on the Rules pattern.
 */
public interface Rule {

    /**
     * Determines whether the rule is applicable based on the given list of transactions.
     *
     * @param transactions the list of objects to evaluate.
     *                     These transactions are used to determine if the rule
     *                     conditions are satisfied.
     * @return true, if the rule is applicable based on the transactions.
     */
    boolean applicable(List<Transaction> transactions);

    /**
     * Retrieves the weight of the rule.
     * The weight represents the importance or priority of the rule
     * and is a double-precision floating-point number in the interval [0, 1].
     *
     * @return the weight of the rule.
     */
    double weight();

}