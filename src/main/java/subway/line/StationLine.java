package subway.line;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import subway.section.StationSection;
import subway.station.Station;

@Entity
public class StationLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    @OneToMany(
            mappedBy = "stationLine",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            orphanRemoval = true
    )
    private List<StationSection> stationSections = new ArrayList<>();

    @Column
    private int distance;

    public StationLine() {}

    public StationLine(String name, String color, int distance) {
        this.name = name;
        this.color = color;
        this.distance = distance;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public List<StationSection> getStationSections() {
        return stationSections;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void updateStationLine(StationLineRequest stationLineRequest) {
        this.name = stationLineRequest.getName();
        this.color = stationLineRequest.getColor();
    }

    public void addStationSection(StationSection stationSection) {
        stationSections.add(stationSection);
    }

    public void deleteStationSection(Station station) {
        StationSection lastStationSection = getLastStationSection();
        if (lastStationSection.getDownStation() != station) {
            throw new IllegalArgumentException("삭제할 구간이 올바르지 않습니다.");
        }

        lastStationSection.remove();
        stationSections.remove(lastStationSection);
    }

    private StationSection getLastStationSection() {
        return stationSections.get(stationSections.size() - 1);
    }

    public boolean isSingleSection() {
        return stationSections.size() == 1;
    }

    public boolean isExistSection(StationSection stationSection) {
        return stationSections.stream()
                .anyMatch(section -> section.isExistSection(stationSection));
    }

    public boolean isConnectedSection(StationSection stationSection) {
        return stationSections.stream()
                .anyMatch(section -> section.isConnectedSection(stationSection));
    }

    public boolean isRemoveFinalSection(Station station) {
        return getLastStationSection().isMatchDownStation(station);
    }
}
