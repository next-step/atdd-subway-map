package subway;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import subway.line.LineCreateRequest;
import subway.line.LineResponse;
import subway.line.LineUpdateRequest;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("지하철 노선 관리 기능")
public class LineAcceptanceTest extends AcceptanceTest{

    Long firstStationId;
    Long secondStationId;
    Long thirdStationId;

    LineCreateRequest lineCreateRequest;

    @BeforeEach
    public void setup() {
        firstStationId = StationRestAssuredTest.createStation("지하철역1");
        secondStationId = StationRestAssuredTest.createStation("지하철역2");
        thirdStationId = StationRestAssuredTest.createStation("지하철역3");
        lineCreateRequest = new LineCreateRequest("신분당선", "bg-red-600", firstStationId, secondStationId, 10L);
    }

    /**
     * When 지하철 노선을 생성하면
     * Then 지하철 노선 목록 조회 시 생성한 노선을 찾을 수 있다
     */
    @DisplayName("지하철 노선을 등록하고 생성한 노선을 찾을 수 있다..")
    @Test
    public void createLineTest() {

        // When
        LineRestAssuredTest.createLine(lineCreateRequest);

        // Then
        var lineResponseList = LineRestAssuredTest.getLineResponseList();
        List<String> nameList = lineResponseList.stream().map(LineResponse::getName).collect(Collectors.toList());

        assertThat(lineResponseList).hasSize(1);
        assertThat(nameList).containsExactly(lineCreateRequest.getName());
    }

    /**
     * Given 2개의 지하철 노선을 생성하고
     * When 지하철 노선 목록을 조회하면
     * Then 지하철 노선 목록 조회 시 2개의 노선을 조회할 수 있다.
     */
    @DisplayName("2개의 지하철 노선을 생성하고 목록을 조회하면 지하철 목록 조회시 2개의 노선을 조회할 수 있다.")
    @Test
    void getLineList() {

        // When
        LineCreateRequest secondLineCreateRequest = new LineCreateRequest("분당선", "bg-red-600", firstStationId, thirdStationId, 10L);
        LineRestAssuredTest.createLine(lineCreateRequest);
        LineRestAssuredTest.createLine(secondLineCreateRequest);

        // Then
        var lineResponseList = LineRestAssuredTest.getLineResponseList();
        List<String> nameList = lineResponseList.stream().map(LineResponse::getName).collect(Collectors.toList());

        assertThat(lineResponseList).hasSize(2);
        assertThat(nameList).containsAnyOf(lineCreateRequest.getName());
        assertThat(nameList).containsAnyOf(secondLineCreateRequest.getName());
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 조회하면
     * Then 생성한 지하철 노선의 정보를 응답받을 수 있다.
     */
    @DisplayName("지하철 노선을 생성하고 해당 지하철 노선을 조회하면 지하철 노선 정보를 조회할 수 있다.")
    @Test
    public void getLineTest() {
        Long lineId = LineRestAssuredTest.createLine(lineCreateRequest);
        var line = LineRestAssuredTest.getLine(lineId);

        LineRestAssuredTest.checkLine(lineCreateRequest, lineId, line, firstStationId, secondStationId);
    }

    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 수정하면
     * Then 해당 지하철 노선 정보는 수정된다
     */
    @DisplayName("지하철 노선을 생성하고 해당 지하철 노선을 수정하면 수정된다.")
    @Test
    public void updateLineTest() {
        Long id = LineRestAssuredTest.createLine(lineCreateRequest);
        LineUpdateRequest lineUpdateRequest = new LineUpdateRequest("다른 분당선", "bg-red-600");
        LineRestAssuredTest.updateLine(id, lineUpdateRequest);
        LineResponse line = LineRestAssuredTest.getLine(id);

        assertThat(line.getId()).isEqualTo(id);
        assertThat(line.getName()).isEqualTo(lineUpdateRequest.getName());
    }


    /**
     * Given 지하철 노선을 생성하고
     * When 생성한 지하철 노선을 삭제하면
     * Then 해당 지하철 노선 정보는 삭제된다
     */
    @DisplayName("지하철 노선을 생성하고 해당 노선을 삭제하면 지하철 노선은 삭제된다.")
    @Test
    public void deleteLineTest() {
        Long lineId = LineRestAssuredTest.createLine(lineCreateRequest);
        LineRestAssuredTest.deleteLine(lineId);

        assertThrows(AssertionFailedError.class, (() -> LineRestAssuredTest.getLine(lineId)));
    }



}
