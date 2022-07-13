package nextstep.subway.acceptance.section;

import nextstep.subway.acceptance.util.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SectionAcceptanceTest extends AcceptanceTest {

    /**
     * Given : 지하철 노선을 생성하고
     * When : 구간을 등록하면
     * Then : 노선에서 등록한 구간을 조회할 수 있다.
     */
    @Test
    @DisplayName("구간을 등록한다.")
    void createdSection() {

    }
    /**
     * Given : 지하철 노선을 생성하고
     * When : 구간을 등록한 후
     * When : 등록한 구간을 제거하면
     * Then : 하행 종점역이 변경된다.
     */
    @Test
    @DisplayName("구간을 제거한다.")
    void deletedSection() {

    }

    /**
     * Given : 지하철 노선을 생성하고
     * When : 노선 내부에 있는 역을 하행역으로 등록할 때
     * Then : 등록 실패한다.
     */
    @Test
    @DisplayName("기존 역을 등록하면 구간 등록이 실패된다.")
    void failedCreatedSection() {

    }

    /**
     * Given : 지하철 노선을 생성하고
     * When : 구간을 등록한 후
     * When : 하행 종점역이 아닌 다른 역을 제거하려 하면
     * Then : 제거 실패한다.
     */
    @Test
    @DisplayName("중간 역을 제거하려하면 실패한다.")
    void failedDeletedSection() {

    }
}
