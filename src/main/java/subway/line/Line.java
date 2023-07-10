package subway.line;

import lombok.*;
import subway.station.Station;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String color;
    @OneToOne
    @JoinColumn(name = "up_station_id")
    private Station upStation;
    @OneToOne
    @JoinColumn(name = "down_station_id")
    private Station downStation;

    @Builder
    Line(String name, String color, Station upStation, Station downStation) {
        this.name = name;
        this.color = color;
        this.upStation = upStation;
        this.downStation = downStation;
    }

    void changeName(String name) {
        this.name = name;
    }

    void changeColor(String color) {
        this.color = color;
    }
}
