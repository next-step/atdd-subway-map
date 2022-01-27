package nextstep.subway.domain;

import nextstep.subway.applicaion.dto.SectionDetailResponse;

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
}
