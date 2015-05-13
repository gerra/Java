package ru.ifmo.ctddev.berezhko.rmi.ifaces;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 13.05.15.
 */
public abstract class PersonAC implements Person {
    protected String firstName;
    protected String lastName;
    protected String passportId;

    private Map<String, Account> accounts = new HashMap<>();

    public PersonAC(String firstName, String lastName, String passportId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.passportId = passportId;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    @Override
    public String getPassportId() {
        return passportId;
    }

    @Override
    public void addAccount(Account account) {
        try {
            accounts.put(account.getId(), account);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Account getAccount(String accountId) {
        return accounts.get(accountId);
    }
}
