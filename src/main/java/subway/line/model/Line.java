package subway.line.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.line.dto.LineCreateRequest;
import subway.station.model.Station;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String color;

    @OneToOne
    @JoinColumn(name = "UP_STATION_ID")
    private Station upStation;

    @OneToOne
    @JoinColumn(name = "DOWN_STATION_ID")
    private Station downStation;

    @Column(nullable = false)
    private Long distance;

    @Builder.Default
    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL)
    private List<LineStation> lineStations = new ArrayList<>();

    public static Line from(LineCreateRequest request, Station upStation, Station downStation) {
        return Line.builder()
                .name(request.getName())
                .color(request.getColor())
                .upStation(upStation)
                .downStation(downStation)
                .distance(request.getDistance())
                .build();
    }

    public void addLineStation(LineStation lineStation) {
        lineStations.add(lineStation);
        lineStation.setLine(this);
    }

    public void updateLine(String name, String color) {
        this.name = name;
        this.color = color;
    }
}
