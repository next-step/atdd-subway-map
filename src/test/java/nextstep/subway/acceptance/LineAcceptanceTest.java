package nextstep.subway.acceptance;

import io.restassured.path.json.JsonPath;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.utils.RequestParamsBuilder;
import nextstep.subway.utils.RestTestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.net.URI;

import static io.restassured.http.Method.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 관리 기능")
class LineAcceptanceTest extends AcceptanceTest {
    /**
     * When 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 성공한다.
     */
    @DisplayName("지하철 노선 생성")
    @Test
    void createLine() {
        //when
        ExtractableResponse<Response> response = 노선_생성_요청("신분당선", "bg-red-600");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * Given 새로운 지하철 노선 생성을 요청 하고
     * When 지하철 노선 목록 조회를 요청 하면
     * Then 두 노선이 포함된 지하철 노선 목록을 응답받는다
     */
    @DisplayName("지하철 노선 목록 조회")
    @Test
    void getLines() {
        //given
        노선_생성_요청("신분당선", "bg-red-600");
        노선_생성_요청("2호선", "bg-green-600");

        //when
        ExtractableResponse<Response> response = RestTestUtils.요청_테스트(URI.create("/lines"), GET);

        //then
        JsonPath jsonPath = response.jsonPath();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(jsonPath.getList("").size()).isEqualTo(2);
        assertThat(jsonPath.getList("id")).containsExactly(2, 1);
        assertThat(jsonPath.getList("name")).containsExactly("2호선", "신분당선");
        assertThat(jsonPath.getList("color")).containsExactly("bg-green-600", "bg-red-600");
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 조회를 요청 하면
     * Then 생성한 지하철 노선을 응답받는다
     */
    @DisplayName("지하철 노선 조회")
    @Test
    void getLine() {
        //given
        ExtractableResponse<Response> fixtureResponse = 노선_생성_요청("신분당선", "bg-red-600");
        URI fixtureLineUri = RestTestUtils.getLocationURI(fixtureResponse);

        //when
        ExtractableResponse<Response> response = RestTestUtils.요청_테스트(fixtureLineUri, GET);

        //then
        JsonPath jsonPath = response.jsonPath();
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(jsonPath.getInt("id")).isEqualTo(1);
        assertThat(jsonPath.getString("name")).isEqualTo("신분당선");
        assertThat(jsonPath.getString("color")).isEqualTo("bg-red-600");
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 지하철 노선의 정보 수정을 요청 하면
     * Then 지하철 노선의 정보 수정은 성공한다.
     */
    @DisplayName("지하철 노선 수정")
    @Test
    void updateLine() {
        //given
        ExtractableResponse<Response> fixtureResponse = 노선_생성_요청("신분당선", "bg-red-600");
        URI fixtureLineUri = URI.create(fixtureResponse.header("Location"));

        //when
        ExtractableResponse<Response> response = RestTestUtils.요청_테스트(fixtureLineUri, RequestParamsBuilder.<String>builder()
                .addParam("name", "구분당선")
                .addParam("color", "bg-blue-600")
                .build(), PUT);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        ExtractableResponse<Response> 업데이트_완료된_이후_조회_응답 = RestTestUtils.요청_테스트(fixtureLineUri, GET);
        JsonPath jsonPath = 업데이트_완료된_이후_조회_응답.jsonPath();
        assertThat(jsonPath.getInt("id")).isEqualTo(1);
        assertThat(jsonPath.getString("name")).isEqualTo("구분당선");
        assertThat(jsonPath.getString("color")).isEqualTo("bg-blue-600");
    }


    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 생성한 지하철 노선 삭제를 요청 하면
     * Then 생성한 지하철 노선 삭제가 성공한다.
     */
    @DisplayName("지하철 노선 삭제")
    @Test
    void deleteLine() {
        //given
        ExtractableResponse<Response> fixtureResponse = 노선_생성_요청("신분당선", "bg-red-600");
        URI fixtureLineUri = RestTestUtils.getLocationURI(fixtureResponse);

        //when
        ExtractableResponse<Response> response = RestTestUtils.요청_테스트(fixtureLineUri, DELETE);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    /**
     * Given 지하철 노선 생성을 요청 하고
     * When 같은 이름으로 지하철 노선 생성을 요청 하면
     * Then 지하철 노선 생성이 실패한다.
     */
    @DisplayName("중복이름으로 노선 생성")
    @Test
    void 중복이름으로_지하철노선을_생성하면_실패한다() {
        //given
        String fixtureLineName = "신분당선";
        ExtractableResponse<Response> fixtureResponse = 노선_생성_요청(fixtureLineName, "bg-red-600");

        //when
        ExtractableResponse<Response> response = 노선_생성_요청(fixtureLineName, "bg-red-600");

        //then
        assertThat(fixtureResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());
    }

    private ExtractableResponse<Response> 노선_생성_요청(String 노선이름, String 노선색) {
        return RestTestUtils.요청_테스트(URI.create("/lines"), RequestParamsBuilder.<String>builder()
                .addParam("name", 노선이름)
                .addParam("color", 노선색)
                .build(), POST);
    }
}
