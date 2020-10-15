package com.smuzdev.lab_06_2;

public class Person {
    public String name, surname, phone, birthDate;

    public Person (String name, String surname, String phone, String birthDate) {
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.birthDate = birthDate;
    }

    public Person () {

    }

    @Override
    public String toString () {
        return "Person{" +
                "name: " + name + '\'' +
                ", surname: " + surname + '\'' +
                ", phone: " + phone + '\'' +
                ", birthDate: " + birthDate + '\'' +
                "}";
    }
}
