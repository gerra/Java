package ru.ifmo.ctddev.berezhko.rmi;

import ru.ifmo.ctddev.berezhko.rmi.ifaces.Account;
import ru.ifmo.ctddev.berezhko.rmi.ifaces.Bank;
import ru.ifmo.ctddev.berezhko.rmi.ifaces.Person;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 13.05.15.
 */
public class BankImpl implements Bank {
    private final Map<String, Account> accounts = new HashMap<>();
    private final Map<String, Person> persons = new HashMap<>();
    private final int port;

    public BankImpl(int port) {
        this.port = port;
    }

    @Override
    public Account createAccount(String var1) throws RemoteException {
        AccountImpl var2 = new AccountImpl(var1);
        this.accounts.put(var1, var2);
        UnicastRemoteObject.exportObject(var2, this.port);
        return var2;
    }

    @Override
    public Account getAccount(String accountId) {
        return accounts.get(accountId);
    }

    @Override
    public Person getPerson(String passportId) {
        return persons.get(passportId);
    }

    @Override
    public void addPerson(Person person) throws RemoteException {
        persons.put(person.getPassportId(), person);
    }
}
