package main.connection;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "antibody_test")
public class AntibodyTest extends Test{
    @Column(name = "anti_ssa")
    private Integer antiSsa;

    @Column(name = "anti_ttg")
    private Integer antiTtg;

    @Column(name = "anti_ccp")
    private Integer antiCcp;

    @Column(name = "anti_srp")
    private Integer antiSrp;

    public AntibodyTest() {
    }

    public Integer getAntiSsa() {
        return antiSsa;
    }

    public Integer getAntiTtg() {
        return antiTtg;
    }

    public Integer getAntiCcp() {
        return antiCcp;
    }

    public Integer getAntiSrp() {
        return antiSrp;
    }

    public void setAntiSsa(Integer antiSsa) {
        this.antiSsa = antiSsa;
    }

    public void setAntiTtg(Integer antiTtg) {
        this.antiTtg = antiTtg;
    }

    public void setAntiCcp(Integer antiCcp) {
        this.antiCcp = antiCcp;
    }

    public void setAntiSrp(Integer antiSrp) {
        this.antiSrp = antiSrp;
    }
}
