package subway;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import subway.test.AcceptanceTestBase;
import subway.test.StationRequestBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static subway.test.Constants.GANGNAM_STATION;
import static subway.test.Constants.SEOUL_STATION;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTestBase {
    StationRequestBuilder.Builder defaultBuilder;

    @BeforeEach
    void beforeEach() {
        this.defaultBuilder = new StationRequestBuilder.Builder()
                .name(GANGNAM_STATION);
    }

    /**
     * When 지하철역을 생성하면
     * Then 지하철역이 생성된다
     * Then 지하철역 목록 조회 시 생성한 역을 찾을 수 있다
     */
    @DisplayName("지하철역을 생성한다")
    @Test
    void createStation() {
        // when
        var createResponse = defaultBuilder.name(GANGNAM_STATION).build().requestCreate();
        // then
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED.value());

        // then
        var getAllResponse = StationRequestBuilder.requestGetAll();
        assertThat(getAllResponse.extractNames()).containsAnyOf(GANGNAM_STATION);
    }

    /**
     * Given 2개의 지하철역을 생성하고
     * When 지하철역 목록을 조회하면
     * Then 2개의 지하철역을 응답 받는다
     */
    @DisplayName("지하철역 목록을 조회한다")
    @Test
    void getAllStation() {
        //given
        this.defaultBuilder.name(GANGNAM_STATION).build().requestCreate();
        this.defaultBuilder.name(SEOUL_STATION).build().requestCreate();

        //when
        var getAllResponse = StationRequestBuilder.requestGetAll();

        //then
        assertThat(getAllResponse.getStatusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(getAllResponse.extractIds().size()).isEqualTo(2);
    }

    /**
     * Given 지하철역을 생성하고
     * When 그 지하철역을 삭제하면
     * Then 그 지하철역 목록 조회 시 생성한 역을 찾을 수 없다
     */
    @DisplayName("지하철역을 제거한다")
    @Test
    void deleteStation() {
        //given
        var createdResponse = this.defaultBuilder.build().requestCreate();
        var createdId = createdResponse.extractId();

        //when
        StationRequestBuilder.requestDelete(createdId);

        //then
        var getAllResponse = StationRequestBuilder.requestGetAll();
        assertThat(getAllResponse.extractIds()).doesNotContain(createdId);
    }
}
