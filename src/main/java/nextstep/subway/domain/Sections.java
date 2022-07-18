package nextstep.subway.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.*;

@Embeddable
public class Sections {

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Section> value = new ArrayList<>();

    public void addSection(Section section) {
        this.value.add(section);
    }

    public List<Station> allStations() {
        List<Station> stations = new ArrayList<>();

        if (value.isEmpty()) {
            return unmodifiableList(stations);
        }

        stations.add(value.get(0).getUpStation());

        value.forEach(section -> {
            stations.add(section.getDownStation());
        });

        return unmodifiableList(stations);
    }

}
