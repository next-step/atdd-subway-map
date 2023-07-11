package subway;

import static subway.util.CastUtils.IntegerToLong;

import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import subway.config.IntegrationTest;
import subway.step.LineSectionStep;
import subway.step.StationStep;
import subway.step.SubwayLineStep;


@Sql(value = "/truncate-subwayline.sql")
@Sql(value = "/truncate-station.sql")
@Sql(value = "/truncate-section.sql")
@DisplayName("지하철 노선 구간 관련 기능")
public class LineSectionAcceptanceTest extends IntegrationTest {

  /**
   * given 지하철 역과 노선을 생성하고
   * <p>
   * when 노선의 하행 종점역이 기점이고, 신규역이 하행역인 구간을 생성하면
   * <p>
   * then 해당 노선의 구간 조회시, 생성한 구간을 조회 할 수 있다.
   */
  @Test
  @DisplayName("지하철의 구간을 생성한다.")
  void 지하철_구간_생성() {

    // given
    List<Map> 지하철역_목록 = StationStep.지하철역_다중_생성_요청(List.of("봉천역", "서울대입구역", "낙성대역"));
    Map 지하철_노선 = SubwayLineStep.지하철_노선_생성(
        Map.of(
            "name", "2호선 (구간생성테스트)",
            "color", "bg-red-500",
            "upStationId"  , IntegerToLong(지하철역_목록.get(0).get("id")),
            "downStationId", IntegerToLong(지하철역_목록.get(1).get("id")),
            "distance", 10
        )
    );

    // when
    Map 지하철_구간_생성 = LineSectionStep.지하철_구간_생성(IntegerToLong(지하철_노선.get("id")),
        Map.of(
            "downStationId", 지하철역_목록.get(2).get("id"),
            "upStationId", 지하철역_목록.get(1).get("id"),
            "distance", 10L
        ));

    // then
    List<Map> 지하철_구간_목록 = LineSectionStep.지하철_구간_목록_조회(IntegerToLong(지하철_구간_생성.get("lineId")));
    Assertions.assertThat(지하철_구간_목록)
        .asList()
        .extracting("lineId")
        .contains(지하철_구간_생성.get("lineId"));
  }


  /**
   * given 지하철 역과 노선을 생성하고
   * <p>
   * when 구간의 기점이 하행 종점역이고, 하행역이 "해당 노선의 역 중 하나" 이면
   * <p>
   * then 구간을 생성 할 수 없다.
   */
  @Test
  @DisplayName("신규 구간의 하행역은 노선의 역중 하나 일 수 없다.")
  void 신규_구간의_하행역은_노선의_역중_하나_일_수_없다() {

    // given
    List<Map> 지하철역_목록 = StationStep.지하철역_다중_생성_요청(List.of("봉천역", "서울대입구역", "낙성대역"));
    Map 지하철_노선 = SubwayLineStep.지하철_노선_생성(
        Map.of(
            "name", "2호선 (구간생성테스트)",
            "color", "bg-red-500",
            "upStationId"  , IntegerToLong(지하철역_목록.get(0).get("id")),
            "downStationId", IntegerToLong(지하철역_목록.get(1).get("id")),
            "distance", 10
        )
    );

    // when && then
    LineSectionStep.지하철_구간_생성에_실패한다(
      IntegerToLong(지하철_노선.get("id")),
      Map.of(
          "upStationId", 지하철역_목록.get(1).get("id"),
          "downStationId", 지하철역_목록.get(0).get("id"), // 기존 노선에 존재하는 역
          "distance", 10L
      ));
  }

  /**
   * given 지하철 역과 노선을 생성하고
   * <p>
   * when 구간의 기점이 하행 종점역아니면
   * <p>
   * then 구간을 생성 할 수 없다.
   */
  @Test
  @DisplayName("신규 구간의 상행역은 구간의 하행종점역이 아니면 생성될 수 없다.")
  void 신규_구간의_상행역은_구간의_하행종점역이여야함() {

    // given
    List<Map> 지하철역_목록 = StationStep.지하철역_다중_생성_요청(List.of("봉천역", "서울대입구역", "낙성대역"));
    Map 지하철_노선 = SubwayLineStep.지하철_노선_생성(
        Map.of(
            "name", "2호선 (구간생성테스트)",
            "color", "bg-red-500",
            "upStationId"  , IntegerToLong(지하철역_목록.get(0).get("id")),
            "downStationId", IntegerToLong(지하철역_목록.get(1).get("id")),
            "distance", 10
        )
    );

    // when && then
    Map 지하철_구간_생성 = LineSectionStep.지하철_구간_생성에_실패한다(
        IntegerToLong(지하철_노선.get("id")),
        Map.of(
            "upStationId", 지하철역_목록.get(0).get("id"),   // 기존 구간의 하행 종점역이 아닌 역
            "downStationId", 지하철역_목록.get(2).get("id"),
            "distance", 10L
        ));
  }
}
