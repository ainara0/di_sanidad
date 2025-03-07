package main.connection;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "blood_count_test")
public class BloodCountTest extends Test{
    @Column(name = "red_blood_cell")
    private Integer redBloodCell;

    @Column(name = "hemoglobin")
    private Integer hemoglobin;

    @Column(name = "hematocrit")
    private Integer hematocrit;

    @Column(name = "platet")
    private Integer platet;

    public BloodCountTest() {
    }

    public Integer getRedBloodCell() {
        return redBloodCell;
    }

    public Integer getHemoglobin() {
        return hemoglobin;
    }

    public Integer getHematocrit() {
        return hematocrit;
    }

    public Integer getPlatet() {
        return platet;
    }

    public void setRedBloodCell(Integer redBloodCell) {
        this.redBloodCell = redBloodCell;
    }

    public void setHemoglobin(Integer hemoglobin) {
        this.hemoglobin = hemoglobin;
    }

    public void setHematocrit(Integer hematocrit) {
        this.hematocrit = hematocrit;
    }

    public void setPlatet(Integer platet) {
        this.platet = platet;
    }
}
