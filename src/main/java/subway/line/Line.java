package subway.line;

import org.hibernate.annotations.Cascade;
import subway.station.Station;

import javax.persistence.*;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, nullable = false)
    private String name;
    @Column(nullable = false)
    private String color;
    @ManyToOne
    @JoinColumn(name = "upStationId")
    private Station upStation;
    @ManyToOne
    @JoinColumn(name = "downStationId")
    private Station downStation;

    public Line() {

    }

    public Line(String name, String color, Station upStation, Station downStation) {
        this(null, name, color, upStation, downStation);
    }

    public Line(Long id, String name, String color, Station upStation, Station downStation) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.upStation = upStation;
        this.downStation = downStation;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }
}
