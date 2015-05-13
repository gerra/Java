package ru.ifmo.ctddev.berezhko.rmi.ifaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by root on 13.05.15.
 */
public interface Account extends Remote {
    String getId() throws RemoteException;

    long getAmount() throws RemoteException;

    void changeAmount(long add) throws RemoteException;
}
