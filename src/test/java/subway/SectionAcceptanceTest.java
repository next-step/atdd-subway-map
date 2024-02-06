package subway;


import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("구간 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class SectionAcceptanceTest {

    @Autowired
    private HibernateUtil hibernateUtil;

    @BeforeEach
    void setUp() {
        hibernateUtil.clear();
    }

    /**
     * Given 지하철역 2개를 생성하고, 지하철 노선을 생성한다.
     * Given 지하쳘역 1개를 생성한다
     * When 지하철 노선에 구간을 등록하면
     * Then 해당 지하철 노선에 구간이 등록된다
     * Then 해당 지하철 노선을 조회하면 등록된 노선이 조회된다
     */
    @Test
    @DisplayName("지하철 노선에 구간을 등록한다.")
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
     * Given 지하철역 두개를 생성한다.
     * Given 노선을 등록한다.
     * Given 지하철역 한개를 생성한다.
     * Given 구간을 추가한다.
     * When 구간을 삭제한다.
     * Then 성공적으로 삭제 처리된다.
     */
    @Test
    @DisplayName("지하철 노선에 구간을 삭제한다.")
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


    /***
     * Given 지하철역 두개를 생성한다.
     * Given 노선을 등록한다.
     * Given 지하철역 한개를 생성한다.
     * When 구간을 삭제한다.
     * Then 실패한다.
     */
    @Test
    @DisplayName("지하철 노선에 상행 종점역과 하행 종점역만 있는 경우(구간이 1개인 경우) 역을 삭제할 수 없다.")
    void deleteSectionFail() {
        // given
        Long upStationId = RequestFixtures.지하철역_생성_요청하기(Fixtures.getCreateStationParams("판교역"))
                .jsonPath().getLong("id");
        Long downStationId = RequestFixtures.지하철역_생성_요청하기(Fixtures.getCreateStationParams("강남역"))
                .jsonPath().getLong("id");
        Long lineId = RequestFixtures.지하철노선_생성_요청하기(
                Fixtures.getCreateLineParams("신분당선", "bg-red-600", upStationId.toString(),
                        downStationId.toString())).jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> response = RequestFixtures.지하철구간_삭제하기(lineId, downStationId);
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /***
     * Given 지하철역 두개를 생성한다.
     * Given 노선을 등록한다.
     * Given 지하철역 한개를 생성한다.
     * When 구간을 삭제한다.
     * Then 실패한다.
     */
    @DisplayName("지하철 노선에 등록된 역(하행 종점역)만 제거할 수 있다.")
    @Test
    void deleteSectionFail2() {
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
        statusCode = RequestFixtures.지하철구간_삭제하기(lineId, upStationId).statusCode();
        // then
        assertThat(statusCode).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /***
     * Given 지하철역 두개를 생성한다.
     * Given 노선을 등록한다.
     * Given 지하철역 한개를 생성한다.
     * When 구간을 추가한다.
     * Then 실패한다.
     */
    @DisplayName("이미 해당 노선에 등록되어있는 역은 새로운 구간의 하행역이 될 수 없다.")
    @Test
    void registSectionFail() {
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
        statusCode = RequestFixtures.지하철구간_등록하기(lineId, params).statusCode();
        // then
        assertThat(statusCode).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /***
     * Given 지하철역 두개를 생성한다.
     * Given 노선을 등록한다.
     * Given 지하철역 한개를 생성한다.
     * When 구간을 추가한다.
     * Then 실패한다.
     */
    @DisplayName("새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.")
    @Test
    void registSectionFail2() {
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
        statusCode = RequestFixtures.지하철구간_등록하기(lineId, params).statusCode();
        // then
        assertThat(statusCode).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

}
