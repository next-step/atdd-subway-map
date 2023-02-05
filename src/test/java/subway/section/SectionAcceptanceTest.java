package subway.section;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import subway.Mocks.LineCreateRequestDTO;
import subway.Mocks.MockStation;
import subway.line.Line;
import subway.line.LineTestUtils;
import subway.station.StationTestUtils;

@DisplayName("구간 관리 기능 테스트")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class SectionAcceptanceTest {

  private static final Long 서울2호선_거리 = 10l;

  /**
   * Given 지하철 노선을 생성하고
   * When 구간을 추가하면
   * Then 노선에 구간이 추가된다.
   */
  @Test
  @DisplayName("지하철 구간 생성 테스트")
  void 노선_구간_생성_테스트() {
    // given
    Line line = LineTestUtils.역_과_노선_생성(LineCreateRequestDTO.서울2호선_노선_생성요청);
    Long 기존_노선_하행역_ID = line.getDownStation().getId();
    Long 신규역아이디 = StationTestUtils.지하철역_생성(MockStation.신림역);

    //when
    SectionCreateRequest request = new SectionCreateRequest(
        기존_노선_하행역_ID, 신규역아이디, 서울2호선_거리
    );
    ExtractableResponse<Response> response = SectionTestUtils.노선에_구간_추가(line, request);

    //then
    assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
  }

  /**
   * Given 지하철 노선을 생성하고
   * When 하행선이 아닌 역에 구간을 추가하면
   * Then 에러를 반환한다
   */
  @Test
  @DisplayName("하행선이 아닌 역에 구간 추가시 에러를 반환한다.")
  void 하행선이_아닌_구간_생성_테스트() {
    // given
    Line line = LineTestUtils.역_과_노선_생성(LineCreateRequestDTO.서울2호선_노선_생성요청);
    Long 기존_노선_상행역_ID = line.getUpStation().getId();
    Long 신규역아이디 = StationTestUtils.지하철역_생성(MockStation.신림역);

    //when
    SectionCreateRequest request = new SectionCreateRequest(
        기존_노선_상행역_ID, 신규역아이디, 서울2호선_거리
    );
    ExtractableResponse<Response> response = SectionTestUtils.노선에_구간_추가(line, request);

    //then
    assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }

  /**
   * Given 지하철 노선을 생성하고
   * When 추가하려는 구간의 하행역이 이미 노선에 등록된 경우
   * Then 에러를 반환한다.
   */
  @Test
  @DisplayName("이미 등록된 역을 새로운 하행역으로 설정하는 구간은 추가할 수 없다.")
  void 노선에_이미_등록된_역의_구간_생성_테스트() {
    // given
    Line line = LineTestUtils.역_과_노선_생성(LineCreateRequestDTO.서울2호선_노선_생성요청);
    Long 기존_노선의_역_ID = line.getUpStation().getId();
    Long 기존_노선_하행역_ID = line.getDownStation().getId();

    //when
    SectionCreateRequest request = new SectionCreateRequest(
        기존_노선_하행역_ID, 기존_노선의_역_ID, 서울2호선_거리
    );
    ExtractableResponse<Response> response = SectionTestUtils.노선에_구간_추가(line, request);

    //then
    assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
  }

  /**
   * Given 구간이 2개 이상인 지하철 노선을 생성하고
   * When 노선의 하행역 구간을 제거하면
   * Then 해당 구간이 지워진 노선이 반환된다.
   */
  @Test
  @DisplayName("노선의 하행역을 포함한 구간을 제거하는 테스트")
  void 노선_하행역_구간_제거_테스트() {
    // Given
    Line line = LineTestUtils.역_과_노선_생성(LineCreateRequestDTO.서울2호선_노선_생성요청);
    Long 기존_노선_하행역_ID = line.getDownStation().getId();
    Long 신규역아이디 = StationTestUtils.지하철역_생성(MockStation.신림역);
    SectionCreateRequest request = new SectionCreateRequest(기존_노선_하행역_ID, 신규역아이디, 서울2호선_거리);

    Long 신규_섹션_ID = SectionTestUtils.노선에_구간_추가(line, request).jsonPath().getLong("id");

    // When
    ExtractableResponse<Response> response = SectionTestUtils.노선에_구간_제거(line, 신규_섹션_ID);

    // then
    assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
  }

  /**
   * Given 지하철 노선을 생성하고
   * When 노선의 하행역이 아닌 구간을 제거하면
   * Then 에러를 반환한다.
   */
  @Test
  @DisplayName("하행역이 포함된 구간이 아닌 경우 지울 수 없다.")
  void 하행역이_포함안된_구간_삭제_실패_테스트() {
    // Given
    Line line = LineTestUtils.역_과_노선_생성(LineCreateRequestDTO.서울2호선_노선_생성요청);
    Long 기존_노선_하행역_ID = line.getDownStation().getId();
    Long 신규역아이디 = StationTestUtils.지하철역_생성(MockStation.신림역);

    // When
    SectionCreateRequest 서울2호선_하행선_구간_추가_요청 = new SectionCreateRequest(기존_노선_하행역_ID, 신규역아이디, 서울2호선_거리);
    SectionTestUtils.노선에_구간_추가(line, 서울2호선_하행선_구간_추가_요청);

    // Then
    assertThrows(RuntimeException.class, () -> SectionTestUtils.노선에_구간_제거(line, line.getSections().get(0).getId()));
  }

  /**
   * Given 구간이 1개인 지하철 노선을 생성하고
   * When 노선을 제거하면
   * 에러를 반환한다.
   */
  @Test
  @DisplayName("구간이 한 개인 역은 지울 수 없다")
  void 구간이_한개면_삭제_실패_테스트() {
    Line line = LineTestUtils.역_과_노선_생성(LineCreateRequestDTO.서울2호선_노선_생성요청);

    assertThrows(RuntimeException.class,
        () ->SectionTestUtils.노선에_구간_제거(line, line.getSections().get(0).getId())
    );
  }
}
