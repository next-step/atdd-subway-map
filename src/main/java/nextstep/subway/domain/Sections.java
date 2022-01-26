package nextstep.subway.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    public List<Station> getStations() {
        List<Station> result = new ArrayList<>();

        sections.forEach(sec -> result.add(sec.getUpStation()));
        result.add(sections.get(sections.size() - 1).getDownStation());
        
        return result;
    }
}
