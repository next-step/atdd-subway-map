package subway;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class SectionAcceptanceTest {

    /**
     * Given 지하철역 2개를 생성하고, 지하철 노선을 생성한다.
     * Given 지하쳘역 1개를 생성한다
     * When 지하철 노선에 구간을 등록하면
     * Then 해당 지하철 노선에 구간이 등록된다
     * Then 해당 지하철 노선을 조회하면 등록된 노선이 조회된다
     */
    @Test
    void registSection() {
        // given
        Long upStationId = RequestFixtures.지하철역_생성_요청하기(Fixtures.getCreateStationParams("판교역"))
            .jsonPath().getLong("id");
        Long downStationId = RequestFixtures.지하철역_생성_요청하기(Fixtures.getCreateStationParams("강남역"))
            .jsonPath().getLong("id");
        Long lineId = RequestFixtures.지하철노선_생성_요청하기(
            Fixtures.getCreateLineParams("신분당선", "bg-red-600", upStationId.toString(),
                downStationId.toString())).jsonPath().getLong("id");
        Long newDownStationId = RequestFixtures.지하철역_생성_요청하기(Fixtures.getCreateStationParams("신사역"))
            .jsonPath().getLong("id");

        Map<String, String> params = Fixtures.getRegistSectionParams(downStationId.toString(),
            newDownStationId.toString(),
            Integer.toString(10));
        int statusCode = RequestFixtures.지하철구간_등록하기(lineId, params).statusCode();
        assertThat(statusCode).isEqualTo(HttpStatus.OK.value());
    }

    /***
     * given 지하철역 두개를 생성한다.
     *       노선을 등록한다.
     *       구간을 추가한다.
     * when 구간을 삭제한다.
     * then 성공적으로 삭제 처리된다.
     */
    @Test
    void deleteSection() {
        // given
        Long upStationId = RequestFixtures.지하철역_생성_요청하기(Fixtures.getCreateStationParams("판교역"))
            .jsonPath().getLong("id");
        Long downStationId = RequestFixtures.지하철역_생성_요청하기(Fixtures.getCreateStationParams("강남역"))
            .jsonPath().getLong("id");
        Long lineId = RequestFixtures.지하철노선_생성_요청하기(
            Fixtures.getCreateLineParams("신분당선", "bg-red-600", upStationId.toString(),
                downStationId.toString())).jsonPath().getLong("id");
        Long newDownStationId = RequestFixtures.지하철역_생성_요청하기(Fixtures.getCreateStationParams("선릉역"))
            .jsonPath().getLong("id");
        Map<String, String> params = Fixtures.getRegistSectionParams(downStationId.toString(),
            newDownStationId.toString(),
            Integer.toString(10));
        int statusCode = RequestFixtures.지하철구간_등록하기(lineId, params).statusCode();
        assertThat(statusCode).isEqualTo(HttpStatus.OK.value());

        // when
        statusCode = RequestFixtures.지하철구간_삭제하기(lineId, newDownStationId).statusCode();
        // then
        assertThat(statusCode).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
