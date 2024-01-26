package subway.station;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Long lineId;
    @Column(length = 20, nullable = false)
    private String name;


    public Station() {
    }

    public Station(String name) {
        this.name = name;
    }

    public Station(String name, Long lineId) {
        this.name = name;
        this.lineId = lineId;
    }

    public void updateLineId(Long lineId) {
        this.lineId = lineId;
    }
}
