package subway;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static subway.LineTestUtils.지하철_노선_목록_조회;
import static subway.LineTestUtils.지하철_노선_삭제;
import static subway.LineTestUtils.지하철_노선_수정;
import static subway.LineTestUtils.지하철_노선_조회;
import static subway.LineTestUtils.지하철_역_노선_모두_생성;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import subway.Mocks.Color;
import subway.Mocks.MockLine;
import subway.Mocks.MockStation;
import subway.line.Line;

@DisplayName("지하철 노선 관리 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class LineAcceptanceTest {

  private static final Long 서울2호선_거리 = 10l;
  private static final Long 신분당선_거리 = 20l;

  /**
   * When 지하철 노선을 생성하면
   * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
   */
  @DisplayName("지하철노선 생성 테스트")
  @Test
  void 지하철_노선_생성_테스트() {
    // when
    Line line = 지하철_역_노선_모두_생성(MockLine.서울2호선, Color.초록,MockStation.서울대입구역,MockStation.봉천역, 서울2호선_거리);

    ExtractableResponse<Response> response = 지하철_노선_조회(line.getId());

    assertAll(
        () -> assertThat(response.jsonPath().getString("name")).isEqualTo(line.getName()),
        () -> assertThat(response.jsonPath().getString("upStation.name")).isEqualTo(MockStation.서울대입구역),
        () -> assertThat(response.jsonPath().getString("downStation.name")).isEqualTo(MockStation.봉천역)
    );
  }

  /**
   * Given 2개의 지하철 노선을 생성하고
   * When 지하철 노선 목록을 조회하면
   * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
   */
  @DisplayName("지하철노선 목록 조회 테스트")
  @Test
  void 지하철_노선_목록_조회_테스트() {
    Line line1 = 지하철_역_노선_모두_생성(MockLine.서울2호선, Color.초록,MockStation.서울대입구역,MockStation.봉천역, 서울2호선_거리);
    Line line2 = 지하철_역_노선_모두_생성(MockLine.신분당선, Color.노랑,MockStation.강남역,MockStation.신사역, 신분당선_거리);

    assertThat(
        지하철_노선_목록_조회().jsonPath().getList("name", String.class)
    ).containsAll(List.of(line1.getName(),line2.getName()));
  }

  /**
   * Given 지하철 노선을 생성하고
   * When 생성한 지하철 노선을 조회하면
   * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
   */
  @DisplayName("지하철노선 조회 테스트")
  @Test
  void 지하철_노선_조회_테스트() {
    // given
    Line created = 지하철_역_노선_모두_생성(MockLine.서울2호선, Color.초록,MockStation.서울대입구역,MockStation.봉천역, 서울2호선_거리);

    // when
    ExtractableResponse<Response> show = 지하철_노선_조회(created.getId());

    // then
    assertThat(show.jsonPath().getString("name")).isEqualTo(created.getName());
  }

  /**
   * Given 지하철 노선을 생성하고
   * When 생성한 지하철 노선을 수정하면
   * Then 해당 지하철 노선 정보는 수정된다
   */
  @DisplayName("지하철노선 수정 테스트")
  @Test
  void 지하철_노선_수정_테스트() {
    // given
    Line created = 지하철_역_노선_모두_생성(MockLine.서울2호선, Color.초록,MockStation.서울대입구역,MockStation.봉천역, 서울2호선_거리);

    // when
    Line updated = 지하철_노선_수정(created.getId(), "이름이_바뀐_2호선", Color.노랑);

    // then
    Line show = 지하철_노선_조회(updated.getId()).as(Line.class);
    assertAll(
        () -> assertThat(show.getName()).isEqualTo("이름이_바뀐_2호선"),
        () -> assertThat(show.getColor()).isEqualTo(Color.노랑)
    );
  }

  /**
   * Given 지하철 노선을 생성하고
   * When 생성한 지하철 노선을 삭제하면
   * Then 해당 지하철 노선 정보는 삭제된다.
   */
  @DisplayName("지하철노선 삭제 테스트")
  @Test
  void 지하철_노선_삭제_테스트() {
    // given
    Line line = 지하철_역_노선_모두_생성(MockLine.서울2호선, Color.초록,MockStation.서울대입구역,MockStation.봉천역, 서울2호선_거리);

    // when
    지하철_노선_삭제(line.getId());

    // then
    assertThat(지하철_노선_조회(line.getId()).statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
  }
}
