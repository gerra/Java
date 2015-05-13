package ru.ifmo.ctddev.berezhko.rmi;

import ru.ifmo.ctddev.berezhko.rmi.ifaces.PersonAC;

import java.io.Serializable;

/**
 * Created by root on 06.05.15.
 */
public class LocalPerson extends PersonAC implements Serializable {
    public LocalPerson(String firstName, String lastName, String passportId) {
        super(firstName, lastName, passportId);
    }
}
