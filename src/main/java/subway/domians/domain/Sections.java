package subway.domians.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Section> sections = new ArrayList<>();

    public void addSection(Section section) {
        if (invalidUpStation(section) || alreadyExistsStation(section)) {
            throw new IllegalArgumentException("invalid section");
        }
        this.sections.add(section);
    }

    public boolean invalidUpStation(Section section) {
        if (!this.sections.isEmpty()) {
            Station endStation = this.getEndStation();
            return !Objects.equals(endStation.getId(), section.getUpStation().getId());
        }
        return false;
    }

    public boolean alreadyExistsStation(Section section) {
        return this.getStations().stream()
            .anyMatch(station -> station.getId().equals(section.getDownStation().getId()));
    }

    public List<Station> getStations() {
        return this.sections.stream()
            .flatMap(section -> Stream.of(section.getUpStation(), section.getDownStation()))
            .collect(Collectors.toList());
    }


    public void removeSection(Long downStationId) {
        if(isNotLastStation(station) || getSectionSize() < 2) {
            throw new IllegalArgumentException("invalid remove section");
        }
        sections.remove(sections.size() - 1);
    }

    public Station getEndStation() {
        return sections.get(sections.size() - 1).getDownStation();
    }

    public int getSectionSize() {
        return this.sections.size();
    }
}
