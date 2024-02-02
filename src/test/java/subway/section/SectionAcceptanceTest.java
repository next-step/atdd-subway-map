package subway.section;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.acceptance.AcceptanceTest;

@DisplayName("구간 관련 기능")
@AcceptanceTest
public class SectionAcceptanceTest {

    /**
     Given 지하철 노선이 생성되고,
     When 새로운 구간의 상행역이 해당 노선에 등록되어 있는 하행 종점역이라면,
     Then 새로운 구간이 성공적으로 등록된다
     */
    @Test
    @DisplayName("상행역이 종점역일 때 구간 등록")
    void registerSectionWithTerminalStation() {
    }

    /**
     Given 지하철 노선이 생성되고,
     When 새로운 구간의 상행역이 해당 노선에 등록되어 있는 하행 종점역이 아니라면,
     Then 에러 메시지가 반환된다.
     */
    @Test
    @DisplayName("상행역이 종점역이 아닐 때 구간 등록 에러")
    void registerSectionWithNonTerminalStation() {
    }

    /**
     Given 지하철 노선이 생성되고,
     When 이미 해당 노선에 등록되어 있는 역이 새로운 구간의 하행역이라면,
     Then 에러 메시지가 반환된다.
     */
    @Test
    @DisplayName("하행역이 이미 등록된 역일 때 구간 등록 에러")
    void registerSectionWithExistingStation() {
    }


    /**
     Given 지하철 노선이 생성되고,
     When 이미 해당 노선에 등록되어 있는 역이 새로운 구간의 하행역이 아니라면,
     Then 새로운 구간이 성공적으로 등록된다.
     */
    @Test
    @DisplayName("하행역이 이미 등록된 역이 아닐 때 구간 등록")
    void registerSectionWithNonExistingStation() {
    }

}
