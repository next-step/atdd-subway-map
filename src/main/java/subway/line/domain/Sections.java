package subway.line.domain;

import subway.line.exception.SectionBadRequestException;
import subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public void add(Section section) {
        addSectionValidationCheck(section);
        sections.add(section);
    }

    public List<Station> getSections() {
        return sections.stream()
            .map(Section::getStations)
            .flatMap(Collection::stream)
            .distinct()
            .collect(Collectors.toList());
    }

    private void addSectionValidationCheck(Section section) {
        if (sections.isEmpty()) {
            return ;
        }
        if (!validateUpStation(section.getUpStation())) {
            throw new SectionBadRequestException("새로운 구간의 상행역은 노선의 하행 종점이어야 합니다.");
        }
        if (validateDownStation(section.getDownStation())) {
            throw new SectionBadRequestException("새로운 구간의 하행역은 노선의 등록되어 있는 역일 수 없습니다.");
        }
    }

    private boolean validateUpStation(Station upStaion) {
        int end = sections.size() - 1;
        Station endStation = sections.get(end).getDownStation();
        return upStaion.equals(endStation);
    }

    private boolean validateDownStation(Station downStation) {
        return getSections().stream().anyMatch(station -> station.equals(downStation));
    }
}
