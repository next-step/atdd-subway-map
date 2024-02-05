package subway.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StationSectionsTest {

    /**
     * Given 지하철 구간이 생성되고
     * When  지하철 구간 목록에 구간을 추가하면
     * Then  지하철 구간 목록에 추가된다.
     */
    @Test
    void 지하철_구간_목록_생성() {
        // given
        StationSections 구간_목록 = new StationSections();

        StationSection 신사_논현_구간 = new StationSection(1L, 2L, 3);
        StationSection 논현_신논현_구간 = new StationSection(2L, 3L, 3);
        StationSection 신논현_강남_구간 = new StationSection(3L, 4L, 3);

        // when
        구간_목록.addSection(신사_논현_구간);
        구간_목록.addSection(논현_신논현_구간);
        구간_목록.addSection(신논현_강남_구간);

        // then
        assertThat(구간_목록.getSections()).hasSize(3);
        assertThat(구간_목록.getSections()).contains(신사_논현_구간, 논현_신논현_구간, 신논현_강남_구간);
    }

    /**
     * Given 지하철 구간이 생성되고, 구간을 목록에 추가한다.
     * When  구간 목록 중 첫번째 구간의 상행역을 조회할 경우
     * Then  제일 먼저 추가한 구간의 상행역이 반환된다.
     */
    @Test
    void 지하철_구간_목록의_첫번째_구간의_상행역_조회() {
        // given
        StationSections 구간_목록 = new StationSections();

        StationSection 신사_논현_구간 = new StationSection(1L, 2L, 3);
        StationSection 논현_신논현_구간 = new StationSection(2L, 3L, 3);
        StationSection 신논현_강남_구간 = new StationSection(3L, 4L, 3);

        // when
        구간_목록.addSection(신사_논현_구간);
        구간_목록.addSection(논현_신논현_구간);
        구간_목록.addSection(신논현_강남_구간);

        // then
        assertThat(구간_목록.findFirstUpStation()).isEqualTo(신사_논현_구간.getUpStationId());
    }

    /**
     * Given 지하철 구간이 생성되고, 구간을 목록에 추가한다.
     * When  기존 모든 구간의 상행역과 새롭게 추가할 구간의 하행역과 비교할 때
     * When     같은 역이 존재하지 않다면
     * Then  true가 반환된다.
     */
    @Test
    void 기존_구간_목록_중_상행역과_새롭게_추가할_구간의_하행역과_비교_존재할_경우_true() {
        // given
        StationSections 구간_목록 = new StationSections();

        StationSection 신사_논현_구간 = new StationSection(1L, 2L, 3);
        StationSection 논현_신논현_구간 = new StationSection(2L, 3L, 3);
        StationSection 신논현_강남_구간 = new StationSection(3L, 4L, 3);
        StationSection 강남_양재_구간 = new StationSection(4L, 5L, 3);

        // when
        구간_목록.addSection(신사_논현_구간);
        구간_목록.addSection(논현_신논현_구간);
        구간_목록.addSection(신논현_강남_구간);

        // then
        assertThat(구간_목록.areAllUpStationsDifferentFrom(강남_양재_구간)).isTrue();
    }

    /**
     * Given 지하철 구간이 생성되고, 구간을 목록에 추가한다.
     * When  기존 모든 구간의 상행역과 새롭게 추가할 구간의 하행역과 비교할 때
     * When     같은 역이 하나라도 존재한다면
     * Then  false가 반환된다.
     */
    @Test
    void 기존_구간_목록_중_상행역과_새롭게_추가할_구간의_하행역과_비교_존재하지_않을_경우_false() {
        // given
        StationSections 구간_목록 = new StationSections();

        StationSection 신사_논현_구간 = new StationSection(1L, 2L, 3);
        StationSection 논현_신논현_구간 = new StationSection(2L, 3L, 3);
        StationSection 신논현_강남_구간 = new StationSection(3L, 4L, 3);
        StationSection 강남_양재_구간 = new StationSection(4L, 2L, 3);

        // when
        구간_목록.addSection(신사_논현_구간);
        구간_목록.addSection(논현_신논현_구간);
        구간_목록.addSection(신논현_강남_구간);

        // then
        assertThat(구간_목록.areAllUpStationsDifferentFrom(강남_양재_구간)).isFalse();
    }

    /**
     * Given 지하철 구간이 2개 이하로 존재하는 지하철 구간 목록이 생성되고
     * When  마지막 구간을 삭제할 경우,
     * Then  구간 삭제가 불가능하다.
     */
    @Test
    void 기존_지하철_구간이_2개_이하_존재하는_경우_구간_삭제_불가능() {
        // given
        StationSections 구간_목록 = new StationSections();

        StationSection 신사_논현_구간 = new StationSection(1L, 2L, 3);

        // when
        구간_목록.addSection(신사_논현_구간);

        // then
        assertThat(구간_목록.isDeletionAllowed()).isFalse();
    }

    /**
     * Given 지하철 구간이 3개 이상 존재하는 지하철 구간 목록이 생성되고
     * When  마지막 구간을 삭제할 경우,
     * Then  구간 삭제가 가능하다.
     */
    @Test
    void 기존_지하철_구간이_3개_이상_존재하는_경우_구간_삭제_가능() {
        // given
        StationSections 구간_목록 = new StationSections();

        StationSection 신사_논현_구간 = new StationSection(1L, 2L, 3);
        StationSection 논현_신논현_구간 = new StationSection(2L, 3L, 3);

        // when
        구간_목록.addSection(신사_논현_구간);
        구간_목록.addSection(논현_신논현_구간);

        // then
        assertThat(구간_목록.isDeletionAllowed()).isTrue();
    }
}
