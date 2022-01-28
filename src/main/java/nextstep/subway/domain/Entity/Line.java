package nextstep.subway.domain.Entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Line extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    @Column(name = "color")
    private String color;

    @Column(name = "up_station_id")
    private Long upStationId;

    @Column(name = "down_station_id")
    private Long downStationId;

    @Column(name = "distance")
    private int distance;

    public List<Section> getSections() {
        return sections;
    }

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    protected Line() {
        //
    }

    public Line(String name, String color, Long upStationId, Long downStationId, int distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public Boolean isExistSection(Station station) {
        for (Section isExistSection : sections) {
            if (isExistSection.getUpStation().equals(station)) {
                return true;
            }
            if (isExistSection.getDownStation().equals(station)) {
                return true;
            }
        }
        return false;
    }

    public void updateDownStation(Station downStation) {
        this.downStationId = downStation.getId();
    }

    public Boolean isDownStation(Long stationId) {
        return this.downStationId == stationId;
    }

    public void updateLine(final String name, final String color) {
        this.color = color;
        this.name = name;
    }

    public int sectionCount() {
        return this.sections.size();
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

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public int getDistance() {
        return distance;
    }
}
