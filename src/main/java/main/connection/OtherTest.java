package main.connection;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "other_test")
public class OtherTest extends Test {
    @Column(name = "notes")
    private String notes;

    @Column(name = "type")
    private String type;

    @Column(name = "results")
    private String results;

    public OtherTest() {
    }

    @Override
    public String getNotes() {
        return notes;
    }

    public String getType() {
        return type;
    }

    public String getResults() {
        return results;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setResults(String results) {
        this.results = results;
    }
}
