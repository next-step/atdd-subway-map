package subway.common;

import subway.section.entity.Section;
import subway.station.entity.Station;
import subway.util.RandomUtil;

public class TestFixTure {

    public TestFixTure() {
    }

    public static Section createSectionFixTrue() {
        return createSectionFixTrue(RandomUtil.getLandomLong(), RandomUtil.getLandomLong());
    }

    public static Section createSectionFixTrue(Long upStationId, Long downStationId) {
        return Section.builder()
                .id(RandomUtil.getLandomLong())
                .upStation(new Station(upStationId, "역2"))
                .downStation(new Station(downStationId, "역1"))
                .distance(RandomUtil.getLandomLong())
                .build();
    }
}
