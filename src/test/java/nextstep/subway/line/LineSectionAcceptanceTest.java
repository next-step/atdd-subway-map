package nextstep.subway.line;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@DisplayName("지하철 구간 관련 테스트")
public class LineSectionAcceptanceTest {

    @DisplayName("지하철 노선에 구간을 등록한다.")
    @Test
    void createLineSection(){
        // when
        // 지하철_노선에_구간_등록_요청
        // then
        // 지하철_노선에_구간_생성됨
    }

    @DisplayName("하행 종점역이 아닌 상행역을 등록한다.")
    @Test
    void createLineSectionWithUpStation(){
        // given
        // 지하철_노선에_구간_등록_요청
        // when
        // 지하철_노선에_구간_등록_요청 (given에 입력한 종점역을 상행역으로)
        // then
        // 지하철_노선에_구간_생성_실패됨
    }

    @DisplayName("이미 등록된 역을 하행역으로 등록한다.")
    @Test
    void createLineSectionWithDownStation(){
        // given
        // 지하철_노선에_구간_등록_요청
        // when
        // 지하철_노선에_구간_등록_요청 (given에 입력한 역중 하나를 하행역으로)
        // then
        // 지하철_노선에_구간_생성_실패됨
    }
}
