package subway;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;
import subway.config.IntegrationTest;
import subway.line.SubwayLineEditRequest;
import subway.line.SubwayLineRequest;
import subway.line.SubwayLineResponse;
import subway.step.SubwayLineStep;

@DisplayName("지하철 노선도 관련 기능")
@Sql(value = "/truncate-subwayline.sql")
public class SubwayLineAcceptanceTest extends IntegrationTest{

  /**
   * When 지하철 노선을 생성하면
   * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
   */
  @DisplayName("지하철 노선 생성")
  @Test
  void 지하철_노선_생성() {

    // given
    SubwayLineRequest 신규노선 = SubwayLineRequest.builder()
        .name("신분당선")
        .color("bg-red-600")
        .upStationId(1L)
        .downStationId(2L)
        .distance(10)
        .build();

    // when
    SubwayLineResponse 지하철_노선 = SubwayLineStep.지하철_노선_생성(신규노선);

    // then
    SubwayLineResponse line = SubwayLineStep.노선조회(지하철_노선.getId());
    Assertions.assertThat(line.getId())
        .isEqualTo(지하철_노선.getId());
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
    SubwayLineResponse 노선_1 = SubwayLineStep.지하철_노선_생성(
        SubwayLineRequest.builder()
            .name("1호선")
            .color("bg-red-500")
            .upStationId(4L)
            .downStationId(5L)
            .distance(10)
            .build()
    );

    SubwayLineResponse 노선_2 = SubwayLineStep.지하철_노선_생성(
        SubwayLineRequest.builder()
            .name("2호선")
            .color("bg-red-400")
            .upStationId(6L)
            .downStationId(7L)
            .distance(10)
            .build()
    );

    // when
    List<SubwayLineResponse> 전체_노선조회 = SubwayLineStep.전체_노선조회();

    // then
    Assertions.assertThat(전체_노선조회)
        .asList()
        .extracting("id")
        .contains(노선_1.getId(), 노선_2.getId());
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
    SubwayLineRequest 신규노선 = SubwayLineRequest.builder()
        .name("3호선")
        .color("bg-red-500")
        .upStationId(4L)
        .downStationId(5L)
        .distance(10)
        .build();

    SubwayLineResponse 노선 = SubwayLineStep.지하철_노선_생성(신규노선);

    // when
    SubwayLineResponse 조회한_노선 = SubwayLineStep.노선조회(노선.getId());

    // then
    Assertions.assertThat(조회한_노선.getId())
        .isEqualTo(노선.getId());
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

    SubwayLineRequest 신규노선 = SubwayLineRequest.builder()
        .name(기존_노선명)
        .color("bg-red-500")
        .upStationId(4L)
        .downStationId(5L)
        .distance(10)
        .build();

    SubwayLineResponse 노선 = SubwayLineStep.지하철_노선_생성(신규노선);

    // when
    SubwayLineEditRequest 노선수정 = SubwayLineEditRequest.builder()
        .name(변경된_노선명)
        .build();

    SubwayLineStep.지하철_노선_수정(노선.getId(), 노선수정);

    // then
    SubwayLineResponse 조회한_노선 = SubwayLineStep.노선조회(노선.getId());
    Assertions.assertThat(조회한_노선)
        .extracting("id", "name")
        .containsExactly(노선.getId(), 변경된_노선명);
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
    SubwayLineRequest 신규노선 = SubwayLineRequest.builder()
        .name("6호선")
        .color("bg-red-500")
        .upStationId(4L)
        .downStationId(5L)
        .distance(10)
        .build();

    SubwayLineResponse 노선 = SubwayLineStep.지하철_노선_생성(신규노선);

    // when
    SubwayLineStep.지하철_노선_삭제(노선.getId());

    // then
    SubwayLineStep.노선이_존재하지_않음(노선.getId());
  }
}
