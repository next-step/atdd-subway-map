package subway.line.model;

import lombok.*;
import subway.line.dto.LineCreateRequest;
import subway.station.model.Station;

import javax.persistence.*;
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

    @Setter
    @Column(nullable = false)
    private String name;

    @Setter
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

    @OneToMany(mappedBy = "id")
    private List<Station> stations;

    public static Line from(LineCreateRequest request, Station upStation, Station downStation) {
        List<Station> stations = List.of(upStation, downStation);
        return Line.builder()
                .name(request.getName())
                .color(request.getColor())
                .upStation(upStation)
                .downStation(downStation)
                .distance(10L)
                .stations(stations)
                .build();
    }
}
