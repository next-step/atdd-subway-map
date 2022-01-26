package nextstep.subway.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line", cascade = {CascadeType.PERSIST, CascadeType.MERGE},
        orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Section> sections = new ArrayList<>();

    public void add(Section section) {
        sections.add(section);
    }

    public boolean isEmpty() {
        return sections.isEmpty();
    }

    public void validateUpStation(Section section) {
        if (!sections.get(sections.size()-1).getDownStation().getId()
            .equals(section.getUpStation().getId())) {
            throw new IllegalArgumentException("등록하는 구간의 상행역이 기존 최하행역과 맞지 않음.");
        }
    }

    public void validateDownStation(Section section) {
        if (sections.stream()
            .anyMatch(section1 -> (section1.getUpStation().getId()
                .equals(section.getDownStation().getId()))
                || section1.getDownStation().getId().equals(section.getDownStation().getId()))) {
            throw new IllegalArgumentException("등록하는 구간의 하행역이 기존 라인에 등록된 역임.");
        }
    }

    public int size() {
        return sections.size();
    }

    public Section get(int index) {
        return sections.get(index);
    }

    public List<Section> get() {
        return sections;
    }

    public boolean contains(Section section) {
        return sections.contains(section);
    }

    public void validateRemovalbe(Station station) {
        if (sections.size() <= 1) {
            throw new IllegalStateException("노선에 구간이 1개 남은 상태는 역을 삭제 할 수 없음.");
        }

        if (!isLastStation(station)) {
            throw new IllegalArgumentException("노선의 마지막 구간 하행역이 아니면 삭제 할 수 없음.");
        }
    }

    private boolean isLastStation(Station station) {
        if (sections.isEmpty()) {
            return false;
        }

        return sections.get(sections.size()-1).getDownStation().equals(station);
    }

    public void removeFarDownSection() {
        sections.remove(sections.size()-1);
    }

    public List<Station> getStations() {
        List<Station> stations = new ArrayList<>();
        for (int i = 0; i < sections.size(); ++i) {
            stations.add(sections.get(i).getUpStation());
            if (i == sections.size()-1) {
                stations.add(sections.get(sections.size()-1).getDownStation());
            }
        }
        return stations;
    }
}
