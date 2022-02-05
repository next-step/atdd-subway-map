package nextstep.subway.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;

import static nextstep.subway.fixture.CommonFixture.uri;
import static nextstep.subway.fixture.LineFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선")
class LineAcceptanceTest extends AcceptanceTest {

    @Nested
    @DisplayName("한개 생성")
    class createLineTest {

        ExtractableResponse<Response> 생성결과;

        @BeforeEach
        void setUp() {
            생성결과 = 지하철_노선_생성(신분당선_이름, 신분당선_색상);
        }

        /**
         * When 지하철 노선 생성을 요청 하면
         * Then 지하철 노선 생성이 성공한다.
         */
        @DisplayName("성공")
        @Test
        void success() {
            // then
            assertThat(생성결과.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            assertThat(uri(생성결과)).isNotBlank();
        }

        /**
         * Given 지하철 노선 생성을 요청하고
         * When 같은 이름으로 지하철 노선 생성을 요청하면
         * Then 지하철 노선 생성이 실패한다.
         */
        @DisplayName("노선명이 중복이면, 노선 생성 실패")
        @Test
        void duplicateNameIsNotAllowed() {
            //when
            ExtractableResponse<Response> 중복생성_결과_response = 지하철_노선_생성(신분당선_이름, 신분당선_색상);

            // then
            assertThat(중복생성_결과_response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }
    }


    /**
     * Given 지하철 노선 생성을 요청 하고
     * Given 새로운 지하철 노선 생성을 요청 하고
     * When 지하철 노선 목록 조회를 요청 하면
     * Then 두 노선이 포함된 지하철 노선 목록을 응답받는다
     */
    @DisplayName("목록 조회")
    @Test
    void getLines() {
        /// given
        지하철_노선_생성(신분당선_이름, 신분당선_색상);
        지하철_노선_생성(구분당선_이름, 구분당선_색상);

        // when
        ExtractableResponse<Response> response = 지하철_노선_조회("/lines");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<String> lineNames = response.jsonPath().getList("name");
        assertThat(lineNames).contains(신분당선_이름, 구분당선_이름);
    }

    @Nested
    @DisplayName("한개")
    class ChangeLineStatusTest {

        String 생성된_노선_uri;

        @BeforeEach
        void setup() {
            ExtractableResponse<Response> 생성_요청_응답 = 지하철_노선_생성(신분당선_이름, 신분당선_색상);
            생성된_노선_uri = uri(생성_요청_응답);
        }

        /**
         * Given 지하철 노선 생성을 요청 하고
         * When 생성한 지하철 노선 조회를 요청 하면
         * Then 생성한 지하철 노선을 응답받는다
         */
        @DisplayName("조회")
        @Test
        void getLine() {
            // when
            ExtractableResponse<Response> 조회요청_응답 = 지하철_노선_조회(생성된_노선_uri);

            // then
            assertThat(조회요청_응답.statusCode()).isEqualTo(HttpStatus.OK.value());
            String lineName = 조회요청_응답.jsonPath().get("name");
            assertThat(lineName).isEqualTo(신분당선_이름);
        }

        /**
         * Given 지하철 노선 생성을 요청 하고
         * When 지하철 노선의 정보 수정을 요청 하면
         * Then 지하철 노선의 정보 수정은 성공한다.
         */
        @DisplayName("수정")
        @Test
        void updateLine() {
            // when
            Map<String, String> 구분당선 = 노선(구분당선_이름, 구분당선_색상);
            ExtractableResponse<Response> response = RestAssured.given().log().all()
                    .body(구분당선)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when()
                    .put(생성된_노선_uri)
                    .then().log().all()
                    .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

            ExtractableResponse<Response> updatedLine = 지하철_노선_조회(생성된_노선_uri);
            String updateName = updatedLine.jsonPath().get("name");
            assertThat(updateName).isEqualTo(구분당선_이름);
        }

        /**
         * Given 지하철 노선 생성을 요청 하고
         * When 생성한 지하철 노선 삭제를 요청 하면
         * Then 생성한 지하철 노선 삭제가 성공한다.
         */
        @DisplayName("삭제")
        @Test
        void deleteLine() {
           // when
            ExtractableResponse<Response> response = RestAssured.given().log().all()
                    .when()
                    .delete(생성된_노선_uri)
                    .then().log().all()
                    .extract();

            // then
            assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        }
    }
}
