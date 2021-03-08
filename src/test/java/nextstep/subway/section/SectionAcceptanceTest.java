package nextstep.subway.section;

import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("지하철 구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철 노선에 구간을 등록한다.")
    @Test
    void createSection() {
        // 지하철노선_구간_등록요청
        // 지하철노선_구간_등록됨
    }

    @DisplayName("지하철 노선에 구간을 제거한다.")
    @Test
    void deleteLine() {
        // 지하철노선_구간_등록요청
        // 지하철노선_구간_제거요청
        // 지하철노선_구간_삭제됨
    }

    @DisplayName("지하철 노선에 등록된 구간 정보로 역목록을 조회한다.")
    @Test
    void getStationsBySectionInformation() {
        // 지하철노선_구간_등록요청
        // 지하철노선_구간역목록_조회요청
        // 지하철노선_구간역록목_응답됨
        // 지하철노선_구간역록목_포함됨
    }
}
