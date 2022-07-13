package nextstep.subway.domain;

import org.aspectj.weaver.ast.Test;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    public Section validationAndSectionDelete(Line line, Long stationId){
        Section lastSection = sections.get(sections.size()-1);

        if(!Objects.equals(lastSection.getDownStation().getId(), stationId))
            throw new IllegalArgumentException("지하철 노선에 등록된 역(하행 종점역)만 제거할 수 있다. 즉, 마지막 구간만 제거할 수 있다.");

        if(sections.size() < 2)
            throw new IllegalArgumentException("지하철 노선에 상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우) 역을 삭제할 수 없다.");

        sections.remove(lastSection);
        return lastSection;
    }

}