package nextstep.subway.domain;

import org.aspectj.weaver.ast.Test;

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

    public void addSection(Line line, Section section) {
        addSectionValidation(section);
        sections.add(section);
        section.setLine(line);
    }

    public void addSectionValidation(Section section){
        Boolean checkDownStation =
                getStation().stream()
                        .anyMatch(
                                currentStation -> currentStation.getId().equals(section.getDownStation().getId())
                        );
        if(checkDownStation){
            throw new IllegalArgumentException("새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.");
        }
    }

    Boolean checkDuplicateStation(Section section){
        return getStation().contains(section.downStation);
    }

}