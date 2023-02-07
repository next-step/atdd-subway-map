package subway.station.domain.station;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "STATION_ID")
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    public Station() {
    }

    @Builder
    public Station(String name) {
        this.name = name;
    }
}
