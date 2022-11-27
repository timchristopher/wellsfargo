package oms;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import com.wellsfargo.oms.OrderManagementSystem;
import com.wellsfargo.oms.exception.OrderGenerationException;

@JdbcTest
@Sql({ "/schema-withoutTransactions.sql" })
public class TestTransactionGeneration {

    private final OrderManagementSystem oms;

    @Autowired
    public TestTransactionGeneration(JdbcTemplate jdbcTemplate) {
        this.oms = new OrderManagementSystem(jdbcTemplate);
    }

    @Test
    void testTransactionGeneration() throws OrderGenerationException {
        int processTransactionFile = oms.processTransactionFile("classpath:transactions.csv");
        assertEquals(3, processTransactionFile);
    }

}
