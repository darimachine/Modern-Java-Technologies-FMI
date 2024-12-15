package bg.sofia.uni.fmi.mjt.frauddetector.transaction;


import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TransactionTest {

    @Test
    void testTransactionLineWithInvalidLength() {
        String line = "ufuefue,aejakfe";
        assertThrows(IllegalArgumentException.class, () -> Transaction.of(line), "Line is invalid length");
    }

    @Test
    void testTransactionLineWithInvalidSeperator() {
        String line = "TX000003,AC00019,126.29.2023-07-10 18:16:08,Mesa,Online";
        String line1 = "TX000003 AC00019,126.29,2023-07-10 18:16:08,Mesa,Online";
        assertThrows(IllegalArgumentException.class, () -> Transaction.of(line), "Incorectly formated CSV file");
        assertThrows(IllegalArgumentException.class,() ->Transaction.of(line),"Incorrectly Formated CSV file");
    }

    @Test
    void testTransactionLineWithInvalidTransactionAmount() {
        String line = "TX000003,AC00019,InvalidAmount,2023-07-10 18:16:08,Mesa,Online";
        assertThrows(IllegalArgumentException.class, () -> Transaction.of(line), "Incorrect transactionAmount parsing");
    }

    @Test
    void testTransactionLineWithInvalidDataFormat() {
        String line = "TX000003,AC00019,126.29,2023/07/10 18:16:08,Mesa,Online";
        assertThrows(IllegalArgumentException.class, () -> Transaction.of(line), "Invalid Data Format");

    }

    @Test
    void testTransactionLineWithInvalidChannel() {
        String line = "TX000003,AC00019,126.29,2023/07/10 18:16:08,Mesa,Invalid";
        assertThrows(IllegalArgumentException.class, () -> Transaction.of(line), "Invalid Channel(Doesnt exists)");
    }


    @Test
    void testTransactionWithZeroAmount() {
        String line = "TX000003,AC00019,0.00,2023-07-10 18:16:08,Mesa,Online";

        assertThrows(IllegalArgumentException.class, () -> Transaction.of(line), "Expected to Throw IllegalArgumentException Transaction Amount is Zero");

    }
    @Test
    void testTransactionLineWithLargeAmount() {
        String line = "TX000003,AC00019,99999999999999999988999999999999999999999999999," +
            "2023-07-10 18:16:08,Mesa,Online";
        assertThrows(IllegalArgumentException.class, () ->Transaction.of(line),"Expected exception overflow TransactionAMount");
    }

    @Test
    void testTransactionWithCorrectLine() {
        String line = "TX000003,AC00019,126.29,2023-07-10 18:16:08,Mesa,Online";
        Transaction transaction = Transaction.of(line);
        assertEquals("TX000003",transaction.transactionID());
        assertEquals("AC00019",transaction.accountID());
        assertEquals(126.29,transaction.transactionAmount());
        assertEquals(LocalDateTime.parse("2023-07-10 18:16:08", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),transaction.transactionDate());
        assertEquals("Mesa",transaction.location());
        assertEquals(Channel.ONLINE,transaction.channel());

    }
}
