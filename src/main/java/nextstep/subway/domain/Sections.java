package nextstep.subway.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    public int getLastIndex() {
        return list.size() - 1;
    }

    public Section getLastSection() {
        return list.get(getLastIndex());
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

    private boolean existsStation(Long downStationId) {
        return list.stream().anyMatch(s -> s.existsStation(downStationId));
    }

    private boolean isDownTerminus(Long upStationId) {
        if (list.isEmpty()) {
            return false;
        }
        return getLastDownTerminusId().equals(upStationId);
    }

    private Long getLastDownTerminusId() {
        return list.get(getLastIndex()).getDownStationId();
    }

}
