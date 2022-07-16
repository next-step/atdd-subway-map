package nextstep.subway.domain;

import java.util.LinkedList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.constant.Message;
import nextstep.subway.exception.IllegalUpdatingStateException;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Sections {

    @OneToMany(mappedBy = "stationLine",
        fetch = FetchType.LAZY,
        cascade = {CascadeType.PERSIST, CascadeType.MERGE} ,
        orphanRemoval = true)
    @OrderBy(value = "id")
    private List<Section> sections = new LinkedList<>();


    public Section add(Section section) {
        validateAddSection(section);

        sections.add(section);
        return section;
    }

    public void validateAddSection(Section newSection) {
        validateDownSection(newSection);
        validateDistinctSection(newSection);
    }

    public void validateDownSection(Section newSection) {
        Section lastSection = getLastSection();

        if(!lastSection.getDownStation().getId().equals(newSection.getUpStation().getId())) {
            throw new IllegalUpdatingStateException(Message.ONLY_ADD_DOWNSTATION.getValue());
        }
    }

    public void validateDistinctSection(Section newSection) {
        sections.stream()
            .filter(station -> station.getUpStation().equals(newSection.getDownStation()))
            .findFirst()
            .ifPresent(station -> { throw new IllegalUpdatingStateException( Message.IS_EXIST_STATION.getValue()); });
    }

    public Section remove(Long stationId) {
        validateRemove();
        return sections.remove(sections.size() - 1);
    }

    public void validateRemove() {
        if(sections.size() == 1) {
            throw new IllegalUpdatingStateException( Message.ONE_COUNT_SECTIONLIST.getValue());
        }
    }

    public Section getLastSection() {
        return sections.get(sections.size()-1);
    }

}
