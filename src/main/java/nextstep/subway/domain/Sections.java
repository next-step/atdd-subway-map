package nextstep.subway.domain;

import lombok.*;
import nextstep.subway.exception.SectionException;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable @Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Sections {
    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> list = new ArrayList<>();

    public void add(Section section) {
        validate(section);
        list.add(section);
    }

    public Section get(int index) {
        return list.get(index);
    }

    public int lastIndex() {
        return list.size() - 1;
    }

    public Section getLastSection() {
        return list.get(lastIndex());
    }

    public void deleteSection(Long stationId) {
        if (!existsMoreThanTwoSection()) {
            throw new SectionException("2개 이상의 구간이 등록되어야 구간을 제거할 수 있습니다.");
        }
        if (!isDownTerminus(stationId)) {
            throw new IllegalArgumentException("하행 종점역만 제거할 수 있습니다.");
        }
        getLastSection().deleteDownStation();
    }

    private void validate(Section section) {
        if (list.isEmpty()) {
            return;
        }
        if (!isDownTerminus(section.getUpStationId())) {
            throw new IllegalArgumentException("구간의 상행역이 노선의 하행 종점역이 아닙니다.");
        }
        if (existsStation(section.getDownStationId())) {
            throw new IllegalArgumentException("하행역이 이미 등록된 지하철역 입니다.");
        }
    }

    private boolean existsMoreThanTwoSection() {
        return list.size() > 1;
    }

    private boolean existsStation(Long downStationId) {
        return list.stream().anyMatch(s -> s.existsStation(downStationId));
    }

    private boolean isDownTerminus(Long stationId) {
        return getLastDownTerminusId().equals(stationId);
    }

    private Long getLastDownTerminusId() {
        return list.get(lastIndex()).getDownStationId();
    }
}
