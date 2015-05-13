package ru.ifmo.ctddev.berezhko.rmi.ifaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by root on 06.05.15.
 */
public interface Person extends Remote {
    public String getFirstName() throws RemoteException;

    public String getLastName() throws RemoteException;

    public String getPassportId() throws RemoteException;

    public void addAccount(Account account) throws RemoteException;

    public Account getAccount(String accountId) throws RemoteException;
}
