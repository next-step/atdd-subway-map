package subway.line.repository.entity;

import lombok.NoArgsConstructor;
import subway.station.repository.entity.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Embeddable
@NoArgsConstructor
public class Sections {

    @OneToMany(cascade = CascadeType.ALL)
    private List<Section> sections = new ArrayList<>();

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public void addSection(Section section) {
        this.sections.add(section);
    }

    public Station getFirstStation() {
        return sections.get(0).getUpStation();
    }

    public Long getFirstStationId() {
        return sections.get(0).getUpStationId();
    }

    public Station getLastStation() {
        int lastIdx = sections.size() - 1;
        return sections.get(lastIdx).getDownStation();
    }

    public Long getLastStationId() {
        int lastIdx = sections.size() - 1;
        return sections.get(lastIdx).getDownStationId();
    }

//    public int getDistance() {
//        sections.stream().map(this::getDistance)
//    }

    public Set<Long> getAllStationIds() {
        Set<Long> allStationIds = new HashSet<>();
        for (Section sec : sections) {
            allStationIds.add(sec.getUpStationId());
            allStationIds.add(sec.getDownStationId());
        }

        return allStationIds;
    }

    public List<Station> getAllStations() {
        Set<Station> allStations = new HashSet<>();
        for (Section sec : sections) {
            allStations.add(sec.getUpStation());
            allStations.add(sec.getDownStation());
        }

        return allStations.stream().collect(Collectors.toList());
    }


}
