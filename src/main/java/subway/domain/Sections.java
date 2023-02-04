package subway.domain;

import subway.domain.exceptions.CanNotAddSectionException;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Sections {

    @ElementCollection
    @CollectionTable(name = "section", joinColumns = @JoinColumn(name = "line_id"))
    private List<Section> sections = new ArrayList<>();

    public static Sections create(Section section) {
        return new Sections(List.of(section));
    }

    public Sections() {

    }

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public void addSection(Section newSection) {
        if (!findLastSection().isDownStation(newSection.getUpStation())) {
            throw new CanNotAddSectionException("새로운 구간의 상행역은 해당 노선에 등록되어 있는 하행 종점역이어야 한다.");
        }

        if (isAlreadyExistsStation(newSection.getDownStation())) {
            throw new CanNotAddSectionException("새로운 구간의 하행역은 해당 노선에 등록되어 있는 역일 수 없다.");
        }

        sections.add(newSection);
    }

    public void delete(Station station) {
        if (sections.size() == 1) {
            throw new CanNotAddSectionException("지하철 노선에 구간이 1개인 경우 역을 삭제할 수 없다.");
        }

        if (!findLastSection().isDownStation(station)) {
            throw new CanNotAddSectionException("지하철 노선에 등록된 역(하행 종점역)만 제거할 수 있다.");
        }

        sections.remove(findLastSection());
    }

    private boolean isAlreadyExistsStation(Station station) {
        return sections.stream()
                .filter(section -> section.hasStation(station))
                .findFirst()
                .isPresent();
    }

    private Section findLastSection() {
        return sections.get(sections.size() - 1);
    }

    public List<Section> getSections() {
        return sections;
    }
}
