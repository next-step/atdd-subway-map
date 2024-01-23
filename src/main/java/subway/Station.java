package subway;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class Station {

    @SequenceGenerator(
        name = "station_seq_id",
        sequenceName = "station_seq_id",
        initialValue = 1,
        allocationSize = 1)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "station_seq_id")
    private Long id;
    @Column(nullable = true)
    private Long lineId;
    @Column(length = 20, nullable = false)
    private String name;


    public Station() {
    }

    public Station(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Station(Long id, String name, Long lineId) {
        this.id = id;
        this.name = name;
        this.lineId = lineId;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
