package nextstep.subway.line.domain;

import nextstep.subway.station.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    public Sections() {
    }

    public List<Section> getSections() {
        return sections;
    }

    public List<Station> getStations() {
        return this.sections.stream()
                .flatMap(section -> section.getStations().stream())
                .distinct()
                .collect(Collectors.toList());
    }

    public Optional<Section> findSection(Station upStation, Station downStation) {
        return this.sections.stream()
                .filter(section -> section.upAndDownStationMatch(upStation, downStation))
                .findFirst();
    }

    public void addSection(Section section) {

        List<Station> stations = this.getStations();
        validate(stations, section);

        if (stations.isEmpty()) {
            this.sections.add(section);
            return;
        }

        this.sections.add(section);
    }

    private void validate(List<Station> stations, Section section) {
        if (stations.isEmpty()) {
            return;
        }

        if (!isLastStationMatch(section)) {
            throw new IllegalArgumentException("추가하는 구간의 상행역이 잘못되었습니다.");
        }

        if (isUpAndDownExists(stations, section)) {
            throw new IllegalArgumentException("상행역과 하행역이 이미 존재합니다.");
        }

        if (isDownExists(stations, section)) {
            throw new IllegalArgumentException("하행역이 이미 노선에 포함되어 있습니다.");
        }
    }

    private boolean isLastStationMatch(Section section) {
        return findLastStation().equals(section.getUpStation());
    }

    private boolean isUpAndDownExists(List<Station> stations, Section section) {
        return stations.contains(section.getUpStation()) && stations.contains(section.getDownStation());
    }

    private boolean isDownExists(List<Station> stations, Section section) {
        return stations.contains(section.getDownStation());
    }

    public void removeSection(Station station) {
        if (isSizeLessThanOne()) {
            throw new RuntimeException("해당 라인에 구간이 1개 밖에 없습니다.");
        }

        if (!findLastStation().equals(station)) {
            throw new RuntimeException("해당 구간의 마지막 역이 아닙니다.");
        }

        sections.remove(sections.size() - 1);
    }

    private boolean isSizeLessThanOne() {
        return this.sections.size() <= 1;
    }

    private Station findLastStation() {
        Section lastSection = this.getSections().get(sections.size() - 1);
        return lastSection.getDownStation();
    }
}