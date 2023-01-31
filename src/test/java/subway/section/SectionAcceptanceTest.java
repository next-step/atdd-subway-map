package subway.section;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.common.AcceptanceTest;

@DisplayName("지하철 구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {
    @Test
    void 노선에_구간을_등록한다() {
        // Given 노선에 구간을 등록하면

        // When 노선 조회 시

        // Then 등록된 구간이 조회된다.
    }

    @Test
    void 구간_등록_시_하행역이_해당_노선에_등록되어있다면_예외를_던진다() {
        // When 노선에 등록된 역을 구간의 하행역으로 등록하면
        // Then 예외를 던진다.
    }

    @Test
    void 구간_등록_시_새로운_구간의_상행역이_해당_노선의_하행_종점역이_아니면_예외를_던진다() {
        // Given 구간 등록 시
        // When 상행역이 해당 노선의 하행 종점역이 아니면
        // Then 예외를 던진다.
    }

    @Test
    void 노선에_등록된_구간을_제거한다() {
        // Given 노선에 등록된 구간을 제거하면

        // When 노선 조회 시

        // Then 삭제한 구간이 조회되지 않는다.
    }

    @Test
    void 마지막_구간이_아닌_역을_제거하면_예외를_던진다(){
        // Given 구간 삭제 시
        // When 마지막 구간이 아닌 역을 제거하면
        // Then 예외를 던진다.
    }

    @Test
    void 노선에_구간이_1개인_경우_역_삭제시_예외를_던진다(){
        // Given 구간 삭제 시
        // When 노선에 구간이 1개인 경우
        // Then 예외를 던진다.
    }
}
