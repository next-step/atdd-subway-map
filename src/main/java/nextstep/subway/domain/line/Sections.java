package nextstep.subway.domain.line;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Sections {
    private static final int MINIMUM_STATIONS_SIZE = 1;
    @OneToMany(cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<Section> sections;

    protected Sections() {/*no-op*/}

    public Sections(final List<Section> sections) {
        if (sections.size() < MINIMUM_STATIONS_SIZE) {
            throw new IllegalArgumentException("역은 상행 종점역, 하행 종점역 두 개의 정보를 포함해야 합니다.");
        }

        this.sections = new ArrayList<>(sections);
    }

    public List<Section> getSections() {
        return List.copyOf(sections);
    }
}
