package subway.line;

import static common.Constants.신분당선;

import subway.line.section.Sections;
import subway.station.Station;

public class LineBuilder {

    private Long id = 1L;
    private String name = 신분당선;
    private String color = "bg-red-600";
    private Sections sections;
    private int distance = 10;

    private LineBuilder() {}

    public static LineBuilder aLine() {
        return new LineBuilder();
    }

    public LineBuilder withId(Long id) {
        this.id = id;

        return this;
    }

    public LineBuilder withName(String name) {
        this.name = name;

        return this;
    }

    public LineBuilder withColor(String color) {
        this.color = color;

        return this;
    }

    public LineBuilder withSections(Sections sections) {
        this.sections = sections;

        return this;
    }

    public LineBuilder withDistance(int distance) {
        this.distance = distance;

        return this;
    }

    /**
     * withSections를 사용했을 때 사용한다
     *
     * @return Line
     *
     * @author JerryK026
     * @date 2023-07-13
     */
    public Line build() {
        return new Line(id, name, color, sections, distance);
    }

    /**
     * withSections를 사용하지 않았을 때 사용한다
     *
     * @param upStation
     * @param downStation
     * @return Line
     *
     * @author JerryK026
     * @date 2023-07-13
     */
    public Line build(Station upStation, Station downStation) {
        Line line = new Line(id, name, color, sections, distance);
        line.initStations(upStation, downStation);

        return line;
    }
}
