package subway.section;

import org.junit.jupiter.api.DisplayName;

@DisplayName("지하철 구간 관련 기능")
public class SectionAcceptanceTest {
    /**
     * Given 지하철역이 4개가 등록되어있다.
     * Given 지하철 노선이 1개가 등록되어있다.
     * When 지하철 노선에 구간을 등록한다
     * Then 지하철 노선 조회 시, 노선의 하행역은 등록한 구간의 하행역이어야한다.
     * Then 지하철 노선의 길이가, 기존 노선의 길이와 등록한 구간의 길이의 합이어야한다.
     */

    /**
     * Given 지하철역이 4개가 등록되어있다.
     * And 지하철 노선이 1개가 등록되어있다.
     * And 지하철 구간이 등록되어 있다.
     * When 지하철 구간을 제거한다.
     * Then 지하철 노선 조회 시, 하행역은 제거한 구간의 상행역이어야한다.
     * Then 지하철 노선의 길이가, 기존 노선의 길이에서 제거한 구간의 길이를 뺀 값이어야한다.
     */
}
