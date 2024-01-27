package subway;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class SubwayLine {
    @Id
    @GeneratedValue
    private Long id;

    @Column(length=20, nullable = false)
    private String name;

    public SubwayLine() {
    }

    public SubwayLine(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
