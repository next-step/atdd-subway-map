package subway.fixtures;

import subway.domain.entity.Line;
import subway.domain.entity.Station;

public class StationFixture {
    static Station yeoksam() {
        return new Station("역삼역");
    }

    static Station jamsil() {
        return new Station("잠실역");
    }
}
