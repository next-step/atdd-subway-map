package nextstep.subway.line.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import nextstep.subway.station.domain.model.Station;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class SectionsTest {
    private static final Line DUMMY_LINE = new Line();
    private static final Distance DUMMY_DISTANCE = new Distance(100);

    @DisplayName("Section 추가 테스트")
    @CsvSource(value = {
        "1=0역,2=1역,3=2역,4=3역,5=4역,6=5역|1-2,2-3,3-4,4-5,5-6",
        "2=0역,3=3역,4=1역,5=5역,6=2역,7=4역|2-3,3-4,4-5,5-6,6-7",
        "1=0역,12=3역,2=1역,4=5역,5=2역,22=4역|1-12,12-4,4-2,2-5,5-22"
    }, delimiter = '|')
    @ParameterizedTest
    void add(String stationStrIdAndNames, String strLinkIds) {
        Map<Long, Station> stations = newStations(stationStrIdAndNames);
        List<LinkData> linkDatas = newLinkDatas(strLinkIds);
        Sections sections = newSections(stations, linkDatas);

        int expertSize = stations.size() - 1;
        assertThat(sections.size())
            .isEqualTo(expertSize);
    }

    @DisplayName("Section 추가 실패 테스트")
    @CsvSource(value = {
        "1=0역,2=1역,3=2역,4=3역|1-2,2-3,4-2", // 새로운 구간의 상행역은 해당 노선에 등록되어있는 하행 종점역이어야 한다.
        "1=0역,2=1역,3=2역,4=3역|1-2,2-1" // 새로운 구간의 하행역은 해당 노선에 등록되어있는 역일 수 없다.
    }, delimiter = '|')
    @ParameterizedTest
    void addThatFailing(String stationStrIdAndNames, String strLinkIds) {
        assertThatThrownBy(() -> add(stationStrIdAndNames, strLinkIds))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("Section 삭제 테스트")
    @CsvSource(value = {
        "1=0역,2=1역,3=2역,4=3역,5=4역,6=5역|1-2,2-3,3-4,4-5,5-6|5,4,3",
        "2=0역,3=3역,4=1역,5=5역,6=2역,7=4역|2-3,3-4,4-5,5-6,6-7|5,4,3",
        "1=0역,12=3역,2=1역,4=5역,5=2역,22=4역|1-12,12-4,4-2,2-5,5-22|5,4,3"
    }, delimiter = '|')
    @ParameterizedTest
    void delete(String stationStrIdAndNames, String strLinkDatas, String strRemoveIds) {
        List<Long> removeIds = Arrays.stream(strRemoveIds.split(","))
                                     .map(Long::parseLong)
                                     .collect(Collectors.toList());
        Map<Long, Station> stations = newStations(stationStrIdAndNames);
        List<LinkData> linkDatas = newLinkDatas(strLinkDatas);
        Sections sections = newSections(stations, linkDatas);

        int expertSectionSize = stations.size() - removeIds.size() - 1;
        removeIds.forEach(sections::delete);
        assertThat(sections.size())
            .isEqualTo(expertSectionSize);
    }

    @DisplayName("Section 삭제 실패 테스트")
    @CsvSource(value = {
        "1=0역,2=1역,3=2역,4=3역,5=4역,6=5역|1-2,2-3,3-4,4-5,5-6|4", // 종점역 삭제가 아닐 경우
        "1=0역,2=1역,3=2역|1-2,2-3|1,2", // 1개만 있을 경우 삭제 X
        "1=0역,2=1역|1-2|1", // 1개만 있을 경우 삭제 X
    }, delimiter = '|')
    @ParameterizedTest
    void deleteThatFailing(String stationStrIdAndNames, String strLinkIds, String strRemoveIds) {
        assertThatThrownBy(() -> delete(stationStrIdAndNames, strLinkIds, strRemoveIds))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("toStations이 올바른 순서로 지하철역을 반환 하는지 테스트")
    @CsvSource(value = {
        "1=0역,2=1역,3=2역,4=3역,5=4역,6=5역|1-2,2-3,3-4,4-5,5-6|0역,1역,2역,3역,4역,5역",
        "2=0역,3=3역,4=1역,5=5역,6=2역,7=4역|2-3,3-4,4-5,5-6,6-7|0역,3역,1역,5역,2역,4역",
        "1=0역,12=3역,2=1역,4=5역,5=2역,22=4역|1-12,12-4,4-2,2-5,5-22|0역,3역,5역,1역,2역,4역"
    }, delimiter = '|')
    @ParameterizedTest
    void toStations(String stationStrIdAndNames, String strLinkIds, String expertNames) {
        Map<Long, Station> stations = newStations(stationStrIdAndNames);
        List<LinkData> linkDatas = newLinkDatas(strLinkIds);
        Sections sections = newSections(stations, linkDatas);

        String actualStationNames = sections.toStations()
                                            .stream()
                                            .map(Station::getName)
                                            .collect(Collectors.joining(","));

        assertThat(actualStationNames)
            .withFailMessage(String.format(
                "결과값이 다름 : %s / %s",
                expertNames, actualStationNames
            ))
            .isEqualTo(expertNames);
    }

    private Map<Long, Station> newStations(String strIdAndNames) {
        return Arrays.stream(strIdAndNames.split(","))
                     .map(eachStrIdAndName -> {
                              String[] splitted = eachStrIdAndName.split("=");
                              return new Station(
                                  Long.parseLong(splitted[0]), splitted[1]
                              );
                          }
                     )
                     .collect(Collectors.toMap(
                         Station::getId,
                         eachStation -> eachStation
                     ));
    }

    private List<LinkData> newLinkDatas(String strLinkIds) {
        return Arrays.stream(strLinkIds.split(","))
                     .map(eachStrLinkData -> new LinkData(eachStrLinkData.split("-")))
                     .collect(Collectors.toList());
    }

    private Sections newSections(Map<Long, Station> idEachStation, List<LinkData> linkDatas) {
        long sectionIdCounter = 0;
        Sections sections = new Sections();
        for (LinkData eachLinkData : linkDatas) {
            Section section = Section.builder()
                .id(++sectionIdCounter)
                .upStation(
                    idEachStation.get(eachLinkData.getUpStationId())
                )
                .downStation(
                    idEachStation.get(eachLinkData.getDownStationId())
                )
                .line(DUMMY_LINE)
                .distance(DUMMY_DISTANCE)
                .build();
            sections.add(section);
        }
        return sections;
    }

    private static class LinkData {
        private String[] linkData;

        public LinkData(String[] linkData) {
            this.linkData = linkData;
        }

        public Long getUpStationId() {
            return Long.parseLong(linkData[0]);
        }

        public Long getDownStationId() {
            return Long.parseLong(linkData[1]);
        }
    }
}