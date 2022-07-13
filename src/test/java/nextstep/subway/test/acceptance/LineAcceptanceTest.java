package nextstep.subway.test.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static nextstep.subway.test.acceptance.StationTestMethod.*;
import static nextstep.subway.test.acceptance.LineTestMethod.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * @author a1101466 on 2022/07/10
 * @project subway
 * @description
 */
public class LineAcceptanceTest extends AcceptanceTest{

    public String 파랑선_시작ID;
    public String 파랑선_종착ID;
    public String 초록선_시작ID;
    public String 초록선_종착ID;

    public Long 초록선_라인ID;

    @BeforeEach
    public void lineTestSetUp() {
        파랑선_시작ID = 지하철역_생성("구일역").jsonPath().getString("id");
        파랑선_종착ID = 지하철역_생성("구로역").jsonPath().getString("id");;
        초록선_시작ID = 지하철역_생성("신도립역").jsonPath().getString("id");;
        초록선_종착ID = 지하철역_생성("문래역").jsonPath().getString("id");;

        지하철노선_생성("파랑선", 파랑선_시작ID, 파랑선_종착ID, "blue", "10");
        초록선_라인ID = 지하철노선_생성("초록선", 초록선_시작ID, 초록선_종착ID, "green", "10")
                .jsonPath().getLong("id");
    }
    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다.
     */
    @Test
    @DisplayName("지하철노선을 생성한다")
    void createLine(){

        String 보라선_시작ID = 지하철역_생성("목동역").jsonPath().getString("id");
        String 보라선_종착ID = 지하철역_생성("오목교").jsonPath().getString("id");

        ExtractableResponse<Response> response =
                지하철노선_생성("보라선", 보라선_시작ID, 보라선_종착ID, "purple", "29" );
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.jsonPath().getLong("id")).isNotNull(),
                () -> assertThat(response.jsonPath().getString("name")).contains("보라선")
        );

    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @Test
    @DisplayName("지하철노선 목록 조회")
    void getLineList(){

        ExtractableResponse<Response> response = 지하철노선_목록조회();
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath().getList("name")).contains("초록선", "파랑선")
        );

    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 시하철 노선의 정보를 응답받을 수 있다.
     */
    @Test
    @DisplayName("지하철노선 조회")
    void getLine(){

        ExtractableResponse<Response> response = 지하철노선_단일조회(초록선_라인ID);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath().getString("name")).isEqualTo("초록선")
        );

    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다.
     */
    @Test
    @DisplayName("지하철조선 수정")
    void modifyLine(){

        Map<String, String> params = new HashMap<>();
        params.put("name", "노랑선");
        params.put("color", "yellow");

        지하철노선_수정(params, 초록선_라인ID);

        ExtractableResponse<Response> response = 지하철노선_단일조회(초록선_라인ID);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath().getString("name")).isEqualTo("노랑선"),
                () -> assertThat(response.jsonPath().getString("color")).isEqualTo("yellow")
        );

    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다.
     */
    @Test
    @DisplayName("지하철노선 삭제")
    void deleteLine(){

        ExtractableResponse<Response> response = 지하철노선_삭제(초록선_라인ID);


        List<String> lineName = 지하철노선_목록조회().jsonPath().getList("name");

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(lineName).doesNotContain("초록선")
        );
    }
}
