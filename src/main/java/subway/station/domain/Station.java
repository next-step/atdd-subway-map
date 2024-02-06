package subway.station.domain;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Where(clause = "deleted_at IS NULL")
@SQLDelete(sql = "UPDATE station SET deleted_at = CURRENT_TIMESTAMP where station_id = ?")
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "station_id")
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column
    private Timestamp deleted_at;

    protected Station() {
    }

    public Station(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
