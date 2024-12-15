//import bg.sofia.uni.fmi.mjt.frauddetector.analyzer.TransactionAnalyzer;
//import bg.sofia.uni.fmi.mjt.frauddetector.analyzer.TransactionAnalyzerImpl;
//import bg.sofia.uni.fmi.mjt.frauddetector.analyzer.TransactionAnalyzerPrinter;
//import bg.sofia.uni.fmi.mjt.frauddetector.rule.FrequencyRule;
//import bg.sofia.uni.fmi.mjt.frauddetector.rule.LocationsRule;
//import bg.sofia.uni.fmi.mjt.frauddetector.rule.Rule;
//import bg.sofia.uni.fmi.mjt.frauddetector.rule.SmallTransactionsRule;
//import bg.sofia.uni.fmi.mjt.frauddetector.rule.ZScoreRule;
//import bg.sofia.uni.fmi.mjt.frauddetector.transaction.Transaction;
//
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.Reader;
//import java.text.MessageFormat;
//import java.time.Period;
//import java.util.List;
//
////TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
//// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
//public class Main {
//
//    public static void printTransactions(List<Transaction> transactions) {
//        for (var el : transactions) {
//            System.out.println(MessageFormat.format("{0}, {1}, {2}, {3}, {4}, {5}", el.transactionID(),
//                el.accountID(), el.transactionAmount(),
//                el.transactionDate().toString(), el.location(), el.channel().toString()));
//        }
//    }
//
//    public static void main(String[] args) throws FileNotFoundException {
//
//        String filePath = "dataset.csv";
//
//        Reader reader = new FileReader("src/dataset.csv");
//        List<Rule> rules = List.of(
//            new ZScoreRule(1.5, 0.3),
//            new LocationsRule(3, 0.4),
//            new FrequencyRule(4, Period.ofWeeks(4), 0.25),
//            new SmallTransactionsRule(1, 10.20, 0.05)
//        );
//
//        TransactionAnalyzer analyzer = new TransactionAnalyzerImpl(reader, rules);
//        printTransactions(analyzer.allTransactions());
//
//        System.out.println(analyzer.allAccountIDs());
//        System.out.println(analyzer.allTransactionsByUser(analyzer.allTransactions().getFirst().accountID()));
//        System.out.println(analyzer.accountsRisk());
//
//    }
//}