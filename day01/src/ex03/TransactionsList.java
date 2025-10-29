package ex03;

import java.util.UUID;

interface TransactionsList {
    void addTransaction(Transaction transaction);

    void removeTransaction(UUID id) throws TransactionNotFoundException;

    Transaction[] toArray();
}
