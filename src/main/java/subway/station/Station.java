package subway.station;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "STATION")
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "STATION_ID")
    private Long id;

    @Column(name = "LINE_ID")
    private Long lineId;

    @Column(name = "STATION_NAME", length = 20, nullable = false)
    private String name;

    public Station(Long lineId, String name) {
        this.lineId = lineId;
        this.name = name;
    }

    public void changeLine(Long lineId) {
        this.lineId = lineId;
    }
}
