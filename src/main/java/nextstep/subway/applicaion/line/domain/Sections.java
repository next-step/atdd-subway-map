package nextstep.subway.applicaion.line.domain;

import nextstep.subway.applicaion.common.DuplicatedDownStationException;
import nextstep.subway.applicaion.common.MinimumSectionException;
import nextstep.subway.applicaion.common.UnappropriateStationException;
import nextstep.subway.applicaion.section.domain.Section;
import nextstep.subway.applicaion.station.domain.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Embeddable
public class Sections {
    private final Integer MINIMUM_SECTION_SIZE = 1;
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    @JoinColumn(name = "line_id")
    private List<Section> sections = new ArrayList<>();

    public void add(Line line, Station upStation, Station downStation, Integer distance) {
        Section section = new Section(upStation, downStation, distance, line);
        sections.add(section);
    }

    public List<Station> stations() {
        List<Station> stations = sections.stream().map(Section::getUpStation).collect(Collectors.toList());
        stations.add(getLastStation());
        return stations;
    }

    public void removeStation(Long stationId) {
        if (sections.size() <= MINIMUM_SECTION_SIZE) {
            throw new MinimumSectionException();
        }

        if (!Objects.equals(getLastStation().getId(), stationId)) {
            throw new UnappropriateStationException();
        }
        sections.remove(getLastSection());
    }

    public void checkIsLastStation(Station station) {
        if (!Objects.equals(getLastStation().getId(), station.getId())) {
            throw new UnappropriateStationException();
        }
    }

    public void checkIsNewStation(Station station) {
        stations().forEach(it -> {
            if (Objects.equals(it.getId(), station.getId())) {
                throw new DuplicatedDownStationException();
            }
        });
    }

    private Section getLastSection() {
        return sections.get(sections.size() - 1);
    }

    private Station getLastStation() {
        return getLastSection().getDownStation();
    }
}
