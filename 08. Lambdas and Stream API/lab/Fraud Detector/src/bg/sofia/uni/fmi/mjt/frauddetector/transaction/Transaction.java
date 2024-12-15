package bg.sofia.uni.fmi.mjt.frauddetector.transaction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record Transaction(String transactionID, String accountID, double transactionAmount,
                          LocalDateTime transactionDate, String location, Channel channel) {
    static final int ZERO = 0;
    static final int FIRST = 1;
    static final int SECOND = 2;
    static final int THIRD = 3;
    static final int FORTH = 4;
    static final int FIVE = 5;
    static final int SIX = 6;
    static final int MAX_TRANSACTION_AMOUNT = 99999999;

    public static Transaction of(String line) {
        try {
            String[] spllited = line.split(",");
            if (spllited.length != SIX) {
                throw new IllegalArgumentException("There is error in CSV line");
            }
            String transactionID = spllited[ZERO];
            String accountId = spllited[FIRST];
            double transactionAmount = Double.parseDouble(spllited[SECOND]);
            if (transactionAmount == 0 || transactionAmount >= MAX_TRANSACTION_AMOUNT) {
                throw new IllegalArgumentException("Amount cannot be 0 or higher than 999999999");
            }
            DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime transactionDate = LocalDateTime.parse(spllited[THIRD], format);

            String location = spllited[FORTH];
            Channel channel = strToChannel(spllited[FIVE]);
            return new Transaction(transactionID, accountId, transactionAmount, transactionDate, location, channel);
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("Error parsing transaction: " + line, e);
        }

    }

    private static Channel strToChannel(String channel) {
        return switch (channel) {
            case "ATM" -> Channel.ATM;
            case "Online" -> Channel.ONLINE;
            case "Branch" -> Channel.BRANCH;
            default -> throw new IllegalStateException("Unexpected value: " + channel);
        };
    }
}
