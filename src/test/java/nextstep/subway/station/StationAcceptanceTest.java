package nextstep.subway.station;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import nextstep.subway.station.dto.StationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철역을 생성한다.")
    @Test
    void 지하철역_생성_및_확인() {
        // when
        ExtractableResponse<Response> response = StationHelper.지하철역_생성_요청("강남역");

        // then
        StationHelper.응답_201_확인(response);
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    @Test
    void 지하철역_생성시_중복안되게_처리() {
        // given
        StationHelper.지하철역_생성_요청("강남역");

        // when
        ExtractableResponse<Response> response2 = StationHelper.지하철역_생성_요청("강남역");

        // then
        assertThat(response2.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }



    @DisplayName("지하철역을 조회한다.")
    @Test
    void 지하철역_목록_조회() {
        /// given
        ExtractableResponse<Response> createResponse1 =
                StationHelper.지하철역_생성_요청("강남역");

        ExtractableResponse<Response> createResponse2 =
                StationHelper.지하철역_생성_요청("역삼역");

        // when
        ExtractableResponse<Response> response = StationHelper.지하철역_목록_조회_결과_요청();

        // then
        StationHelper.응답_200_확인(response);
        List<Long> expectedLineIds = StationHelper.지하철역_목록_예상_아이디_리스트(createResponse1, createResponse2);
        List<Long> resultLineIds = StationHelper.지하철_노선_목록_결과_아이디_리스트(response);
        assertThat(resultLineIds).containsAll(expectedLineIds);
    }



    @DisplayName("지하철역을 제거한다.")
    @Test
    void 지하철역_삭제() {
        // given
        ExtractableResponse<Response> createResponse =
                StationHelper.지하철역_생성_요청("강남역");

        // when
        String uri = createResponse.header("Location");
        ExtractableResponse<Response> response = StationHelper.지하철역_삭제_요청(uri);

        // then
        StationHelper.응답_204_확인(response);
    }


}
