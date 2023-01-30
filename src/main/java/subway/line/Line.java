package subway.line;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.station.Station;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "color")
    private String color;

    @ManyToOne(fetch = FetchType.LAZY)
    private Station upStation;

    @ManyToOne(fetch = FetchType.LAZY)
    private Station downStation;

    private int distance;

    public static Line of(LineRequest dto, Station downStation, Station upStation) {
        return Line.builder()
            .name(dto.getName())
            .color(dto.getColor())
            .downStation(downStation)
            .upStation(upStation)
            .distance(dto.getDistance())
            .build();
    }

    public void modify(LineRequest request, Station downStation, Station upStation) {
        this.name = request.getName();
        this.distance = request.getDistance();
        this.color = request.getColor();
        this.downStation = downStation;
        this.upStation = upStation;
    }
}
