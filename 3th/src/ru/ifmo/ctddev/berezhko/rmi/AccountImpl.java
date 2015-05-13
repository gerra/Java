package ru.ifmo.ctddev.berezhko.rmi;

import ru.ifmo.ctddev.berezhko.rmi.ifaces.Account;

/**
 * Created by root on 13.05.15.
 */
public class AccountImpl implements Account {
    private String id;
    private long amount;

    public AccountImpl(String id) {
        this.id = id;
        amount = 0;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public long getAmount() {
        return amount;
    }

    @Override
    public void changeAmount(long add) {
        amount += add;
        System.out.println(String.format("Amount of %s = %d", id, amount));
    }
}
