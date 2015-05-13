package ru.ifmo.ctddev.berezhko.rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by root on 13.05.15.
 */
public class Server {
    public static void main(String[] args) {
        BankImpl bank = null;
        try {
            bank = new BankImpl(8888);
            UnicastRemoteObject.exportObject(bank, 8888);
            Naming.rebind("bank", bank);

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }
}
