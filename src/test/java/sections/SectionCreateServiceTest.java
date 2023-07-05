package sections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import line.LineFixture;
import subway.SchemaInitSql;
import subway.StationFixture;
import subway.SubwayApplication;
import subway.section.service.SectionCreateService;

@SchemaInitSql
@SpringBootTest(classes = SubwayApplication.class, webEnvironment = WebEnvironment.DEFINED_PORT)
@DisplayName("SectionCreateService 클래스")
public class SectionCreateServiceTest {

    @Autowired
    private SectionCreateService sectionCreateService;

    StationFixture stationFixture = new StationFixture();
    LineFixture lineFixture = new LineFixture();

    @Test
    public void create() {
//        StationResponse createdUpStation = stationFixture.지하철역_생성("A");
//        StationResponse createdDownStation = stationFixture.지하철역_생성("B");
//
//        LineResponse lineResponse = lineFixture.노선생성("테스트 노선", "yellow", createdUpStation.getId(), createdDownStation.getId(), 10);
//
//        SectionCreateRequest request = new SectionCreateRequest();
//        request.setDistance(1);
//        request.setUpStationId(createdUpStation.getId());
//        request.setDownStationId(createdDownStation.getId());
//
//        Section section = sectionCreateService.create(lineResponse.getId(), request);
//
//        Assertions.assertThat(section).isNotNull();
    }

}
