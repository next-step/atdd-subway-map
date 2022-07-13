package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.applicaion.dto.SectionDeleteRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static nextstep.subway.acceptance.LineRequestCollection.지하철_노선_생성;
import static nextstep.subway.acceptance.LineRequestCollection.지하철_단일_노선_조회;
import static nextstep.subway.acceptance.SectionRequestCollection.*;
import static nextstep.subway.acceptance.StationRequestCollection.지하철역_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("지하철 구간 괸련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {

    ExtractableResponse<Response> 강남역;
    ExtractableResponse<Response> 건대입구역;
    Long 강남역Id;
    Long 건대입구역Id;
    ExtractableResponse<Response> 이호선;
    ExtractableResponse<Response> 성수역;
    Long 이호선Id;
    Long 성수역Id;

    @BeforeEach
    void init() {
        강남역 = 지하철역_생성("강남역");
        건대입구역 = 지하철역_생성("건대입구역");
        강남역Id = getId(강남역);
        건대입구역Id = getId(건대입구역);

        이호선 = 지하철_노선_생성("2호선", "bg-blue-600", 강남역Id, 건대입구역Id, 10);
        성수역 = 지하철역_생성("성수역");
        이호선Id = getId(이호선);
        성수역Id = getId(성수역);
    }

    /**
     * Given 지하철 노선을 1개 생성
     * Given 추가할 역 1개 생성
     * When 기존의 노선에 등록된 하행역을 상행역으로 하고
     * When 새로 생성한 역을 하행역으로 하고
     * When 구간 생성 요청을 하면
     * Then 정상적으로 구간이 노선에 추가된다.
     */
    @Test
    @DisplayName("정상적으로 역을 생성하여 기존 노선 끝에 구간을 추가한다")
    public void addSection() {
        // when
        int statusCode = 지하철_구간_생성_요청(이호선, 성수역);

        // then
        ExtractableResponse<Response> response = 지하철_단일_노선_조회(이호선Id);
        List<String> stationList = response.jsonPath().getList("stations.name", String.class);
        assertAll(
                () -> assertThat(statusCode).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(stationList).containsExactly("강남역", "건대입구역", "성수역")
        );
    }

    /**
     * Given 지하철 노선을 1개 생성
     * Given 추가할 역 1개 생성
     * When 기존 노선에 등록된 상행역을 상행역으로 하고
     * When 새로 생성한 역을 하행역으로 하고
     * When 구간 생성 요청을 하면
     * Then 예외를 발생시킨다
     */
    @Test
    @DisplayName("기존 노선의 하행이 신규 구간의 상행과 일치하지 않는경우 실패한다")
    public void addSectionWithInvalidUpStation() {
        // when
        int statusCode = 지하철_구간_등록(이호선Id, 강남역Id, 성수역Id, 3);

        // then
        assertThat(statusCode).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 노선을 1개 생성
     * When 기존의 노선에 등록된 하행역을 상행역으로 하고
     * When 기존 노선에 등록되있는 역중 하나를 하행역으로 하고
     * When 구간 생성 요청을 하면
     * Then 예외를 발생시킨다
     */
    @Test
    @DisplayName("기존 노선에 있는 역은 새 구간의 하행역이 될수없다")
    public void addSectionWithInvalidDownStation() {
        // when
        int statusCode = 지하철_구간_생성_요청(이호선, 강남역);

        // then
        assertThat(statusCode).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 노선을 1개 생성
     * Given 추가할 역 1개 생성
     * Given 구간을 생성
     * When 지하철 구간 제거를 요청하면
     * Then 노선 목록에서 삭제된 구간은 조회할 수 없다
     */
    @Test
    @DisplayName("지하철 노선에 등록된 하행 종점역을 제거할 수 있다")
    public void deleteSection() {
        // given
        지하철_구간_생성_요청(이호선, 성수역);
        SectionDeleteRequest 성수역_삭제_요청 = SectionDeleteRequest.of(이호선Id, 성수역Id);

        // when
        int statusCode = 지하철_구간_삭제(성수역_삭제_요청);

        // then
        ExtractableResponse<Response> response = 지하철_단일_노선_조회(이호선Id);
        List<String> stationNames = response.jsonPath().getList("station.name", String.class);

        assertAll(
                () -> assertThat(statusCode).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(stationNames).doesNotContain("성수역")
        );
    }

    /**
     * Given : 지하철 노선을 1개 생성
     * Given : 추가할 역 1개 생성
     * Given : 구간을 생성
     * When : 하행 종점역이 아닌 지하철 구간 제거를 요청하면
     * Then : 예외를 발생한다.
     */
    @Test
    @DisplayName("하행 종점역이 아닌 역을 삭제요청할 경우 예외를 발생시킨다")
    public void deleteSectionUnsuitableStation() {
        // given
        지하철_구간_생성_요청(이호선, 성수역);
        SectionDeleteRequest 건대입구_삭제_요청 = SectionDeleteRequest.of(이호선Id, 강남역Id);

        // when
        int statusCode = 지하철_구간_삭제(건대입구_삭제_요청);

        // then
        assertThat(statusCode).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    /**
     * Given 지하철 노선을 1개 생성
     * When 노선에 역이 딱 하행역, 상행역 2개만 있는 경우에 삭제를 요청하면
     * Then 예외를 발생한다.
     */
    @Test
    @DisplayName("지하철 노선에 상행, 하행 역 2개만 있는 경우 삭제할수 없다")
    public void deleteWithOnlyOneSection() {
        // given
        SectionDeleteRequest 건대입구_삭제_요청 = SectionDeleteRequest.of(이호선Id, 건대입구역Id);

        // when
        int statusCode = 지하철_구간_삭제(건대입구_삭제_요청);

        // then
        assertThat(statusCode).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public Long getId(ExtractableResponse<Response> response) {
        return response.jsonPath().getLong("id");
    }
}
