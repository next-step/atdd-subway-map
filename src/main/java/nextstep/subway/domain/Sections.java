package nextstep.subway.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author a1101466 on 2022/07/13
 * @project subway
 * @description
 */
@Embeddable
public class Sections {

    private final static int LIMIT_SECTIONS_SIZE = 1;

    @OneToMany(mappedBy = "line", orphanRemoval = true, cascade = CascadeType.ALL)
    private final List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public Sections(Line line, int distance, Station upStation, Station downStation) {
        Section section = new Section(distance, upStation, downStation);
        section.setLine(line);
        sections.add(section);
    }

    public List<Station> getStation() {
        return sections.stream()
                .map(Section::getStations)
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());
    }
}