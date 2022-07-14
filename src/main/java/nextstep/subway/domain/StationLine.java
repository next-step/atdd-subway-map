package nextstep.subway.domain;

import nextstep.subway.applicaion.dto.StationLineRequest;

import javax.persistence.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Entity
public class StationLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "stationLine", orphanRemoval = true)
    List<Section> sections = new LinkedList<>();

    public StationLine() {
    }

    public StationLine(String name, String color) {
        this.name = name;
        this.color = color;
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

    public List<Section> getSections() {
        return sections;
    }

    public void updateByStationLineRequest(StationLineRequest stationLineRequest) {
        if (stationLineRequest.getName() != null){
            this.name = stationLineRequest.getName();
        }
        if (stationLineRequest.getColor() != null) {
            this.color = stationLineRequest.getColor();
        }
    }

    public Set<Long> getStationIdsIncluded() {
        Set<Long> stations = new HashSet<>();
        List<Section> sections = this.getSections();

        for (Section section : sections){
            stations.add(section.getUpStationId());
            stations.add(section.getDownStationId());
        }

        return stations;
    }
}
