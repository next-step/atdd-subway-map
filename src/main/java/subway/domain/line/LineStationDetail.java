package subway.domain.line;

import subway.domain.section.Section;
import subway.domain.section.Sections;
import subway.domain.station.Station;
import subway.exception.SectionAddException;
import subway.exception.SectionDeleteException;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.OneToOne;
import java.util.List;

@Embeddable
public class LineStationDetail {

    @OneToOne
    private Station startStation;

    @OneToOne
    private Station endStation;

    @Embedded
    private Sections sections;

    protected LineStationDetail() {
    }

    public LineStationDetail(Line line, Station upStation, Station downStation, int distance) {
        this.startStation = upStation;
        this.endStation = downStation;
        this.sections = new Sections(line, upStation, downStation, distance);
    }

    public void addSection(Section section) {
        if (!section.isUp(endStation)) {
            throw new SectionAddException();
        }
        endStation = section.getDownStation();
        sections.add(section);
    }

    public void removeSection(Station station) {
        Section section = sections.findSectionByDownStation(station);
        if (!section.isDown(endStation)) {
            throw new SectionDeleteException();
        }
        endStation = section.getUpStation();
        sections.remove(section);
    }

    public List<Station> getStations() {
        return sections.getStations(startStation);
    }
}
