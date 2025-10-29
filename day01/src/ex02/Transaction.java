package ex02;

import java.util.UUID;

public class Transaction {
    enum TransferCategory {
        DEBIT, CREDIT
    }

    private final UUID id;
    private final User sender;
    private final User recipient;
    private final TransferCategory category;
    private final Integer amount;

    private Transaction(UUID id, User sender, User recipient, TransferCategory category, Integer amount) {
        this.id = id;
        this.sender = sender;
        this.recipient = recipient;
        this.category = category;
        this.amount = amount;
    }

    static Transaction[] createTransaction(User sender, User recipient, Integer amount) {
        Transaction[] tr = new Transaction[2];

        if (sender.getBalance() < amount || amount < 1) {
            return null;
        }

        UUID id = UUID.randomUUID();
        tr[0] = new Transaction(id, sender, recipient, TransferCategory.CREDIT, -amount);
        tr[1] = new Transaction(id, recipient, sender, TransferCategory.DEBIT, amount);

        return tr;
    }

    public UUID getId() {
        return id;
    }

    public User getSender() {
        return sender;
    }

    public User getRecipient() {
        return recipient;
    }

    public TransferCategory getCategory() {
        return category;
    }

    public Integer getAmount() {
        return amount;
    }
}
