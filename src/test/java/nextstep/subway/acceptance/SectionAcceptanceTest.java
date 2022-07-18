package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import nextstep.subway.applicaion.dto.StationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.validate.HttpStatusValidate.*;
import static nextstep.subway.api.StationApi.*;
import static nextstep.subway.api.StationLineApi.*;
import static nextstep.subway.api.SectionApi.*;
import static nextstep.subway.validate.HttpStatusValidate.*;

@DisplayName("지하철 관리")
public class SectionAcceptanceTest extends AcceptionceTest{

    @BeforeEach
    public void createMockData() {
        지하철역_생성("강남역");
        지하철역_생성("역삼역");
        지하철역_생성("선릉역");
    }

    /**
     * when 지하철 노선 생성
     * when 지하철 노선 추가
     * then 지하철 구간 등록후, 하행선 검증.
     *
     */
    @DisplayName("구간 등록 기능 테스트")
    @Test
    void createSectionTest() {
        지하철_노선_생성("2호선", "bg-green-600", 1L, 2L , 6);

        ExtractableResponse<Response> 지하철구간_생성 = 지하철_구간_생성(1L, 2L, 3L, 4);
        상태코드_체크(지하철구간_생성, HttpStatus.CREATED);

        ExtractableResponse<Response> response = 지하철_노선_조회(1L);
        하행선값_검증(response, "선릉역");
    }

    @DisplayName("새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.")
    @Test
    void exception_notEqualsAddDownStationId() {
        지하철_노선_생성("2호선", "bg-green-600", 1L, 2L , 6);

        ExtractableResponse<Response> exceptionResponse = 지하철_구간_생성(1L, 1L, 2L, 4);

        상태코드_체크(exceptionResponse, HttpStatus.CONFLICT);
        에러메시지_체크(exceptionResponse, "하행선만 등록 가능합니다.");
    }

    @DisplayName("새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.")
    @Test
    void exception_isExstsStation() {
        지하철_노선_생성("2호선", "bg-green-600", 1L, 2L , 6);

        ExtractableResponse<Response> exceptionResponse = 지하철_구간_생성(1L, 2L, 1L, 4);

        상태코드_체크(exceptionResponse, HttpStatus.CONFLICT);
        에러메시지_체크(exceptionResponse, "이미 등록된 역입니다.");
    }


    /**
     * when 지하철 노선 추가
     * when 지하철 구간 삭제
     * then 지하철 조회후, 하생선 체크
     *
     */
    @DisplayName("구간 제거 기능 테스트")
    @Test
    void deleteSectionTest() {
        지하철_노선_생성("2호선", "bg-green-600", 1L, 2L , 6);

        ExtractableResponse<Response> createSection = 지하철_구간_생성(1L, 2L, 3L, 4);
        상태코드_체크(createSection, HttpStatus.CREATED);

        ExtractableResponse<Response> deleteSection = 지하철_구간_삭제(1L, 3L);
        상태코드_체크(deleteSection, HttpStatus.NO_CONTENT);

        ExtractableResponse<Response> response = 지하철_노선_조회(1L);
        하행선값_검증(response, "역삼역");
    }

    @DisplayName("마지막 구간만 제거할 수 있다.")
    @Test
    void exception_onlyDeleteDownSection() {
        지하철_노선_생성("2호선", "bg-green-600", 1L, 2L , 6);

        ExtractableResponse<Response> createSection = 지하철_구간_생성(1L, 2L, 3L, 4);
        상태코드_체크(createSection, HttpStatus.CREATED);

        ExtractableResponse<Response> exceptionResponse = 지하철_구간_삭제(1L, 2L);

        상태코드_체크(exceptionResponse, HttpStatus.CONFLICT);
        에러메시지_체크(exceptionResponse, "하행선만 삭제 가능합니다.");
    }

    @DisplayName("(구간이 1개인 경우) 역을 삭제할 수 없다.")
    @Test
    void exception_notDeleteSectionOne() {
        지하철_노선_생성("2호선", "bg-green-600", 1L, 2L , 6);

        ExtractableResponse<Response> exceptionResponse = 지하철_구간_삭제(1L, 2L);

        상태코드_체크(exceptionResponse, HttpStatus.CONFLICT);
        에러메시지_체크(exceptionResponse, "구간이 1개여서 삭제할 수 없습니다.");
    }

    private void 하행선값_검증(ExtractableResponse<Response> response, String name) {
        List<StationResponse> stationList = response.jsonPath().getList("stations", StationResponse.class);
        String downStationName = stationList.get(stationList.size()-1).getName();

        동일한_값_인지_검증(downStationName, name);
    }

}
