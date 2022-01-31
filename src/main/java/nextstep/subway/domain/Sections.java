package nextstep.subway.domain;

import nextstep.subway.exception.BadRequestException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    void addSection(Section section, Line line) {
        sections.add(section);
        section.setLine(line);
    }

    void deleteSection(Long stationId) {
        validateDelete(stationId);

        Section lastSection = sections.get(sections.size() - 1);
        sections.remove(lastSection);
    }

    private void validateDelete(Long stationId) {
        Station downEndStation = sections.get(sections.size() - 1).getDownStation();
        if (!downEndStation.getId().equals(stationId)) {
            throw new BadRequestException("구간 삭제는 하행 종점역만 삭제할 수 있습니다.");
        }

        if (sections.size() == 1) {
            throw new BadRequestException("구간 삭제는 구간이 2개 이상이어야 합니다.");
        }
    }

    public List<Section> getSections() {
        return Collections.unmodifiableList(sections);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Sections sections1 = (Sections) o;
        return Objects.equals(getSections(), sections1.getSections());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSections());
    }
}
