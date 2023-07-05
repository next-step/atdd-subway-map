package subway;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import subway.controller.dto.line.LineModifyRequest;
import subway.controller.dto.line.LineSaveRequest;
import subway.utils.StationApiHelper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Sql("truncate_tables.sql")
@DisplayName("지하철 구간 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class SectionAcceptanceTest {

    /**
     * Given 상행종점역이 A이고, 하행종점역이 B인 지하철 노선을 생성하고
     * When 지하철역 B와 C를 연결하는 구간을 생성하면
     * Then 해당 노선이 A-B-C로 연결되고,
     * Then 지하철 노선의 하행역이 C로 바뀐다.
     */
    @DisplayName("2개역이 있는 노선에 지하철 구간을 추가한다.")
    @Test
    void createSection_성공__2개역_노선() {

    }

    /**
     * Given 상행종점역이 A이고, 하행종점역이 C인 지하철 노선 A-B-C를 생성하고
     * When 지하철역 C와 D를 연결하는 구간을 생성하면
     * Then 해당 노선이 A-B-C-D로 연결되고,
     * Then 지하철 노선의 하행역이 D로 바뀐다.
     */
    @DisplayName("3개역이 있는 노선에 지하철 구간을 추가한다.")
    @Test
    void createSection_성공__3개역_노선() {

    }

    /**
     * Given 상행종점역이 A이고, 하행종점역이 C인 지하철 노선 A-B-C를 생성하고
     * When 지하철역 C와 이미 노선에 존재하는 B를 연결하는 구간을 생성하면
     * Then 에러를 응답한다.
     */
    @DisplayName("[오류] 노선의 하행 종점역이 아닌 지하철역을 상행역으로 하는 구간을 추가한다.")
    @Test
    void createSection_에러__하행종점역이_아닌_상행역_구간() {

    }

    /**
     * Given 상행종점역이 A이고, 하행종점역이 C인 지하철 노선 A-B-C를 생성하고
     * When 지하철역 C와 이미 노선에 존재하는 B를 연결하는 구간을 생성하면
     * Then 에러를 응답한다.
     */
    @DisplayName("[오류] 이미 노선에 등록된 지하철역을 하행역으로 하는 지하철 구간을 추가한다.")
    @Test
    void createSection_에러__노선에_등록된_하행역() {

    }

}
