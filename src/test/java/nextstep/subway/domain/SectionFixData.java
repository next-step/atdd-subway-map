package nextstep.subway.domain;

import java.util.ArrayList;
import java.util.Arrays;

public class SectionFixData {
    private static int DEFAULT_DISTANCE = 5;

    public static String FIRST_STATION_NAME = "강남역";
    public static String SECOND_STATION_NAME = "양재역";
    public static String THIRD_STATION_NAME = "양재시민의숲";
    public static String FOURTH_STATION_NAME = "판교역";

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
        return new Sections(new ArrayList<>(Arrays.asList(sections)));
    }
}
