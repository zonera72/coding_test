package com.smallworld;

import com.smallworld.data.Transaction;

import java.util.*;

public class TransactionDataFetcher {

    private List<Transaction> transactions;

    //  initialize the transactions
    public TransactionDataFetcher(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    /**
     * Returns the sum of the amounts of all transactions
     */
    public double getTotalTransactionAmount() {
        double totalAmount = 0.0;

        for (Transaction transaction : transactions) {
            totalAmount += transaction.getAmount();
        }
        return totalAmount;
    }


    /**
     * Returns the sum of the amounts of all transactions sent by the specified client
     */
    public double getTotalTransactionAmountSentBy(String senderFullName) {

        double totalAmount = 0.0;
        for (Transaction transaction : transactions) {
            if (transaction.getSenderFullName().equals(senderFullName)) {
                totalAmount += transaction.getAmount();
            }
        }
        return totalAmount;
    }

    /**
     * Returns the highest transaction amount
     */
    public double getMaxTransactionAmount() {
        double maxAmount = 0;
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() > maxAmount) {
                maxAmount = transaction.getAmount();
            }
        }
        return maxAmount;
    }

    /**
     * Counts the number of unique clients that sent or received a transaction
     */
    public long countUniqueClients() {
        Set<String> uniqueClients = new HashSet<>();
        for (Transaction transaction : transactions) {
            uniqueClients.add(transaction.getSenderFullName());
            uniqueClients.add(transaction.getBeneficiaryFullName());
        }
        return uniqueClients.size();
    }

    /**
     * Returns whether a client (sender or beneficiary) has at least one transaction with a compliance
     * issue that has not been solved
     */
    public boolean hasOpenComplianceIssues(String clientFullName) {

        for (Transaction transaction : transactions) {
            if((clientFullName.equals(transaction.getSenderFullName()) ||clientFullName.equals(transaction.getBeneficiaryFullName())) && !transaction.getIssueSolved()){
                return true;
            }
        }
        return false;
    }

    /**
     * Returns all transactions indexed by beneficiary name
     */
    public Map<String, Transaction> getTransactionsByBeneficiaryName() {
        Map<String, Transaction> result = new HashMap<>();

        for (Transaction transaction : transactions) {
            String beneficiaryFullName = transaction.getBeneficiaryFullName();
            result.put(beneficiaryFullName, transaction);
        }

        return result;
    }


    /**
     * Returns the identifiers of all open compliance issues
     */
    public Set<Integer> getUnsolvedIssueIds() {
        Set<Integer> set = new HashSet<>();
        for (Transaction transaction : transactions) {
            if (!Boolean.TRUE.equals(transaction.getIssueSolved())) {
                set.add(transaction.getIssueId());
            }
        }
        return set;
 }

    /**
     * Returns a list of all solved issue messages
     */
    public List<String> getAllSolvedIssueMessages() {
        List<String> list = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if(Boolean.TRUE.equals(transaction.getIssueSolved()) && Objects.nonNull(transaction.getIssueMessage())){
                list.add(transaction.getIssueMessage());
            }
        }
        return list;
    }

    /**
     * Returns the 3 transactions with the highest amount sorted by amount descending
     */
    public List<Transaction> getTop3TransactionsByAmount() {

        Collections.sort(transactions,Collections.reverseOrder());
        List<Transaction> list = new ArrayList<>();
        for (int i = 0; i < 3 && i < transactions.size(); i++) {
            list.add(transactions.get(i));
        }
        return list;
    }

    /**
     * Returns the senderFullName of the sender with the most total sent amount
     */
    public Optional<String> getTopSender() {
        Map<String, Double> senderTotalAmounts = new HashMap<>();

        for (Transaction transaction : transactions){
            String senderFullName = transaction.getSenderFullName();
            double currentTotalAmount = senderTotalAmounts.getOrDefault(senderFullName, 0.0);
            double newAmount = transaction.getAmount();
            senderTotalAmounts.put(senderFullName, currentTotalAmount + newAmount);
        }

        Optional<Map.Entry<String, Double>> maxEntry = senderTotalAmounts.entrySet().stream()
                .max(Map.Entry.comparingByValue());

        return maxEntry.map(x->x.getKey());
    }


}
