package nextstep.subway.lines.domain;

import nextstep.subway.stations.domain.Station;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.*;

@Embeddable
public class Sections implements Iterable<Section> {

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    @JoinColumn(name = "line_id")
    List<Section> sections = new ArrayList<>();

    public void addSection(Station upStation, Station downStation, int distance) {
        addSection(new Section(upStation, downStation, distance));
    }

    public void addSection(Section section) {
        this.sections.add(section);
    }

    public void removeSection(Section section) {
        if(this.sections.size() == 1) {
            throw new IllegalStateException("");
        }
        this.sections.remove(section);
    }

    public boolean isEmpty() {
        return this.sections.isEmpty();
    }

    public Section findFirst() {
        if(!isEmpty()) {
            return this.sections.get(0);
        }

        throw new IllegalStateException("");
    }

    @Override
    public Iterator<Section> iterator() {
        return this.sections.iterator();
    }

    public Set<Station> extractStations() {
        Set<Station> stations = new LinkedHashSet<>();

        for(Section section : this.sections) {
            stations.add(section.getUpStation());
            stations.add(section.getUpStation());
        }

        return stations;
    }
}
