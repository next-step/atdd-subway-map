package subway.line;

import fixture.LineFixture;
import fixture.SectionFixture;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import support.annotation.AcceptanceTest;
import static org.assertj.core.api.Assertions.assertThat;

// 윤태한님, 이주오님

@DisplayName("지하철 구간 관련 기능")
@AcceptanceTest
class SectionAcceptanceTest {


    private SectionFixture 강남역_선릉역_구간_Fixture;
    private LineFixture 이호선_Fixture;

    @BeforeEach
    void setupFixture() {
        이호선_Fixture = LineFixture.이호선();
        강남역_선릉역_구간_Fixture = SectionFixture.강남_선릉_구간();
    }

    /*
    Given 지하철 노선이 있을 때
    When 구간을 노선에 등록하면
    Then 구간이 노선에 등록되어야 한다.
    Then 노선의 하행역이 구한의 하행역으로 바뀌어야 한다.
     */
    @DisplayName("지하철 구간 등록을 성공한다.")
    @Test
    void createSection() {
        // given
        var 이호선 = LineFixture.save(이호선_Fixture).as(LineResponse.class);

        // when
        ExtractableResponse<Response> response = SectionFixture.save(이호선.getId(), 강남역_선릉역_구간_Fixture);// then
        var 강남_선릉_구간 = response.as(SectionResponse.class);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        이호선 = LineFixture.get(이호선.getId()).as(LineResponse.class);
        assertThat(이호선.getStations().get(이호선.getStations().size()-1).getId()).isEqualTo(강남_선릉_구간.getDownStationId());
    }

    /*
    Given 지하철 노선이 있을 때
    When 상행역이 노선의 하행역과 다른 구간을 노선에 등록하면
    Then 구간이 노선에 등록되지 않고 InvalidUpstationException 발생한다.
     */
    @DisplayName("지하철 구간의 상행역이 노선의 하행역이 아닌경우 등록에 실패한다.")
    @Test
    void createSectionExceptionWithInvalidUpsation() {
        // given

        // when

        // then

    }


    /*
    Given 지하철 노선이 있을 때
    When 노선에 이미 구간의 하행역이 있는 경우, 구간을 노선에 등록하면
    Then 구간이 노선에 등록되지 않고 AlreadyExistStationException 발생한다.
     */
    @DisplayName("지하철 구간 하행역이 노선에 등록되어 있는 경우 등록에 실패한다.")
    @Test
    void createSectionExceptionWithAlreadyExistsStation() {
        // given

        // when

        // then

    }

}
