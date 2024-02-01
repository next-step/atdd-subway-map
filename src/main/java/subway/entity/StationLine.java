package subway.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class StationLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String color;

    private Long upStationId;

    private Long downStationId;

    private int distance;

    @JsonIgnore
    @OneToMany(mappedBy = "stationLine")
    private List<StationSection> sections = new ArrayList<>();

    protected StationLine() {
    }

    public StationLine(String name, String color, Long upStationId, Long downStationId, int distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public StationLine update(String name, String color) {
        this.name = name;
        this.color = color;
        return this;
    }

    public boolean canSave(StationSection toSaveSection) {
        if(toSaveSection.areStationsSame()) {
            return false;
        }
        if(!toSaveSection.connectToLastUpStation(downStationId)) {
            return false;
        }
        return hasNoConnectingDownStation(toSaveSection);
    }

    private boolean hasNoConnectingDownStation(StationSection toSaveSection) {
        return sections.stream()
                .noneMatch(existSection -> existSection.isUpStationSameAsDownStation(toSaveSection));
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

    public List<StationSection> getSections() {
        return sections;
    }

    public void updateDownStation(Long downStationId) {
        this.downStationId = downStationId;
    }
}
