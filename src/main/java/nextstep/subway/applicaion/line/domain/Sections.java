package nextstep.subway.applicaion.line.domain;

import nextstep.subway.applicaion.common.DuplicatedDownStationException;
import nextstep.subway.applicaion.common.SectionNotFoundException;
import nextstep.subway.applicaion.common.UnappropriateUpStationException;
import nextstep.subway.applicaion.section.domain.Section;
import nextstep.subway.applicaion.station.domain.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Embeddable
public class Sections {
    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST}, orphanRemoval = true)
    @JoinColumn(name = "line_id")
    private List<Section> sections = new ArrayList<>();

    public void initializeLine(Line line, Station upStation, Station downStation, Integer distance) {
        addFirstMark(line, upStation);
        addLastMark(line, downStation);
        this.add(line, upStation, downStation, distance);
    }

    public void addLast(Line line, Station upStation, Station downStation, Integer distance) {
        this.add(line, upStation, downStation, distance);
        addLastMark(line, downStation);
    }

    public void addFirst(Line line, Station upStation, Station downStation, Integer distance) {
        this.add(line, upStation, downStation, distance);
        addFirstMark(line, upStation);
    }

    public List<Station> stations() {
        return sections.stream().map(it -> it.upStation).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public void checkIsLastStation(Station upStation) {
        if (!Objects.equals(getLastStation().getId(), upStation.getId())) {
            throw new UnappropriateUpStationException();
        }
    }

    public void checkIsNewStation(Station downStation) {
        stations().forEach(it -> {
            if (Objects.equals(it.getId(), downStation.getId())) {
                throw new DuplicatedDownStationException();
            }
        });
    }

    private void addLastMark(Line line, Station downStation) {
        getLastSection().ifPresent(it -> sections.remove(it));
        Section lastSection  = new Section(downStation, null, 0, line);
        sections.add(lastSection);
    }

    private void addFirstMark(Line line, Station upStation) {
        getFirstSection().ifPresent(it -> sections.remove(it));
        Section firstSection = new Section(null, upStation, 0, line);
        sections.add(firstSection);
    }

    private Optional<Section> getLastSection() {
        return sections.stream().filter(it -> it.downStation == null).findFirst();
    }

    private Optional<Section> getFirstSection() {
        return sections.stream().filter(it -> it.upStation == null).findFirst();
    }

    private void add(Line line, Station upStation, Station downStation, Integer distance) {
        Section section = new Section(upStation, downStation, distance, line);
        sections.add(section);
    }

    private Station getLastStation() {
        return getLastSection().orElseThrow(SectionNotFoundException::new).upStation;
    }
}
