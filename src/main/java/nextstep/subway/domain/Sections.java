package nextstep.subway.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class Sections {
    private static final int MIN_SIZE = 1;

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    protected Sections() {
    }

    public Sections(Section section) {
        sections.add(section);
    }

    public void add(Section section){
        verifyConnectable(section);
        sections.add(section);
    }

    public void pop(Station station){
        Section lastSection = getLastSection();

        if (!lastSection.isEqualToDownStation(station) || sections.size() == MIN_SIZE) {
            throw new IllegalArgumentException("구간을 제거할 수 없습니다.");
        }

        sections.remove(sections.size()-1);
    }

    public Section getLastSection(){
        return sections.get(sections.size() - 1);
    }

    public List<Station> getStations(){
        List<Station> stations = sections.stream()
                .map(Section::getUpStation)
                .collect(Collectors.toList());

        stations.add(getLastSection().getDownStation());

        return stations;
    }

    private void verifyConnectable(Section section) {
        if (sections.isEmpty()) {
            throw new IllegalArgumentException("기존 구간이 없습니다.");
        }
        if (getStations().contains(section.getDownStation())) {
            throw new IllegalArgumentException("기등록된 역은 하행역으로 등록할 수 없습니다.");
        }

        Section lastSection = getLastSection();
        if (!lastSection.isConnectable(section)) {
            throw new IllegalArgumentException("상행역이 기등록된 하행 종점역과 일치하지 않습니다.");
        }
    }

}
