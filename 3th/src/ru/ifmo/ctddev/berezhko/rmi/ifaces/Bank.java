package ru.ifmo.ctddev.berezhko.rmi.ifaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by root on 13.05.15.
 */
public interface Bank extends Remote {
    Account createAccount(String accountId) throws RemoteException;

    Account getAccount(String accountId) throws RemoteException;

    Person getPerson(String passportId) throws RemoteException;

    void addPerson(Person person) throws RemoteException;
}
