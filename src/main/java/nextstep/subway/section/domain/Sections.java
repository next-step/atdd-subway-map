package nextstep.subway.section.domain;


import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class Sections {
    private static final int MIN_SECTION_SIZE = 1;
    private List<Section> sections;

    public Sections(List<Section> sections) {
        this.sections = sections;
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public Set<Long> getSectionIds() {
        return sections.stream().flatMap(section -> section.getStationIds().stream()).collect(Collectors.toSet());
    }

    public void checkAddValidation(Long upStationId, Long downStationId) {
        if (!isEmpty()) {
            checkUpStationNone(upStationId);
            checkDownStationNone(downStationId);
            checkUpDownStation(upStationId);
        }
    }

    public void checkRemoveValidation(Long downStationId) {
        if (!isEmpty()) {
            checkRemoveOnlyStation();
            checkRemoveDownStation(downStationId);
        }
    }

    private void checkUpStationNone(Long upStationId) {
        Assert.isTrue(sections.stream().noneMatch(section ->
                Objects.equals(section.getUpStationId(), upStationId)

        ), "새로운 구간의 상행역은 현재 등록되어있는 상행역일 수 없다.");
    }

    private void checkDownStationNone(Long downStationId) {
        Assert.isTrue(sections.stream().noneMatch(section ->
                Objects.equals(section.getUpStationId(), downStationId) || Objects.equals(section.getDownStationId(), downStationId)
        ), "새로운 구간의 하행역은 현재 등록되어있는 역일 수 없다.");
    }

    private void checkUpDownStation(Long upStationId) {
        Assert.isTrue(sections.stream().anyMatch(section ->
                Objects.equals(section.getDownStationId(), upStationId)
        ), "구간의 상행역은 현재 등록되어있는 하행 종점역이어야 합니다.");
    }

    private void checkRemoveOnlyStation() {
        Assert.isTrue(sections.size() != MIN_SECTION_SIZE, "지하철 노선에 상행 종점역과 하행 종점역만 있는 경우(구간이 " + MIN_SECTION_SIZE + "개인 경우) 역을 삭제할 수 없다.");
    }

    private void checkRemoveDownStation(Long downStationId) {
        Assert.isTrue(Objects.equals(sections.get(sections.size() - 1).getDownStationId(), downStationId)
                , "지하철 노선에 등록된 마지막 역(하행 종점역)만 제거할 수 있다.");
    }
}
