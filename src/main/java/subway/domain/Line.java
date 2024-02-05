package subway.domain;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Getter;

@Entity
@Getter
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String color;

    @OneToMany(mappedBy = "line", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<Section> sections = new HashSet<>();

    public Line() {
    }

    public Line(String name, String color, int distance, Station upStation, Station downStation) {
        this.name = name;
        this.color = color;

        Section section = new Section(this, upStation.getId(), downStation.getId(), distance);
        sections.add(section);
    }


    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void registSection(Section section) {
        validateNewSectionStationNotInExistingInStations(section);
        // 하행 종점 구간 변수명
        getLatestSections().stream()
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("새로운 구간의 상행역은 기존 구간의 하행역과 일치해야 합니다."));

        sections.add(section);
    }

    private Optional<Section> getLatestSections() {
        return sections.stream().max(Comparator.comparing(Section::getId));
    }

    private void validateNewSectionStationNotInExistingInStations(Section newSection) {
        if (sections.stream()
            .anyMatch(
                section -> section.getDownStationId().equals(newSection.getDownStationId()) ||
                    section.getUpStationId().equals(newSection.getUpStationId()))) {
            throw new IllegalArgumentException("이미 해당 노선에 등록되어있는 역은 새로운 구간의 하행역이 될 수 없습니다.");
        }
    }

    public void deleteSection(Long stationId) {
        if (sections.size() == 1) {
            throw new RuntimeException("지하철 구간을 삭제할 수 없습니다");
        }
        getLatestSections()
            .ifPresent(section -> {
                if (!section.getDownStationId().equals(stationId)) {
                    throw new IllegalArgumentException("지하철 노선에 등록된 역(하행 종점역)만 제거할 수 있습니다.");
                }
                sections.remove(section);
            });
    }

    public Set<Long> getStationIds() {
        Set<Long> stationIds = new HashSet<>();
        for (Section section : sections) {
            stationIds.add(section.getUpStationId());
            stationIds.add(section.getDownStationId());
        }
        return stationIds;
    }
}
