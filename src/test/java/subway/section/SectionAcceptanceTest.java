package subway.section;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import subway.AcceptanceTest;
import subway.line.LineRequest;
import subway.station.StationRequest;

import java.util.Map;

/**
 * 프로그래밍 요구사항
 * - 인수 테스트 주도 개발 프로세스에 맞춰서 기능을 구현하세요.
 *   - 요구사항 설명을 참고하여 인수 조건을 정의
 *   - 인수 조건을 검증하는 인수 테스트 작성
 *   - 인수 테스트를 충족하는 기능 구현
 * - 인수 조건은 인수 테스트 메서드 상단에 주석으로 작성하세요.
 *   - 뼈대 코드의 인수 테스트를 참고
 * - 인수 테스트의 결과가 다른 인수 테스트에 영향을 끼치지 않도록 인수 테스트를 서로 격리 시키세요.
 * - 인수 테스트의 재사용성과 가독성, 그리고 빠른 테스트 의도 파악을 위해 인수 테스트를 리팩터링 하세요.
 */
@DisplayName("지하철 구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {
    /**
     * # 구간 등록 기능
     * ## 요구사항
     * - 지하철 노선에 구간을 등록한다.
     * - 새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다. 즉, 새로운 구간이 등록될 때, "기존 구간의 하행역 == 새로운 구간의 상행 역"이여야 등록 가능하다.
     * - 새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다. -> 하행역이 N개가 될 수 있으므로..
     * - 새로운 구간 등록시 위 조건에 부합하지 않는 경우 에러 처리한다.
     * ## Request
     * - POST /lines/{lineId}/sections
     * - application/json
     * - "downStationId" : 하행 역의 id
     * - "upStationId" : 상행 역의 id
     * - "distance" : 하행 역과 상행 역 간의 거리
     * ## 시나리오
     * Given : 지하철역을 3개 생성하고
     * And : 지하철 노선을 1개 생성한 후
     * When : 새로운 구간을 등록하면
     * Then : 노선에 새로운 구간이 등록된다
     */
    @DisplayName("지하철 구간 등록")
    @Test
    void registerSectionOk() {
        // given
        long 노선_상행_Id = 응답_결과에서_Id를_추출한다(StationRequest.지하철역을_생성한다("강남역"));
        long 노선_하행_Id = 응답_결과에서_Id를_추출한다(StationRequest.지하철역을_생성한다("양재역"));
        long 구간_하행_Id = 응답_결과에서_Id를_추출한다(StationRequest.지하철역을_생성한다("양재시민의숲역"));

        long lineId = 응답_결과에서_Id를_추출한다(LineRequest.지하철_노선을_생성한다(노선_상행_Id, 노선_하행_Id, "신분당선"));

        // when
        Map<String, ? extends Number> params = Map.of(
                "upStationId", 노선_하행_Id,
                "downStationId", 구간_하행_Id,
                "distance", 10
        );

        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .pathParam("lineId", lineId)
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/lines/{lineId}/sections")
                .then().log().all()
                .extract();


    }

    private long 응답_결과에서_Id를_추출한다(ExtractableResponse<Response> responseOfCreateStation) {
        return responseOfCreateStation.jsonPath().getLong("id");
    }

    /**
     * Given : 새로운 노선을 1개 생성하고
     * When : 상행역이 해당 노선의 하행 종점역이 아닌 새로운 구간을 등록하면
     * Then : 예외가 발생한다
     */
    @DisplayName("지하철 구간 등록 예외 케이스 : 노선의 하행 종점역 != 새로운 노선의 상행역")
    @Test
    void registerSectionFailCase1() {

    }

    /**
     * Given : 새로운 노선을 1개 생성하고
     * When : 해당 노선에 등록되어 있는 하행 역을 가진 새로운 구간을 등록하면
     * Then : 예외가 발생한다
     */
    @DisplayName("지하철 구간 등록 예외 케이스 : 노선에 등록된 하행 역을 가진 새로운 구간 등록")
    @Test
    void registerSectionFailCase2() {

    }


    /**
     * # 구간 제거 기능
     * ## 요구사항
     * - 지하철 노선에 구간을 제거한다.
     * - 지하철 노선에 등록된 "하행 종점역"만 제거할 수 있다. (마지막 구간만 제거)
     * - 지하철 노선에 상행 종점역과 하행 종점역만 있는 경우, 즉 구간이 1개인 경우 역을 삭제할 수 없다.
     * - 새로운 구간 제거 시, 위 조건에 부합하지 않는 경우 에러 처리한다.
     * ## Request
     * - DELETE /lines/{lineId}/sections?stationId={stationId}
     * ## 시나리오
     * Given : 지하철 노선을 1개 등록하고
     * And : 새로운 구간을 1개 등록한 후
     * When : 하행 종점역을 제거하면
     * Then : 구간이 삭제된다
     */
    @DisplayName("지하철 구간 삭제")
    @Test
    void deleteSectionOk() {

    }

    /**
     * Given : 지하철 노선을 1개 등록하고
     * And : 새로운 구간을 1개 등록한 후
     * When : 하행 종점역(마지막 구간)이 아닌 구간을 제거하면
     * Then : 예외가 발생한다.
     */
    @DisplayName("지하철 구간 삭제 예외 케이스 : 하행 종점역이 아닌 구간을 제거")
    @Test
    void deleteSectionFailCase1() {

    }

    /**
     * Given : 지하철 노선을 1개 등록하고
     * When : 하행 종점역을 제거하면
     * Then : 예외가 발생한다.
     */
    @DisplayName("지하철 구간 삭제 예외 케이스 : 구간이 1개일 때 하행 종점역을 제거")
    @Test
    void deleteSectionFailCase2() {

    }
}
