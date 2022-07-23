package nextstep.subway.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Embeddable
public class Sections {

    @OneToMany(
            mappedBy = "line",
            orphanRemoval = true,
            cascade = { CascadeType.PERSIST, CascadeType.REMOVE }
    )

    private List<Section> sections = new ArrayList<>();

    public void add(Section section) {
        this.sections.add(section);
    }

    public Set<Long> getLineStationIds() {
        Set<Long> stationIds = new HashSet<>();
        sections.forEach((section) -> {
            stationIds.add(section.getUpStationId());
            stationIds.add(section.getDownStationId());
        });
        return stationIds;
    }


}
