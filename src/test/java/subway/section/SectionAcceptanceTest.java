package subway.section;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SectionAcceptanceTest {

    /**
     * Given : 지하철 노선을 등록하고
     * When : 해당 노선에 새로운 구간을 추가하면
     *   - 새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이다.
     *   - 새로운 구간의 하행역은 해당 노선에 등록되어있지 않은 역이다.
     * Then : 해당 노선에 새로운 구간이 추가된다
     */
    @DisplayName("지하철 구간 등록 - 정상 케이스")
    @Test
    public void createSection() {
    }

    /**
     * Given : 지하철 노선을 등록하고
     * When : 해당 노선에 새로운 구간을 추가하면
     *   - 새로운 구간의 상행역은 해당 노선에 등록된 하행 종점역이 아니다.
     * Then : 새로운 구간이 추가되지 않는다.
     */
    @DisplayName("지하철 구간 등록 - 예외 케이스 : 새로운 구간의 상행역이 해당 노선에 등록되어있는 하행 종점역이 아닌 경우")
    @Test
    public void createStation_InvalidCase1() {

    }

    /**
     * Given : 지하철 노선을 등록하고
     * When : 해당 노선에 새로운 구간을 추가하면
     *   - 새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.
     *   - 새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.
     * Then : 해당 노선에 새로운 구간이 추가된다
     *   - 새로운 구간 등록시 위 조건에 부합하지 않는 경우 에러 처리한다.
     */
    @DisplayName("지하철 구간 등록 - 예외 케이스 : 새로운 구간의 하행역이 해당 노선에 등록되어있는 역인 경우")
    @Test
    public void createStation_InvalidCase2() {

    }

    /**
     * Given : 구간이 두 개 이상인 지하철 노선을 등록하고
     * When : 하행 종점역을 제거하면
     * Then : 해당 종점역을 포함한 구간이 제거된다
     */
    @DisplayName("지하철 구간 제거 - 정상 케이스")
    @Test
    public void deleteStation() {
    }

    /**
     * Given : 구간이 한 개인 지하철 노선을 등록하고
     * When : 하행 종점역을 제거하면
     * Then : 해당 구간이 제거되지 않는다.
     */
    @DisplayName("지하철 구간 제거 - 예외 케이스 : 구간이 한 개인 노선의 하행 종점역을 제거하려는 경우")
    @Test
    public void deleteStation_InvalidCase1() {
    }

    /**
     * Given : 구간이 두 개 이상인 지하철 노선을 등록하고
     * When : 하행 종점역이 아닌 역을 제거하면
     * Then : 해당 구간이 제거되지 않는다.
     */
    @DisplayName("지하철 구간 제거 - 예외 케이스 : 하행 종점역이 아닌 역을 제거하려는 경우")
    @Test
    public void deleteStation_InvalidCase2() {
    }


}
