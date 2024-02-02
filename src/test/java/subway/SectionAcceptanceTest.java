package subway;


import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class SectionAcceptanceTest {

    /*
    지하철 노선에 구간을 등록하는 기능을 구현
새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.
이미 해당 노선에 등록되어있는 역은 새로운 구간의 하행역이 될 수 없다.
새로운 구간 등록시 위 조건에 부합하지 않는 경우 에러 처리한다.
     */


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
        assertThat(statusCode).isEqualTo(HttpStatus.OK);
    }

}
