package ex03;

import java.util.UUID;

public class TransactionsLinkedList implements TransactionsList {
    ListNode root;
    ListNode tail;
    Integer count = 0;

    public void addTransaction(Transaction transaction) {
        ListNode newNode = new ListNode(transaction);
        if (count == 0) {
            root = newNode;
        } else {
            ListNode.addAfter(tail, newNode);
        }
        tail = newNode;
        count++;
    }

    public void removeTransaction(UUID id) throws TransactionNotFoundException {
        boolean transactionFound = false;

        for (ListNode node = root; node != null; node = node.next) {
            if (node.transaction.getId().equals(id)) {
                transactionFound = true;
                node.deleteNode();
                count--;
                break;
            }
        }

        if (!transactionFound) {
            throw new TransactionNotFoundException("Transaction with UUID=" + id + " not found");
        }
    }

    public Transaction[] toArray() {
        Transaction[] tr = new Transaction[count];
        int i = 0;
        for (ListNode node = root; node != null; node = node.next) {
            tr[i++] = node.transaction;
        }

        return tr;
    }

    static class ListNode {
        Transaction transaction;
        ListNode prev;
        ListNode next;

        ListNode(Transaction transaction) {
            this.transaction = transaction;
        }

        static void addAfter(ListNode prev, ListNode node) {
            if (prev == null || node == null) {
                return;
            }

            ListNode nextNode = prev.next;
            prev.next = node;
            if (nextNode != null) {
                nextNode.prev = node;
            }

            node.prev = prev;
            node.next = nextNode;
        }

        void deleteNode() {
            ListNode prevNode = this.prev;
            ListNode nextNode = this.next;

            if (prevNode != null) {
                prevNode.next = nextNode;
            }
            if (nextNode != null) {
                nextNode.prev = prevNode;
            }
        }
    }
}
