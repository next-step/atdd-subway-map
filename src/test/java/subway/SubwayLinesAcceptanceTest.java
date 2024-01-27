package subway;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.ARRAY;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import org.springframework.test.context.jdbc.Sql;

@Sql(value="/db/subwayLine.sql")
@DisplayName("지하철 노선 기능")
@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class SubwayLinesAcceptanceTest {
    private String 일호선 = "일호선";
    private String 이호선 = "이호선";

    private String 빨간색 = "bg-red-600";

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DirtiesContext
    @Test
    void 지하철노선_생성() {
        // When
        final ExtractableResponse<Response> createResponse = createLines(일호선, 빨간색, 1L, 2L, 10L);

        // Then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // Then
        final LinesResponse createdLines = createResponse.as(LinesResponse.class);
        final ExtractableResponse<Response> listResponse = getLinesList();
        final List<LinesResponse> linesList = listResponse.jsonPath().getList(".", LinesResponse.class);
        final LinesResponse foundLines = linesList
            .stream()
            .filter(current -> Objects.equals(current.getId(), createdLines.getId()))
            .findFirst().orElse(null);

        assertThat(foundLines).isNotNull();
        assertThat(foundLines.getName()).isEqualTo(createdLines.getName());
        assertThat(foundLines.getColor()).isEqualTo(createdLines.getColor());

        // Then
        final List<Long> foundStationIdList = foundLines
            .getStations().stream().map(StationResponse::getId)
            .collect(Collectors.toList());
        final List<Long> createdStationIdList = createdLines
            .getStations().stream().map(StationResponse::getId)
            .collect(Collectors.toList());
        assertThat(foundStationIdList).containsAll(createdStationIdList);
    }


    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DirtiesContext
    @Test
    void 지하철노선_목록_조회() {
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DirtiesContext
    @Test
    void 지하철노선_조회() {
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DirtiesContext
    @Test
    void 지하철노선_수정() {
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DirtiesContext
    @Test
    void 지하철노선_삭제() {
    }

    private static ExtractableResponse<Response> getLinesList() {
        return RestAssured
            .given().log().all()
            .when()
            .get("/lines")
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> createLines(String name, String color, Long upStationId, Long downStationId, Long distance) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId.toString());
        params.put("downStationId", downStationId.toString());
        params.put("distance", distance.toString());

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(params)
            .when()
            .post("/lines")
            .then().log().all()
            .extract();
    }
}
