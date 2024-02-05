package com.smallworld.data;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smallworld.TransactionDataFetcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TransactionDataFetcherTest {


    private List<Transaction> transactions;
    @BeforeEach
    void setUp() throws IOException {
        // Read transactions from the transactions.json file
        String jsonContent = new String(Files.readAllBytes(Paths.get("transactions.json")));
        ObjectMapper objectMapper = new ObjectMapper();
        transactions = objectMapper.readValue(jsonContent, new TypeReference<List<Transaction>>() {});
    }

    @Test
    void Get_Total_TransactionAmountTest() {
        TransactionDataFetcher dataFetcher = new TransactionDataFetcher(transactions);
        double totalAmount = dataFetcher.getTotalTransactionAmount();
        assertEquals(4371.37, totalAmount);
    }

    @Test
    void Get_Total_TransactionAmount_SentByTest() {
        TransactionDataFetcher dataFetcher = new TransactionDataFetcher(transactions);
        double totalAmountSentByTom = dataFetcher.getTotalTransactionAmountSentBy("Tom Shelby");
        assertEquals(828.26, totalAmountSentByTom, 0.001);
    }

    @Test
    void Get_Max_TransactionAmountTest() {
        TransactionDataFetcher dataFetcher = new TransactionDataFetcher(transactions);
        double maxAmount = dataFetcher.getMaxTransactionAmount();
        assertEquals(985.0, maxAmount, 0.001);
    }

    @Test
    void Count_UniqueClientsTest() {
        TransactionDataFetcher dataFetcher = new TransactionDataFetcher(transactions);
        long uniqueClientsCount = dataFetcher.countUniqueClients();
        assertEquals(14, uniqueClientsCount);
    }

    @Test
    void HasOpenComplianceIssuesTest() {
        TransactionDataFetcher dataFetcher = new TransactionDataFetcher(transactions);
        boolean hasOpenComplianceIssues = dataFetcher.hasOpenComplianceIssues("Tom Shelby");
        assertTrue(hasOpenComplianceIssues);
    }

    @Test
    void Get_Transactions_ByBeneficiaryNameTest() {
        TransactionDataFetcher dataFetcher = new TransactionDataFetcher(transactions);
        Map<String, Transaction> transactionsByBeneficiary = dataFetcher.getTransactionsByBeneficiaryName();
        assertEquals(10, transactionsByBeneficiary.size());
      }

    @Test
    void Get_UnsolvedIssue_IdsTest() {
        TransactionDataFetcher dataFetcher = new TransactionDataFetcher(transactions);
        Set<Integer> unsolvedIssueIds = dataFetcher.getUnsolvedIssueIds();
        assertEquals(Set.of(1, 3, 99, 54,15), unsolvedIssueIds);
    }

    @Test
    void GetAll_SolvedIssueMessagesTest() {
        TransactionDataFetcher dataFetcher = new TransactionDataFetcher(transactions);
        List<String> solvedIssueMessages = dataFetcher.getAllSolvedIssueMessages();
        assertEquals(List.of("Never gonna give you up", "Never gonna let you down", "Never gonna run around and desert you"), solvedIssueMessages);
    }

    @Test
    void GetTop3Transactions_ByAmountTest() {
        TransactionDataFetcher dataFetcher = new TransactionDataFetcher(transactions);
        List<Transaction> top3Transactions = dataFetcher.getTop3TransactionsByAmount();
        assertEquals(3, top3Transactions.size());
        assertEquals(985.0, top3Transactions.get(0).getAmount(), 0.001);
        assertEquals(666.0, top3Transactions.get(1).getAmount(), 0.001);
        assertEquals(666.0, top3Transactions.get(2).getAmount(), 0.001);
    }

    @Test
    void Get_TopSenderTest() {
        TransactionDataFetcher dataFetcher = new TransactionDataFetcher(transactions);
        Optional<String> topSender = dataFetcher.getTopSender();
        assertEquals("Grace Burgess", topSender.orElse(null));
    }
}
