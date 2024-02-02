package subway.line;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import subway.controller.dto.LineCreateRequestBody;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철 노선 구간 관련 기능")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SectionAcceptanceTest {
    private final String routePrefix = "/lines";

    @Nested
    class SectionCreateTest {
        /**
         * Given 지하철 역 2개와 이 역들을 포함하는 노선을 1개 생성하고
         * When 앞서 생성된 노선의 하행역을 상행역으로 지정하지 않고 새 노선을 추가하려는 경우
         * Then 노선은 추가되지 않고 에러가 발생한다.
         */
        @DisplayName("등록하려는 상행역이 기존 노선의 하행역이 아닌 경우 에러가 발생한다.")
        @Test
        void createLineSectionFailForUpStationValidation() {
            // given
            Long upStationId = setupStationByName("강남역");
            Long downStationId = setupStationByName("역삼역");
            Long willAddedStationId = setupStationByName("선릉역");

            Long createdLine = setupLine(
                    "2호선", "bg-green-600", upStationId, downStationId, 10
            );

            // when
            RestAssured.given().log().all()
                    .body(Map.of("downStationId", willAddedStationId, "upStationId", upStationId, "distance", 10))
                    .pathParam("id", createdLine)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().post(routePrefix + "/{id}/sections")
                    .then().log().all()
                    .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());

            // then
            assertThat(getJsonPathOfGetLine(createdLine).getList("stations.id", Long.class))
                    .containsExactly(upStationId, downStationId);
        }

        /**
         * Given 지하철 역 2개와 이 역들을 포함하는 노선을 1개 생성하고
         * When 앞서 생성된 지하철 역 중 하나를 하행선으로 두는 노선을 추가하려는 경우
         * Then 노선은 추가되지 않고 에러가 발생한다.
         */
        @DisplayName("등록하려는 하행역이 기존 노선에 이미 존재하는 역인 경우 에러가 발생한다.")
        @Test
        void createLineSectionFailForDownStationValidation() {
            // given
            Long upStationId = setupStationByName("강남역");
            Long downStationId = setupStationByName("역삼역");
            Long willAddedStationId = setupStationByName("선릉역");

            Long createdLine = setupLine(
                    "2호선", "bg-green-600", upStationId, downStationId, 10
            );

            // when
            RestAssured.given().log().all()
                    .body(Map.of("downStationId", upStationId, "upStationId", downStationId, "distance", 10))
                    .pathParam("id", createdLine)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().post(routePrefix + "/{id}/sections")
                    .then().log().all()
                    .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());

            // then
            assertThat(getJsonPathOfGetLine(createdLine).getList("stations.id", Long.class))
                    .containsExactly(upStationId, downStationId);
        }


        /**
         * Given 지하철 역 2개와 이 역들을 포함하는 노선을 1개 생성하고
         * When 앞서 생성된 지하철 역 중 하행역을 상행선으로 두고 새 노선을 추가하면
         * Then 노선 조회 시 세 개의 역이 조회된다.
         */
        @DisplayName("지하철 노선 구간을 추가한다.")
        @Test
        void createLineSection() {
            // given
            Long upStationId = setupStationByName("강남역");
            Long downStationId = setupStationByName("역삼역");
            Long willAddedStationId = setupStationByName("선릉역");

            Long createdLine = setupLine(
                    "2호선", "bg-green-600", upStationId, downStationId, 10
            );

            // when
            RestAssured.given().log().all()
                    .body(Map.of("downStationId", willAddedStationId, "upStationId", downStationId, "distance", 10))
                    .pathParam("id", createdLine)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().post(routePrefix + "/{id}/sections")
                    .then().log().all()
                    .statusCode(HttpStatus.CREATED.value());

            // then
            assertThat(getJsonPathOfGetLine(createdLine).getList("stations.id", Long.class))
                    .containsExactly(upStationId, downStationId, willAddedStationId);
        }
    }

    @Nested
    class SectionDeleteTest {

        /**
         * Given 지하철 역 2개만 포함하는 노선을 1개 생성하고
         * When 앞서 생성한 노선에서 역을 하나 제거하려는 경우
         * Then 노선을 제거되지 않고 에러가 발생한다.
         */
        @DisplayName("구간이 1개인 경우 역을 제거하려 시도하면 에러가 발생한다.")
        @Test
        void deleteLineSectionFailWithOneLine() {
            // given
            Long upStationId = setupStationByName("강남역");
            Long downStationId = setupStationByName("역삼역");
            Long willAddedStationId = setupStationByName("선릉역");

            Long createdLine = setupLine(
                    "2호선", "bg-green-600", upStationId, downStationId, 10
            );

            // when
            RestAssured.given().log().all()
                    .queryParam("stationId", upStationId)
                    .pathParam("id", createdLine)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().delete(routePrefix + "/{id}/sections")
                    .then().log().all()
                    .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());

            // then
            assertThat(getJsonPathOfGetLine(createdLine).getList("stations.id", Long.class))
                    .containsExactly(upStationId, downStationId);
        }

        /**
         * Given 지하철 역 2개와 이 역들을 포함하는 노선을 1개 생성하고
         * When 상행역을 구간에서 제거하려는 경우
         * Then 노선을 제거되지 않고 에러가 발생한다.
         */
        @DisplayName("하행역이 아닌 역을 제거하려는 경우 에러가 발생한다.")
        @Test
        void deleteLineSectionFailWithUpStation() {
            // given
            Long upStationId = setupStationByName("강남역");
            Long downStationId = setupStationByName("역삼역");
            Long willAddedStationId = setupStationByName("선릉역");

            Long createdLine = setupLine(
                    "2호선", "bg-green-600", upStationId, downStationId, 10
            );

            RestAssured.given().log().all()
                    .body(Map.of("downStationId", willAddedStationId, "upStationId", downStationId, "distance", 10))
                    .pathParam("id", createdLine)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().post(routePrefix + "/{id}/sections")
                    .then().log().all()
                    .statusCode(HttpStatus.CREATED.value());

            // when
            RestAssured.given().log().all()
                            .queryParam("stationId", upStationId)
                            .pathParam("id", createdLine)
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .when().delete(routePrefix + "/{id}/sections")
                            .then().log().all()
                            .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value());

            // then
            assertThat(getJsonPathOfGetLine(createdLine).getList("stations.id", Long.class))
                    .containsExactly(upStationId, downStationId, willAddedStationId);
        }

        /**
         * Given 지하철 역 3개로 2개의 구간을 가진 1개의 노선을 생성 후
         * When 하행역을 제거하면
         * Then 지하철 역 2개, 1개의 구간을 가진 1개의 노선이 조회된다.
         */
        @DisplayName("하행역을 제거함으로써 구간을 제거한다.")
        @Test
        void deleteLineSectionSuccess() {
            // given
            Long upStationId = setupStationByName("강남역");
            Long downStationId = setupStationByName("역삼역");
            Long willAddedStationId = setupStationByName("선릉역");

            Long createdLine = setupLine(
                    "2호선", "bg-green-600", upStationId, downStationId, 10
            );

            RestAssured.given().log().all()
                    .body(Map.of("downStationId", willAddedStationId, "upStationId", downStationId, "distance", 10))
                    .pathParam("id", createdLine)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().post(routePrefix + "/{id}/sections")
                    .then().log().all()
                    .statusCode(HttpStatus.CREATED.value());

            // when
            RestAssured.given().log().all()
                    .queryParam("stationId", willAddedStationId)
                    .pathParam("id", createdLine)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .when().delete(routePrefix + "/{id}/sections")
                    .then().log().all()
                    .statusCode(HttpStatus.NO_CONTENT.value());

            // then
            assertThat(getJsonPathOfGetLine(createdLine).getList("stations.id", Long.class))
                    .containsExactly(upStationId, downStationId);
        }

    }

    // TODO: 아래 private 함수들 test util 같은 곳으로 옮기기
    private Long setupStationByName(String stationName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", stationName);
        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/stations")
                .then().log().all()
                .extract().jsonPath().getLong("id");
    }

    private Long setupLine(
            String name,
            String color,
            Long upStationId,
            Long downStationId,
            int distance
    ) {
        LineCreateRequestBody lineCreateRequestBody = new LineCreateRequestBody(
                name, color, upStationId, downStationId, distance
        );
        return RestAssured.given().log().all()
                .body(lineCreateRequestBody)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(routePrefix)
                .then().log().all().extract().jsonPath().getLong("id");
    }

    private JsonPath getJsonPathOfGetLine(Long lineId) {
        return RestAssured.given().log().all()
                .pathParam("id", lineId)
                .when().get(routePrefix + "/{id}")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract().jsonPath();
    }
}
