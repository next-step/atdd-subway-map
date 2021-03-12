package nextstep.subway.station;

import com.sun.tools.javac.util.List;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Map;

import static nextstep.subway.utils.StationTestUtils.*;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {
    private static final String GANGNAM_STATION = "강남역";
    private static final String YEOKSAM_STATION = "역삼역";

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // given
        Map<String, String> 강남역 = 역_파라미터_설정(GANGNAM_STATION);

        // when
        ExtractableResponse<Response> 역생성응답 = 역_생성_요청(강남역);

        // then
        응답_상태코드_확인(역생성응답, HttpStatus.CREATED);
        응답_헤더_로케이션_값_있음(역생성응답);
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    @Test
    void createStationWithDuplicateName() {
        // given
        Map<String, String> 강남역 = 역_파라미터_설정(GANGNAM_STATION);

        역_생성_요청(강남역);

        // when
        ExtractableResponse<Response> 역중복생성응답 = 역_생성_요청(강남역);

        // then
        응답_상태코드_확인(역중복생성응답, HttpStatus.BAD_REQUEST);
    }

    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        /// given
        Map<String, String> 강남역 = 역_파라미터_설정(GANGNAM_STATION);
        ExtractableResponse<Response> 강남역생성응답 = 역_생성_요청(강남역);

        Map<String, String> 역삼역 = 역_파라미터_설정(YEOKSAM_STATION);
        ExtractableResponse<Response> 역삼역생성응답 = 역_생성_요청(역삼역);

        // when
        ExtractableResponse<Response> 역목록조회응답 = 역_목록_조회_요청();

        // then
        응답_상태코드_확인(역목록조회응답, HttpStatus.OK);
        지하철_역_목록_포함됨(역목록조회응답, List.of(강남역생성응답, 역삼역생성응답));
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        Map<String, String> 강남역 = 역_파라미터_설정(GANGNAM_STATION);
        ExtractableResponse<Response> 강남역생성응답 = 역_생성_요청(강남역);

        // when
        ExtractableResponse<Response> 역삭제응답 = 역_제거_요청(강남역생성응답);

        // then
        응답_상태코드_확인(역삭제응답, HttpStatus.NO_CONTENT);
    }
}
