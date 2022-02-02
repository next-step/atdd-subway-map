package nextstep.subway.domain;

import nextstep.subway.exception.ContainStationException;
import nextstep.subway.exception.IllegalDeleteSectionException;
import nextstep.subway.exception.NotEqualDownStationException;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Sections {
    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Section> sections = new ArrayList<>();

    public void init(Section section) {
        sections.add(section);
    }

    public void addSection(Section section) {
        checkContainSection(section);
        checkNotEqualDownStation(section);
        sections.add(section);
    }

    public List<Section> getSections() {
        return sections;
    }

    private Section lastSection() {
        if (sections.size() - 1 < 0) {
            throw new IllegalArgumentException("sections 의 size 가 0 이하 입니다.");
        }
        return sections.get(sections.size() - 1);
    }

    private void checkNotEqualDownStation(Section section) {
        Section lastSection = lastSection();
        if (!lastSection.getDownStation().equals(section.getUpStation())) {
            throw new NotEqualDownStationException();
        }
    }

    private void checkContainSection(Section section) {
        if (getStations().contains(section.getDownStation())) {
            throw new ContainStationException();
        }
    }

    public List<Station> getStations() {
        List<Station> stations = new ArrayList<>();
        sections.stream()
                .forEach(section -> {
                    stations.add(section.getDownStation());
                    stations.add(section.getUpStation());
                });
        return stations.stream().distinct().collect(Collectors.toList());
    }

    public void delete(Section section) {
        if (sections.size() <= 1) {
            throw new IllegalDeleteSectionException();
        }
        sections.remove(section);
    }
}
