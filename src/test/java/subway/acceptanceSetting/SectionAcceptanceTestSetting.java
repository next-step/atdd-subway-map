package subway.acceptanceSetting;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import subway.dto.LineRequestTestDto;
import subway.restAssured.LineRestAssured;
import subway.restAssured.SectionRestAssured;
import subway.restAssured.StationRestAssured;

@ActiveProfiles("acceptance")
@Transactional(readOnly = true)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SectionAcceptanceTestSetting {

    protected static final String SHINBUNDANG_LINE = "신분당선";
    protected static final String RED = "bg-red-600";
    protected static final long DISTANCE_SEVEN = 7L;
    protected static final long DISTANCE_THREE = 3L;
    protected static final long DISTANCE_FIVE = 5L;

    @LocalServerPort
    int port;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    private final StationRestAssured stationRestAssured = new StationRestAssured();
    protected final LineRestAssured lineRestAssured = new LineRestAssured();
    protected final SectionRestAssured sectionRestAssured = new SectionRestAssured();

    protected Long firstStationId;
    protected Long secondStationId;
    protected Long thirdStationId;
    protected Long fourthStationId;
    protected Long fifthStationId;
    protected Long firstLineId;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        databaseCleanUp.execute();

        firstStationId = stationRestAssured.save("강남역");
        secondStationId = stationRestAssured.save("송파역");
        thirdStationId = stationRestAssured.save("한남역");
        fourthStationId = stationRestAssured.save("서울역");
        fifthStationId = stationRestAssured.save("판교역");

        firstLineId = lineRestAssured.save(테스트용_지하철_노선_데이터_생성());
    }

    private LineRequestTestDto 테스트용_지하철_노선_데이터_생성() {
        return LineRequestTestDto.builder()
                .name(SHINBUNDANG_LINE)
                .color(RED)
                .upStationId(fifthStationId)
                .downStationId(firstStationId)
                .distance(DISTANCE_SEVEN)
                .build();
    }
}
