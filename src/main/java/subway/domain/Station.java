package subway.domain;

import java.util.Objects;
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

    public boolean isStationInLine(Long lineId) {
        return this.lineId.equals(lineId);
    }

    public boolean equalsId(Long id) {
        return this.id.equals(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Station station = (Station) o;
        return Objects.equals(id, station.id) && Objects.equals(lineId,
            station.lineId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lineId);
    }

    public boolean isStationRegisteredToLine() {
        return lineId != null;
    }
}
