package oms;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import com.wellsfargo.oms.OrderManagementSystem;
import com.wellsfargo.oms.exception.OrderGenerationException;

@JdbcTest
@Sql({ "/schema-withTransactions.sql" })
public class TestOrderGeneration {

    private final OrderManagementSystem oms;

    @Autowired
    public TestOrderGeneration(JdbcTemplate jdbcTemplate) {
        this.oms = new OrderManagementSystem(jdbcTemplate);
    }

    @Test
    void testTransactionCreation_AAA() throws OrderGenerationException, IOException {
        final String outputFileName = "target/temp/actual";
        final int generateAAAOrders = oms.generateAAAOrders(outputFileName);

        final String actualOutput = Files.readString(Paths.get(outputFileName + ".aaa"));
        assertEquals(1, generateAAAOrders);
        assertEquals(Files.readString(Paths.get("src/test/resources/a.aaa")), actualOutput);
    }

    @Test
    void testTransactionCreation_BBB() throws OrderGenerationException, IOException {
        final String outputFileName = "target/temp/actual";
        final int generateAAAOrders = oms.generateBBBOrders(outputFileName);

        final String actualOutput = Files.readString(Paths.get(outputFileName + ".bbb"));
        assertEquals(1, generateAAAOrders);
        assertEquals(Files.readString(Paths.get("src/test/resources/b.bbb")), actualOutput);
    }

    @Test
    void testTransactionCreation_CCC() throws OrderGenerationException, IOException {
        final String outputFileName = "target/temp/actual";
        final int generateAAAOrders = oms.generateCCCOrders(outputFileName);

        final String actualOutput = Files.readString(Paths.get(outputFileName + ".ccc"));
        assertEquals(1, generateAAAOrders);
        assertEquals(Files.readString(Paths.get("src/test/resources/c.ccc")), actualOutput);
    }

}
