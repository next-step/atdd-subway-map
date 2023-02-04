package subway.domain;

import subway.exception.SubwayException;
import subway.exception.statusmessage.SubwayExceptionStatus;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();


    public void add(Section addSection) {

        if (!sections.isEmpty() && !canAddAfterLastSection(addSection)) {
            throw new SubwayException(
                    SubwayExceptionStatus.SECTION_NOT_ADD
                    , "마지막 구간의 하행선과 추가하려는 구간의 상행선이 같아야 합니다.");
        }
        if (!sections.isEmpty() && containDownStationOfSection(addSection)) {
            throw new SubwayException(
                    SubwayExceptionStatus.SECTION_NOT_ADD,
                    "추가하려는 구간의 하행선이 기존 노선에 이미 있습니다.");
        }
        this.sections.add(addSection);
    }

    private boolean containDownStationOfSection(Section addSection) {
        return sections.stream().anyMatch(section -> section.hasStation(addSection.getDownStation()));
    }

    private boolean canAddAfterLastSection(Section addSection) {
        return this.getLast().equalsDownStation(addSection);
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public Section getLast() {
        return sections.get(sections.size() - 1);
    }

    public boolean lessThanTwo() {
        return sections.size() < 2;
    }

    public void remove(Section section) {
        sections.remove(section);
    }

    public List<Section> getSections() {
        return Collections.unmodifiableList(sections);
    }

}
