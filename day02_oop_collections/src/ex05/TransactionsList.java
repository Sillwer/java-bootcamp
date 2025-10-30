package ex05;

import java.util.UUID;

interface TransactionsList {
    void addTransaction(Transaction transaction);

    Transaction removeTransaction(UUID id) throws TransactionNotFoundException;

    Transaction[] toArray();

    Transaction getTransactionByID(UUID transactionID);
}
