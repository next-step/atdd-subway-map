package nextstep.subway.domain;

import java.util.Arrays;

public class SectionFixData {
    private static int DEFAULT_DISTANCE = 5;

    public static Line createLine() {
        return new Line("2호선", "bg-green-700");
    }

    public static Station createStation(String name) {
        return new Station(name);
    }

    public static Section createSection(Station upStation, Station downStation) {
        return new Section(createLine(), upStation, downStation, DEFAULT_DISTANCE);
    }

    public static Sections createSections(Section ...sections) {
        return new Sections(Arrays.asList(sections));
    }
}
