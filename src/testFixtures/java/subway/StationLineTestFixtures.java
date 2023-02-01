package subway;

import subway.line.StationLine;

/**
 * StationLineTestFixtures
 *
 * @author JungGyun.Choi
 * @version 1.0.0
 * @since 2023. 02. 01.
 */
public class StationLineTestFixtures {

    public static StationLine fixture(
        String name,
        String color,
        Long upStationId,
        Long downStationId,
        Integer distance
    ) {
        return StationLine.builder()
                .name(name)
                .color(color)
                .upStationId(upStationId)
                .downStationId(downStationId)
                .distance(distance)
                .build();
    }
}
