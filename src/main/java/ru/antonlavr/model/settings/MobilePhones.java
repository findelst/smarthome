package ru.antonlavr.model.settings;

import javax.persistence.*;

@Entity
@Table(name = "settings_mobile_phones")
public class MobilePhones {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long id;
    @Column(name = "number")
    private String number;
    @Column(name = "password")
    private String password;

    public MobilePhones() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
