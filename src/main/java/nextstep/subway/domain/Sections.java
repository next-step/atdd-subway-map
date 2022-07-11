package nextstep.subway.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
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

    public List<Station> getStation() {
        return sections.stream()
                .map(Section::getStations)
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public void addSection(Line line, Section section) {
        if (sections.contains(section)) {
            throw new IllegalArgumentException("이미 등록 된 구간입니다");
        }
        sections.add(section);
        section.setLine(line);
    }
}
