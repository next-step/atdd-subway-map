package subway.line.repository.entity;

import lombok.NoArgsConstructor;
import subway.line.business.model.Line;
import subway.station.repository.entity.StationEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "line")
@NoArgsConstructor
public class LineEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 10, nullable = false)
    private String name;
    @Column(length = 20, nullable = false)
    private String color;
    @ManyToOne
    private StationEntity upStation;
    @ManyToOne
    private StationEntity downStation;
    @Column(nullable = false)
    private Integer distance;

    public LineEntity(Line line) {
        this.id = line.getId();
        this.name = line.getName();
        this.color = line.getColor();
        this.distance = line.getDistance();
    }

    public Line toLine() {
        return Line.builder()
                .id(id)
                .name(name)
                .color(color)
                .upStation(upStation.toStation())
                .downStation(downStation.toStation())
                .distance(distance)
                .build();
    }

    public LineEntity modify(String name, String color) {
        this.name = name;
        this.color = color;

        return this;
    }

    public void setStations(StationEntity upStation, StationEntity downStation) {
        this.upStation = upStation;
        this.downStation = downStation;
    }

}
