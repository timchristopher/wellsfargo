package com.wellsfargo.oms;

import java.sql.SQLException;

import org.h2.tools.Csv;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.wellsfargo.oms.exception.OrderGenerationException;

@Service
public class OrderManagementSystem {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderManagementSystem.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public OrderManagementSystem(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int processTransactionFile(String fileName) throws OrderGenerationException {
        final String sql = """
                INSERT INTO TRANSACTION
                SELECT *
                FROM CSVREAD('%s', null, 'charset=UTF-8 fieldSeparator=,')
                """;
        try {
            return jdbcTemplate.update(String.format(sql, fileName));
        } catch (Exception e) {
            throw new OrderGenerationException(e);
        }
    }

    /**
     * Generate orders based on the requirement for the AAA OMS.
     *
     * @param outputFileName the name of the file to be written.
     * @return the number of rows written.
     * @throws OrderGenerationException
     */
    public int generateAAAOrders(String outputFileName) throws OrderGenerationException {
        try {
            final String outputFilePath = outputFileName + ".aaa";
            final String sql = """
                    SELECT
                      S.ISIN, P.PORTFOLIOCODE, T.NOMINAL, T.TRANSACTIONTYPE
                    FROM
                      TRANSACTION T, PORTFOLIO P, SECURITY S
                    WHERE
                      P.PORTFOLIOID = T.PORTFOLIOID AND S.SECURITYID = T.SECURITYID AND T.OMS = 'AAA'
                    """;

            final Csv csv = new Csv();
            csv.setFieldDelimiter('\0');
            csv.setFieldSeparatorWrite(",");
            csv.setWriteColumnHeader(true);
            final int writeCount = csv.write(jdbcTemplate.getDataSource().getConnection(), outputFilePath, sql, null);
            LOGGER.info("AAA - Wrote " + writeCount + " order(s) to " + outputFilePath);
            return writeCount;
        } catch (SQLException e) {
            throw new OrderGenerationException(e);
        }
    }

    /**
     * Generate orders based on the requirement for the BBB OMS.
     *
     * @param outputFileName the name of the file to be written.
     * @return the number of rows written.
     * @throws OrderGenerationException
     */
    public int generateBBBOrders(String outputFileName) throws OrderGenerationException {
        try {
            final String outputFilePath = outputFileName + ".bbb";
            final String sql = """
                    SELECT
                      S.CUSIP, P.PORTFOLIOCODE, T.NOMINAL, T.TRANSACTIONTYPE
                    FROM
                      TRANSACTION T, PORTFOLIO P, SECURITY S
                    WHERE
                      P.PORTFOLIOID = T.PORTFOLIOID AND S.SECURITYID = T.SECURITYID AND T.OMS = 'BBB'
                    """;

            final Csv csv = new Csv();
            csv.setFieldDelimiter('\0');
            csv.setFieldSeparatorWrite("|");
            csv.setWriteColumnHeader(true);
            final int writeCount = csv.write(jdbcTemplate.getDataSource().getConnection(), outputFilePath, sql, null);
            LOGGER.info("BBB - Wrote " + writeCount + " order(s) to " + outputFilePath);
            return writeCount;
        } catch (SQLException e) {
            throw new OrderGenerationException(e);
        }
    }

    /**
     * Generate orders based on the requirement for the CCC OMS.
     *
     * @param outputFileName the name of the file to be written.
     * @return the number of rows written.
     * @throws OrderGenerationException
     */
    public int generateCCCOrders(String outputFileName) throws OrderGenerationException {
        try {
            final String outputFilePath = outputFileName + ".ccc";
            final String sql = """
                    SELECT
                      P.PORTFOLIOCODE, S.TICKER, T.NOMINAL, T.TRANSACTIONTYPE
                    FROM
                      TRANSACTION T, PORTFOLIO P, SECURITY S
                    WHERE
                      P.PORTFOLIOID = T.PORTFOLIOID AND S.SECURITYID = T.SECURITYID AND T.OMS = 'CCC'
                    """;

            final Csv csv = new Csv();
            csv.setFieldDelimiter('\0');
            csv.setFieldSeparatorWrite(",");
            csv.setWriteColumnHeader(false);
            final int writeCount = csv.write(jdbcTemplate.getDataSource().getConnection(), outputFilePath, sql, null);
            LOGGER.info("CCC - Wrote " + writeCount + " order(s) to " + outputFilePath);
            return writeCount;
        } catch (SQLException e) {
            throw new OrderGenerationException(e);
        }
    }

}
