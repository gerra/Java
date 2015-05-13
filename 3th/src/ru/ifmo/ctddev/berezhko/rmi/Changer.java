package ru.ifmo.ctddev.berezhko.rmi;

import ru.ifmo.ctddev.berezhko.rmi.ifaces.Account;
import ru.ifmo.ctddev.berezhko.rmi.ifaces.Bank;
import ru.ifmo.ctddev.berezhko.rmi.ifaces.Person;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by root on 13.05.15.
 */
public class Changer {
    public static void main(String[] args) {
        Bank bank = null;
        try {
            bank = (Bank) Naming.lookup("bank");
            System.out.println(bank);
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        if (bank == null) {
            return;
        }
        Person person = null;
        try {
            person = bank.getPerson(args[2]);
            if (person == null) {
                person = new RemotePerson(args[0], args[1], args[2]);
                bank.addPerson(person);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        try {
            Account account = person.getAccount(args[3]);
            if (account == null) {
                account = new AccountImpl(args[3]);
            }
            try {
                account.changeAmount(Integer.parseInt(args[4]));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            person.addAccount(account);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
