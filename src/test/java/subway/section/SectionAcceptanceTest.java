package subway.section;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import subway.util.DatabaseCleanup;

@Profile("acceptance")
@DisplayName("구간 관련 기능")
@TestConstructor(autowireMode = AutowireMode.ALL)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SectionAcceptanceTest {

    @LocalServerPort
    private int port;

    private final DatabaseCleanup databaseCleanup;

    public SectionAcceptanceTest(final DatabaseCleanup databaseCleanup) {
        this.databaseCleanup = databaseCleanup;
    }

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        databaseCleanup.truncateTable();
    }

    @DisplayName("구간 등록 관련 기능")
    @Nested
    private class RegisterSectionTest {

        /**
         * When 지하철 노선에 새로운 구간을 등록하면
         * Then 새로운 구간은 노선에 등록되어있는 하행 종점역이어야 한다.
         * And 새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.
         */
        @DisplayName("노선에 새로운 구간을 등록한다.")
        @Test
        void registerSection() {

        }

        /**
         * When 해당 노선에 등록되어 있는 구간일 경우
         * Then 에러 처리한다.
         */
        @DisplayName("노선에 새로운 구간 등록했을 때 해당 노선에 등록되어 있는 구간일 경우 경우 에러 처리한다.")
        @Test
        void registerSection_Error_AlreadyRegisteredSection() {

        }

        /**
         * When 노선에 구간을 등록했을 때 구간의 하행역이 해당 노선에 등록되어 있는 역일 경우
         * Then 에러 처리한다.
         */
        @DisplayName("노선에 새로운 구간 등록시 새로운 구간의 하행역이 해당 노선에 등록되어 있는 역일 경우 에러 처리한다.")
        @Test
        void registerSection_Error_SectionDownStationAlreadyRegisteredSection() {

        }
    }

    @DisplayName("구간 제거 관련 기능")
    @Nested
    private class RemoveSectionTest {

        /**
         * When 노선의 구간을 제거하면
         * Then 노선의 하행 종점역은 제거한 구간의 상행역이어야 한다.
         */
        @DisplayName("노선의 구간을 제거한다.")
        @Test
        void removeSection() {

        }

        /**
         * When 제거할 구간이 노선의 하행 종점역이 아닌 경우
         * Then 에러 처리한다.
         */
        @DisplayName("제거할 구간이 노선의 하행 종점역이 아닌 경우 에러 처리한다.")
        @Test
        void removeSection_Error_RemoveSectionIsNotDownStation() {

        }

        /**
         * When 구간 제거시 노선에 상행 종점역과 하행 종점역만 있는 경우
         * Then 에러 처리한다.
         */
        @DisplayName("구간 제거시 노선에 상행 종점역과 하행 종점역만 있는 경우 에러 처리한다.")
        @Test
        void removeSection_Error_LineOnlyHasUpStationAndDownStation() {

        }
    }
}
