package subway.line;

import subway.line.section.Section;
import subway.station.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
    @Column
    private Long distance;

    @OneToMany(mappedBy = "line")
    private List<Section> sections = new ArrayList<>();

    public Line() {

    }

    public Line(String name, String color, Station upStation, Station downStation, Long distance) {
        this(null, name, color, upStation, downStation, distance);
    }

    public Line(Long id, String name, String color, Station upStation, Station downStation, Long distance) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
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

    public Long getDistance() {
        return distance;
    }

    public List<Section> getSections() {
        return sections;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setDownStation(Station downStation) {
        this.downStation = downStation;
    }

    public void setDistance(Long distance) {
        this.distance = distance;
    }

    public boolean isCurrentDownStationId(Long upStationId) {
        return downStation.getId().equals(upStationId);
    }

    public Long changeDistance(Long distance) throws Exception {
        long increasedDistance = distance - this.distance;
        if (increasedDistance < 0) {
            throw new Exception("총 구간 길이가 이전의 길이보다 작습니다.");
        }
        this.distance = distance;
        return increasedDistance;
    }
}
