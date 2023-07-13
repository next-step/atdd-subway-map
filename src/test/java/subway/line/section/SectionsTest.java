package subway.line.section;

import static common.Constants.새로운지하철역;
import static common.Constants.신논현역;
import static common.Constants.지하철역;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.line.section.SectionBuilder.aSection;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.common.exception.BusinessException;
import subway.station.Station;

@DisplayName("구간 관련")
public class SectionsTest {

    @DisplayName("마지막 순서의 구간을 만든다")
    @Test
    void createLastSection() {
        Section section = aSection().build();
        Sections sections = new Sections(new ArrayList<>(List.of(section)));
        sections.add(section.getLine(), new Station(2L, 신논현역), new Station(3L, 지하철역));
        Section newSection = sections.getLastSection();

        assertAll(
            () -> assertThat(newSection.getUpStation().getId()).isEqualTo(2L),
            () -> assertThat(newSection.getDownStation().getId()).isEqualTo(3L),
            () -> assertThat(newSection.getSequence()).isEqualTo(2)
        );
    }

    @DisplayName("구간 추가시 비즈니스 검증에 문제가 없다면 아무 일도 일어나지 않는다")
    @Test
    void sectionValidate_pass() {
        // given
        Sections sections = new Sections(List.of(aSection().build()));

        // when & then
        sections.validate(new Station(2L, 신논현역), new Station(3L, 지하철역));
    }

    @DisplayName("구간 추가시 이미 등록된 역을 하행선으로 가지면 예외를 발생시킨다")
    @Test
    void sectionValidate_fail_duplicatedDownStation() {
        // given
        Sections sections = new Sections(List.of(aSection().build()));

        // when & then
        assertThatThrownBy(() -> sections.validate(new Station(3L, 지하철역), new Station(2L, 신논현역)))
            .isInstanceOf(BusinessException.class);
    }

    @DisplayName("구간 추가시 하행 종점역이 아닌 역을 상행선으로 가지면 실패한다")
    @Test
    void sectionValidate_fail_upStationDoesNotMatchWithDownEndStation() {
        // given
        Section firstSection = aSection().build();
        Section midSection = aSection().withUpStation(new Station(2L, 신논현역)).withDownStation(new Station(3L, 지하철역)).withSequence(2).build();
        Sections sections = new Sections(List.of(firstSection, midSection));

        // when & then
        assertThatThrownBy(() -> sections.validate(new Station(2L, 신논현역), new Station(4L, 새로운지하철역)))
            .isInstanceOf(BusinessException.class);
    }
}
