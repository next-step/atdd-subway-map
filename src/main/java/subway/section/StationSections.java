package subway.section;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import subway.station.Station;

@Embeddable
public class StationSections {
    @OneToMany(cascade = CascadeType.ALL)
    private List<StationSection> sections = new ArrayList<>();

    public StationSections() {
    }

    public StationSections(List<StationSection> sections) {
        this.sections = sections;
    }

    public void addSection(Station upStation, Station downStation, long distance) {
//        validateSection(upStation, downStation);
        sections.add(new StationSection(null, upStation, downStation, distance));
    }

    // 다른 메서드들...

    public List<StationSection> getSections() {
        return sections;
    }
}
