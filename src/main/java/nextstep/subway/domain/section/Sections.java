package nextstep.subway.domain.section;

import nextstep.subway.domain.section.dto.SectionDetailResponse;
import nextstep.subway.domain.station.Station;
import nextstep.subway.handler.validator.SectionValidator;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Sections {
    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sectionList = new ArrayList<>();

    public List<Station> getAllStations() {
        List<Station> stations = new ArrayList<>();
        sectionList.stream().forEach(section -> section.push(stations));

        return stations;
    }

    public boolean hasAnyMatchedDownStation(Station upStation) {
        return sectionList.stream()
                .filter(section -> section.hasDownStation(upStation))
                .findAny()
                .isPresent();
    }

    public boolean hasStation(Station downStation) {
        return sectionList.stream()
                .filter(section -> section.hasStation(downStation))
                .findAny()
                .isPresent();
    }

    public boolean isEmpty() {
        return sectionList.isEmpty();
    }

    public List<SectionDetailResponse> getSectionDetailResponseList() {
        return sectionList.stream()
                .map(SectionDetailResponse::of)
                .sorted((o1, o2) -> (int) (o1.getId() - o2.getId()))
                .collect(Collectors.toList());
    }

    public Section delete(Station station) {
        Section section = sectionList.get(sectionList.size() - 1);
        SectionValidator.properDelete(section, station);
        sectionList.remove(section);

        return section;
    }
}
