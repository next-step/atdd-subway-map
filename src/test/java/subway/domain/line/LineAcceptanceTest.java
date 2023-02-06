package subway.domain.line;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.common.AcceptanceTest;
import subway.dto.LineResponse;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.common.AssertResponseTest.*;
import static subway.common.SetupTest.*;
import static subway.domain.line.LineApiTest.*;

@DisplayName("지하철 노선을 관리한다.")
public class LineAcceptanceTest extends AcceptanceTest {

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다.
     */
    @DisplayName("지하철 노선을 생성한다.")
    @Test
    void createLine() {
        //given
        Map<String, Object> param = new HashMap<>();
        param.put("name", "신분당선");
        param.put("color", "bg-red-600");

        //when
        var response = POST("/lines", param);

        //then
        assertAll("지하철 노선 생성 테스트 (독립적)",
                () -> 응답_상태코드_검증(HttpStatus.CREATED.value(), response.statusCode()),
                () -> 응답_정보_검증("신분당선", response.jsonPath().getString("name")));

    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("지하철노선 목록을 조회한다.")
    @Test
    void showStationLinesList(){
        //given
        신분당선_노선을_생성한다();
        분당선_노선을_생성한다();

        //when
        var response = 지하철노선을_조회한다();

        //then
        assertAll("지하철 노선 조회 테스트 (독립적)",
                () -> 응답_상태코드_검증(HttpStatus.OK.value(), response.statusCode()),
                () -> 응답_정보_검증(2, response.jsonPath().getList("name").size()));
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("특정 지하철노선을 조회한다.")
    @Test
    void showStationLineById(){
        //given
        신분당선_노선을_생성한다();
        분당선_노선을_생성한다();

        //when
        var response = 특정지하철노선을_조회한다(2);

        //then
        assertAll("특정 지하철 노선 조회 테스트 (독립적)",
                () -> 응답_상태코드_검증(HttpStatus.OK.value(), response.statusCode()),
                () -> 응답_정보_검증("분당선", response.jsonPath().getString("name")));
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("특정 지하철노선을 수정한다.")
    @Test
    void updateStationLine(){
        //given
        신분당선_노선을_생성한다();

        //when
        Map<String, String> updateParam = new HashMap<>();
        updateParam.put("name", "수정된 이름");
        updateParam.put("color", "bg-black-600");

        int id = (Integer) 지하철노선을_조회한다().jsonPath().getList("id").get(0);

        var response = 지하철노선을_수정한다(id, updateParam);

        //then
        assertAll("특정 지하철 노선 수정 테스트 (독립적)",
                () -> 응답_상태코드_검증(HttpStatus.OK.value(), response.statusCode()),
                () -> 응답_정보_검증("수정된 이름", response.jsonPath().getString("name")),
                () -> 응답_정보_검증("bg-black-600", response.jsonPath().getString("color")));
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("특정 지하철노선을 삭제한다.")
    @Test
    void deleteStationLineById(){
        신분당선_노선을_생성한다();
        분당선_노선을_생성한다();

        LineResponse deleteLine = 특정지하철노선을_조회한다(2).as(LineResponse.class);

        var response = 지하철노선을_삭제한다(Math.toIntExact(deleteLine.getId()));

        assertAll("특정 지하철 노선 삭제 테스트 (독립적)",
                () -> 응답_상태코드_검증(HttpStatus.NO_CONTENT.value(), response.statusCode()),
                () -> 응답_노선정보_미포함_검증(deleteLine.getName(), 지하철노선을_조회한다().jsonPath().getList("name")));
    }

}
