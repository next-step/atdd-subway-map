package subway;

import static subway.util.CastUtils.IntegerToLong;

import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import subway.config.IntegrationTest;
import subway.line.SubwayLineEditRequest;
import subway.line.LineResponse;
import subway.step.StationStep;
import subway.step.SubwayLineStep;

@DisplayName("지하철 노선도 관련 기능")
@Sql(value = "/truncate-line.sql")
public class SubwayLineAcceptanceTest extends IntegrationTest{

  /**
   * When 지하철 노선을 생성하면
   * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
   */
  @DisplayName("지하철 노선 생성")
  @Test
  void 지하철_노선_생성() {

    // given
    List<Map> 지하철역_목록 = StationStep.지하철역_다중_생성_요청(List.of("정자역", "미금역"));

    // when
    Map 노선 = SubwayLineStep.지하철_노선_생성(
        Map.of(
            "name", "신분당선",
            "color", "bg-red-500",
            "upStationId"  , IntegerToLong(지하철역_목록.get(0).get("id")),
            "downStationId", IntegerToLong(지하철역_목록.get(1).get("id")),
            "distance", 10
        )
    );
    Long 노선_ID = IntegerToLong(노선.get("id"));

    // then
    LineResponse 조회한_노선 = SubwayLineStep.노선조회(노선_ID);
    Assertions.assertThat(조회한_노선.getId())
        .isEqualTo(노선_ID);
  }

  /**
   * Given 2개의 지하철 노선을 생성하고
   * When 지하철 노선 목록을 조회하면
   * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
   */
  @DisplayName("지하철노선 목록 조회")
  @Test
  void 지하철노선_목록_조회() {
    // given
    List<Map> 지하철역_목록 = StationStep.지하철역_다중_생성_요청(List.of("신림역", "봉천역", "숙명여대", "삼각지역"));
    Map 노선_1 = SubwayLineStep.지하철_노선_생성(
        Map.of(
            "name", "1호선",
            "color", "bg-red-500",
            "upStationId"  , IntegerToLong(지하철역_목록.get(0).get("id")),
            "downStationId", IntegerToLong(지하철역_목록.get(1).get("id")),
            "distance", 10
        )
    );

    Map 노선_2 = SubwayLineStep.지하철_노선_생성(
        Map.of(
            "name", "2호선",
            "color", "bg-red-500",
            "upStationId"  , IntegerToLong(지하철역_목록.get(2).get("id")),
            "downStationId", IntegerToLong(지하철역_목록.get(3).get("id")),
            "distance", 10
        )
    );

    // when
    List<LineResponse> 전체_노선조회 = SubwayLineStep.전체_노선조회();

    // then
    Assertions.assertThat(전체_노선조회)
        .asList()
        .extracting("id")
        .contains(IntegerToLong(노선_1.get("id")), IntegerToLong(노선_2.get("id")));
  }

  /**
   * Given 지하철 노선을 생성하고
   * When 생성한 지하철 노선을 조회하면
   * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
   */
  @DisplayName("지하철노선 조회")
  @Test
  void 지하철노선_조회() {

    // given
    List<Map> 지하철역_목록 = StationStep.지하철역_다중_생성_요청(List.of("봉천역", "서울대입구역"));
    Map 노선 = SubwayLineStep.지하철_노선_생성(
        Map.of(
            "name", "3호선",
            "color", "bg-red-500",
            "upStationId"  , IntegerToLong(지하철역_목록.get(0).get("id")),
            "downStationId", IntegerToLong(지하철역_목록.get(1).get("id")),
            "distance", 10
        )
    );


    // when
    Long 노선_ID = IntegerToLong(노선.get("id"));
    LineResponse 조회한_노선 = SubwayLineStep.노선조회(노선_ID);

    // then
    Assertions.assertThat(조회한_노선.getId())
        .isEqualTo(노선_ID);
  }


  /**
   * Given 지하철 노선을 생성하고
   * When 생성한 지하철 노선을 수정하면
   * Then 해당 지하철 노선 정보는 수정된다
   */
  @DisplayName("지하철노선 수정")
  @Test
  void 지하철노선_수정() {
    // given
    final String 기존_노선명 = "4호선";
    final String 변경된_노선명 = "5호선";

    List<Map> 지하철역_목록 = StationStep.지하철역_다중_생성_요청(List.of("봉천역", "서울대입구역"));
    Map 노선 = SubwayLineStep.지하철_노선_생성(
        Map.of(
            "name", 기존_노선명,
            "color", "bg-red-500",
            "upStationId"  , IntegerToLong(지하철역_목록.get(0).get("id")),
            "downStationId", IntegerToLong(지하철역_목록.get(1).get("id")),
            "distance", 10
        )
    );

    Long 노선_ID = IntegerToLong(노선.get("id"));

    // when
    SubwayLineEditRequest 노선수정 = SubwayLineEditRequest.builder()
        .name(변경된_노선명)
        .build();

    SubwayLineStep.지하철_노선_수정(노선_ID, 노선수정);

    // then
    LineResponse 조회한_노선 = SubwayLineStep.노선조회(노선_ID);
    Assertions.assertThat(조회한_노선)
        .extracting("id", "name")
        .containsExactly(노선_ID, 변경된_노선명);
  }

  /**
   * Given 지하철 노선을 생성하고
   * When 생성한 지하철 노선을 삭제하면
   * Then 해당 지하철 노선 정보는 삭제된다
   */
  @DisplayName("지하철노선 삭제")
  @Test
  void 지하철노선_삭제() {
    // given
    List<Map> 지하철역_목록 = StationStep.지하철역_다중_생성_요청(List.of("봉천역", "서울대입구역"));
    Map 노선 = SubwayLineStep.지하철_노선_생성(
        Map.of(
            "name", "x호선",
            "color", "bg-red-500",
            "upStationId"  , IntegerToLong(지하철역_목록.get(0).get("id")),
            "downStationId", IntegerToLong(지하철역_목록.get(1).get("id")),
            "distance", 10
        )
    );

    Long 노선_ID = IntegerToLong(노선.get("id"));

    // when
    SubwayLineStep.지하철_노선_삭제(노선_ID);

    // then
    SubwayLineStep.노선이_존재하지_않음(노선_ID);
  }
}
