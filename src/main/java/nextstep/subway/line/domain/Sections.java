package nextstep.subway.line.domain;

import com.google.common.collect.Iterables;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.line.domain.exception.CannotAppendSectionException;
import nextstep.subway.line.domain.exception.CannotDeleteSectionException;
import nextstep.subway.station.domain.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Embeddable
public class Sections {

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "line_section", joinColumns = @JoinColumn(name = "line_id"))
    @OrderColumn(name = "section_idx")
    private List<Section> sections = new ArrayList<>();

    public Sections(Section firstSection) {
        this.sections.add(firstSection);
    }

    public void appendSection(Section section) {
        validateSection(section);
        sections.add(section);
    }

    private void validateSection(Section section) {
        validateUpStation(section.getUpStation());
        validateDownStation(section.getDownStation());
    }

    private void validateUpStation(Station upStation) {
        if (upStation != lastStation()) {
            throw new CannotAppendSectionException("새로운 구간의 상행역은 현재 노선의 종점이어야합니다.");
        }
    }

    private void validateDownStation(Station downStation) {
        sections.stream()
                .filter(sec -> sec.contains(downStation))
                .findAny()
                .ifPresent(sec -> {
                    throw new CannotAppendSectionException("새로운 구간의 하행역이 이미 노선에 등록되어있습니다.");
                });
    }

    public void removeStation(Station station) {
        if (station != lastStation()) {
            throw new CannotDeleteSectionException("구간의 종점만 삭제할 수 있습니다.");
        }
        if (sections.size() <= 1) {
            throw new CannotDeleteSectionException("구간이 1개인 경우 구간을 삭제할 수 없습니다.");
        }
        sections.remove(sections.size() - 1);
    }

    private Station lastStation() {
        return Iterables.getLast(sections).getDownStation();
    }
}
