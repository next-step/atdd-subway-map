package nextstep.subway.domain;

import lombok.Getter;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Getter
@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line",orphanRemoval = true, cascade = CascadeType.ALL)
    private final List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public Sections(Line line, Station upStation, Station downStation, Integer distance) {
        Section section = new Section(line, upStation, downStation, distance);
        sections.add(section);
    }

    public void addSection(Section section) {
        validContainAlreadyReqDownStation(section.getDownStation());
        sections.add(section);
    }

    public List<Station> getStations() {
        return sections.stream()
                       .map(Section::getRelatedStations)
                       .flatMap(Collection::stream).distinct()
                       .collect(Collectors.toList());
    }

    public Station getLastStation() {
        if (sections.isEmpty())
            throw new ArrayIndexOutOfBoundsException("저장된 section 정보가 없습니다.");

        return sections.get(sections.size() - 1).getDownStation();
    }

    private void validContainAlreadyReqDownStation(Station downStation) {
        boolean isAlreadyRegisterStation = getStations().stream()
                                                        .anyMatch(station -> station.getName().equals(downStation.getName()));

        if (isAlreadyRegisterStation)
            throw new IllegalArgumentException("새로운 구간의 하행역은 해당 노선에 이미 등록되어있습니다.");

   }
}
