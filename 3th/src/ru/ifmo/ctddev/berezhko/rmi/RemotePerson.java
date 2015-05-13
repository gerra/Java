package ru.ifmo.ctddev.berezhko.rmi;

import ru.ifmo.ctddev.berezhko.rmi.ifaces.Account;
import ru.ifmo.ctddev.berezhko.rmi.ifaces.Person;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by root on 06.05.15.
 */
public class RemotePerson /*extends PersonAC*/ implements Person, Serializable {
//    public RemotePerson(String firstName, String lastName, String passportId) {
//        super(firstName, lastName, passportId);
//    }
protected String firstName;
    protected String lastName;
    protected String passportId;

    private Map<String, Account> accounts = new HashMap<>();

    public RemotePerson(String firstName, String lastName, String passportId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.passportId = passportId;
    }


    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPassportId() {
        return passportId;
    }

    public void addAccount(Account account) {
        try {
            accounts.put(account.getId(), account);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public Account getAccount(String accountId) {
        return accounts.get(accountId);
    }
}
