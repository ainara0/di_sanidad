package main.connection;


import jakarta.persistence.*;

@MappedSuperclass
public class Person {
    @Id
    @Column(name = "dni")
    private String dni;

    @Column(name = "name")
    private String name;

    @Column(name = "surname1")
    private String surname1;

    @Column(name = "surname2")
    private String surname2;

    @Column(name = "telephone")
    private String telephone;

    @Column(name = "email")
    private String email;

    public Person() {
    }

    public String getDni() {
        return dni;
    }

    public String getName() {
        return name;
    }

    public String getSurname1() {
        return surname1;
    }

    public String getSurname2() {
        return surname2;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return name + " " + surname1 + (surname2 == null ? "" : " " + surname2);
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname1(String surname1) {
        this.surname1 = surname1;
    }

    public void setSurname2(String surname2) {
        this.surname2 = surname2;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

