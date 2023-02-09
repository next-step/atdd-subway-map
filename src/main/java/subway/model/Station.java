package subway.model;

import javax.persistence.*;

@Entity
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, nullable = false)
    private String name;

    @Column
    private Long preStationId;

    protected Station() {
    }

    public Station(String name) {
        this.name = name;
    }

    public Station(String name, Long preStationId) {
        this.name = name;
        this.preStationId = preStationId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getPreStationId() {
        return preStationId;
    }
}
