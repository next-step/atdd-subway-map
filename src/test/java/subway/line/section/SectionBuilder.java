package subway.line.section;

import static common.Constants.강남역;
import static common.Constants.신논현역;

import subway.line.Line;
import subway.station.Station;

public class SectionBuilder {
    private Long id = 1L;
    private Station upStation = new Station(1L, 강남역);
    private Station downStation = new Station(2L, 신논현역);
    private Line line = null;
    private int sequence = 1;

    private SectionBuilder() {}

    public static SectionBuilder aSection() {
        return new SectionBuilder();
    }

    public SectionBuilder withId(Long id) {
        this.id = id;

        return this;
    }

    public SectionBuilder withStations(Station upStation, Station downStation) {
        this.upStation = upStation;
        this.downStation = downStation;

        return this;
    }

    public SectionBuilder withLine(Line line) {
        this.line = line;

        return this;
    }

    public SectionBuilder withSequence(int sequence) {
        this.sequence = sequence;

        return this;
    }

    public Section build() {
        return new Section(id, line, upStation, downStation, sequence);
    }
}
