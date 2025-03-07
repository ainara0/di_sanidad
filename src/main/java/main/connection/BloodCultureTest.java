package main.connection;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "blood_culture_test")
public class BloodCultureTest extends Test{
    @Column(name = "salmonella")
    private Integer salmonella;

    @Column(name = "acinetobacter")
    private Integer acinetobacter;

    @Column(name = "escherichiaColi")
    private Integer escherichiaColi;

    @Column(name = "citrobacter")
    private Integer citrobacter;

    public BloodCultureTest() {
    }

    public Integer getSalmonella() {
        return salmonella;
    }

    public Integer getAcinetobacter() {
        return acinetobacter;
    }

    public Integer getEscherichiaColi() {
        return escherichiaColi;
    }

    public Integer getCitrobacter() {
        return citrobacter;
    }

    public void setSalmonella(Integer salmonella) {
        this.salmonella = salmonella;
    }

    public void setAcinetobacter(Integer acinetobacter) {
        this.acinetobacter = acinetobacter;
    }

    public void setEscherichiaColi(Integer escherichiaColi) {
        this.escherichiaColi = escherichiaColi;
    }

    public void setCitrobacter(Integer citrobacter) {
        this.citrobacter = citrobacter;
    }
}
