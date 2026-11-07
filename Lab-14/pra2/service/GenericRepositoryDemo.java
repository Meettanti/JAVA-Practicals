package service;

import java.util.List;
import model.Account;
import model.CurrentAccount;
import model.SavingsAccount;
import exception.InvalidAmountException;

public class GenericRepositoryDemo {
    public static void main(String[] args) throws InvalidAmountException {
        GenericRepository<Account> repository = new GenericRepository<>(Account.class);
        Account savings = new SavingsAccount("Repository Savings", 1000, 0);
        Account current = new CurrentAccount("Repository Current", 500, 100);

        repository.create(savings);
        repository.create(current);
        savings.deposit(25);
        repository.update(savings);

        List<Account> selected = repository.findByIds(
                current.getAccountNumber(), savings.getAccountNumber());
        if (selected.size() != 2 || selected.get(0) != current || selected.get(1) != savings) {
            throw new IllegalStateException("Varargs lookup did not preserve requested ID order.");
        }

        Account loaded = repository.readById(savings.getAccountNumber()).orElseThrow();
        if (loaded.getBalance() != 1025) {
            throw new IllegalStateException("Repository update was not retained.");
        }
        repository.delete(current.getAccountNumber()).orElseThrow();
        if (repository.size() != 1) {
            throw new IllegalStateException("Repository delete did not remove the entity.");
        }

        System.out.println("Generic repository CRUD and varargs lookup passed.");
        System.out.println("Remaining account: " + repository.findAll().get(0));
    }
}
