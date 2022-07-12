package nextstep.subway.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL)
    private final List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public Sections(Line line, long distance, Station upStation, Station downStation) {
        Section section = new Section(distance, upStation, downStation);
        section.setLine(line);
        sections.add(section);
    }

    public void addSection(Line line, Section section) {
        validation(section);
        sections.add(section);
        section.setLine(line);
    }

    private void validation(Section section) {
        isContains(section);
        isUpStation(section);
        isDownStation(section);
    }

    private void isContains(Section section) {
        if (sections.contains(section)) {
            throw new IllegalArgumentException("이미 등록 된 구간입니다");
        }
    }

    private void isUpStation(Section section) {
        boolean isUpStation = sections.stream().anyMatch(currentSection ->
                currentSection.isUpStation(section)
        );
        if (!isUpStation) {
            throw new IllegalArgumentException("새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 합니다.");
        }
    }

    private void isDownStation(Section section) {
        boolean isDownStation = sections.stream().anyMatch(currentSection ->
                currentSection.isDownStation(section)
        );
        if (isDownStation) {
            throw new IllegalArgumentException("새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없습니다.");
        }
    }

    public void removeSection(Section section) {
        Section lastSection = getLastSection();
        if (!lastSection.equals(section)) {
            throw new IllegalArgumentException("현재 구간은 마지막 구간이 아닙니다.");
        }
        sections.remove(section);
    }

    private Section getLastSection() {
        return sections.get(sections.size() - 1);
    }

    public List<Station> getStation() {
        return sections.stream()
                .map(Section::getStations)
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());
    }
}
