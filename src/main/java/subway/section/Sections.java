package subway.section;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import subway.station.Station;

@Embeddable
public class Sections {
    @OneToMany
    @OrderBy("id ASC")
    private List<Section> sections;

    public Sections(Section section) {
        sections = List.of(section);
    }

    public Sections() {
        sections = new ArrayList<Section>();
    }

    public List<Station> toStationList() {
        Set<Station> result = new HashSet<>();
        for (Section section : sections) {
            result.addAll(Set.copyOf(section.toStationList()));
        }
        return result.stream().toList();
    }

    public boolean checkDuplicateStation(Station stationToCheck) {
        return sections.stream().anyMatch(section -> section.contains(stationToCheck));
    }

    public void addSection(Section section) {
        validationCheck(section);
        sections.add(section);
    }

    private Section getLastSection() {
        return sections.get(size() - 1);
    }

    public int size() {
        return sections.size();
    }

    public Section deleteSection(Station toRemoveStation) {
        Section lastSection = getLastSection();
        validateRemovableSection(lastSection, toRemoveStation);
        sections.remove(lastSection);
        return lastSection;
    }

    private void validateRemovableSection(Section lastSection, Station toRemoveStation) {
        boolean isSingleSection = sections.size() <= 1;
        boolean isDifferentStation = !lastSection.getDownStation().equals(toRemoveStation);
        if (isSingleSection || isDifferentStation) {
            throw new IllegalStateException("구간을 제거할 수 없습니다.");
        }
    }

    private void validationCheck(Section section) {
        checkConnectivity(section.getUpStation(), getLastSection().getDownStation());
        checkIfThereAreNoDuplicateStation(section.getDownStation());
    }

    private void checkConnectivity(Station toAddSectionUpStation, Station lineDownStation) {
        if (!toAddSectionUpStation.equals(lineDownStation)) {
            throw new IllegalStateException("새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 합니다.");
        }
    }

    private void checkIfThereAreNoDuplicateStation(Station downStation) {
        if (checkDuplicateStation(downStation)) {
            throw new IllegalStateException("이미 해당 노선에 등록되어있는 역은 새로운 구간의 하행역이 될 수 없습니다.");
        }
    }
}
