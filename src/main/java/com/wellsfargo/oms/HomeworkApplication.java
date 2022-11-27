package com.wellsfargo.oms;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.wellsfargo.oms.exception.OrderGenerationException;

@SpringBootApplication
public class HomeworkApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(HomeworkApplication.class);

    @Autowired
    private OrderManagementSystem oms;

    @Autowired
    private ApplicationContext appContext;

    public static void main(String[] args) {
        LOGGER.info("Use -DtxnFile to specify a transaction file.");
        SpringApplication.run(HomeworkApplication.class, args);
    }

    @PostConstruct
    public void executeHomeworkTest() throws OrderGenerationException {
        final String fileName = System.getProperty("txnFile", "classpath:transactions.csv");
        LOGGER.info("Processing using the following transaction file: " + fileName);

        final String outputfileName = "oms-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));

        oms.processTransactionFile(fileName);
        final int numberAaaOrders = oms.generateAAAOrders(outputfileName);
        final int numberBbbOrders = oms.generateBBBOrders(outputfileName);
        final int numberCccOrders = oms.generateCCCOrders(outputfileName);

        LOGGER.info("Generated " + numberAaaOrders + " AAA order(s)");
        LOGGER.info("Generated " + numberBbbOrders + " BBB order(s)");
        LOGGER.info("Generated " + numberCccOrders + " CCC order(s)");

        new Thread(() -> {
            LOGGER.info("Existing application.");
            SpringApplication.exit(appContext, () -> 0);
        }).start();
    }

}