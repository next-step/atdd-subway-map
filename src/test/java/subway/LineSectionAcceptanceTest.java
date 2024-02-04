package subway;


import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import subway.common.Line;
import subway.common.Section;
import subway.common.Station;
import subway.interfaces.line.dto.LineResponse;
import subway.interfaces.station.dto.StationResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@DisplayName("지하철 노선 구간 관련 기능")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
public class LineSectionAcceptanceTest {

    StationResponse A역;
    StationResponse B역;
    StationResponse C역;
    StationResponse D역;
    StationResponse E역;
    LineResponse A역_B역_노선;
    Section.RequestBody B역_C역_구간;
    Section.RequestBody C역_D역_구간;
    Section.RequestBody D역_B역_구간;
    Section.RequestBody B역_A역_구간;

    @BeforeEach
    void setUpFixture() {
        A역 = Station.랜덤역생성();
        B역 = Station.랜덤역생성();
        C역 = Station.랜덤역생성();
        D역 = Station.랜덤역생성();
        E역 = Station.랜덤역생성();

        A역_B역_노선 = Line.노선생성(A역.getId(), B역.getId());

        B역_C역_구간 = Section.REQUEST_BODY(B역.getId(), C역.getId());
        C역_D역_구간 = Section.REQUEST_BODY(C역.getId(), D역.getId());
        D역_B역_구간 = Section.REQUEST_BODY(D역.getId(), B역.getId());
        B역_A역_구간 = Section.REQUEST_BODY(B역.getId(), A역.getId());
    }

    /**
     * When  A역-B역 지하철 노선에 B역-C역 구간을 등록하면
     * Then B역-C역 구간이 등록 된다 (201)
     * Then A역-B역 노선의 하행역이 C역으로 변경되어야 한다
     */
    @DisplayName("지하철 노선에 구간을 등록한다.")
    @Test
    void enrollSectionToLine() {
        // When
        ExtractableResponse<Response> response = Section.Api.createBy(A역_B역_노선.getId(), B역_C역_구간);

        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        //Then
        A역_B역_노선 = Line.Api.retrieveLineBy(A역_B역_노선.getId()).as(LineResponse.class);
        assertThat(A역_B역_노선.getDownStation().getId()).isEqualTo(C역.getId());
        assertThat(A역_B역_노선.getDownStation().getName()).isEqualTo(C역.getName());
    }

    /**
     * When A역-B역 노선에 B역-A역 구간을 등록하면
     * Then 구간등록에 실패한다 (409)
     * Then 실패 메시지는 "이미 포함된 하행역" 이어야 한다.
     */
    @DisplayName("구간의 하행역이 이미 노선에 포함되어있는 경우 등록을 실패한다.")
    @Test
    void failEnrollSectionToLineWithAlreadyExist() {
        // When
        ExtractableResponse<Response> response = Section.Api.createBy(A역_B역_노선.getId(), B역_A역_구간);

        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CONFLICT.value());

        // Then
        String failMessage = response.jsonPath().getString("message");
        assertThat(failMessage).isEqualTo("이미 포함된 하행역");
    }

    /**
     * When A역-B역 노선에 C역-D역 구간을 등록하면
     * Then 구간 등록에 실패한다 (400)
     * Then 실패 메시지는 "잘못된 상행역" 이어야 한다.
     */
    @DisplayName("새로운 구간의 상행역과 기존 노선의 하행역이 같지 않으면 등록을 실패한다.")
    @Test
    void failEnrollSectionToLineWithInvalidUpStation() {
        // When
        ExtractableResponse<Response> response = Section.Api.createBy(A역_B역_노선.getId(), C역_D역_구간);

        // Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());

        // Then
        String failMessage = response.jsonPath().getString("message");
        assertThat(failMessage).isEqualTo("잘못된 상행역");
    }

    /**
     * Given A역-B역 노선에 B역-C역 구간을 등록하고
     * When 하행역이 C역인 구간을 삭제 요청하면
     * Then B역-C역 구간이 삭제된다 (204)
     * Then A역-B역 노선의 새로운 하행역은 B역이 되어야 한다
     */
    @DisplayName("지하철 노선 구간을 삭제한다.")
    @Test
    void deleteLineSection() {

    }

    /**
     * Given A역-B역 노선에 B역-C역 구간을 등록하고
     * When 하행역이 B역인 구간을 삭제 요청하면
     * Then 구간 삭제를 실패한다 (400)
     * Then 실패 메시지는 "잘못된 하행역" 이어야 한다.
     */
    @DisplayName("삭제할 구간이 노선의 마지막 구간이 아닌 경우 삭제를 실패한다.")
    @Test
    void failDeleteLineSectionWithInvalidDownStation() {

    }

    /**
     * When A역-B역 노선에서 하행역이 B역인 구간을 삭제 요청하면
     * Then 구간 삭제를 실패한다 (400)
     * Then 실패 메시지는 "유일한 구간" 이어야 한다.
     */
    @DisplayName("삭제할 구간이 노선의 유일한 구간인 경우 삭제를 실패한다.")
    @Test
    void failDeleteLineSectionWithLastSection() {

    }
}
