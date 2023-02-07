package subway.station.domain.line;

import lombok.Builder;
import lombok.Getter;
import subway.station.domain.station.Station;

import javax.persistence.*;

@Entity
@Getter
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LINE_ID")
    private Long id;

    private String name;
    private String color;
    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_up_station"))
    private Station upStation;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_down_station"))
    private Station downStation;
    private Long distance;

    public Line() {
    }

    @Builder
    public Line(String name, String color, Station upStation, Station downStation, Long distance) {
        this.name = name;
        this.color = color;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changeColor(String color) {
        this.color = color;
    }
}
