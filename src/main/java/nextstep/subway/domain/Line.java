package nextstep.subway.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityNotFoundException;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "지하철 노선의 색은 필수 값 입니다.")
    @Length(min = 1, max = 100, message = "지하철 노선 색의 길이를 확인해주세요.")
    private String color;

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Section> sections = new ArrayList<>();

    @Builder
    public Line(String name, String color, Section section) {
        this.name = name;
        this.color = color;
        this.sections.add(section);
        section.setLine(this);
    }

    public static Line createLine(String name, String color, Section section) {
        return Line.builder()
                .name(name)
                .color(color)
                .section(section)
                .build();
    }

    public void updateNameAndColor(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public int getTotalDistance() {
        int totalDistance = 0;
        for (Section section : sections) {
            totalDistance += section.getDistance();
        }
        return totalDistance;
    }

    public Station getUpStationTerminal() {
        return sections.get(0).getUpStation();
    }

    public Station getDownStationTerminal() {
        return sections.get(sections.size() - 1).getDownStation();
    }

    public void addSection(Section newSection) {
        this.sections.stream()
                .filter(section -> isSameSection(newSection, section)
                        || isNewSectionDownStationInLine(newSection, section))
                .findFirst()
                .ifPresent(section -> {
                    throw new IllegalArgumentException("구간이나 역이 중복되어 있으면 등록할 수 없습니다.");
                });

        if (isNotTerminalDownStation(newSection)) {
            throw new IllegalArgumentException("새로운 구간은 기존 노선의 하행 종점역이어야 합니다.");
        }

        this.sections.add(newSection);
        newSection.setLine(this);
    }

    public Section deleteSection(Station station) {
        if (this.getDownStationTerminal() != station) {
            throw new IllegalArgumentException("하행종점역만 삭제할 수 있습니다.");
        }
        Section section = getSectionByDownStation(station);

        sections.remove(section);
        return section;
    }

    private Section getSectionByDownStation(Station downStation) {
        return sections.stream()
                    .filter(e -> e.getDownStation().equals(downStation))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException("해당되는 구간을 찾을 수 없습니다."));
    }

    private boolean isNewSectionDownStationInLine(Section newSection, Section section) {
        return section.getUpStation() == newSection.getDownStation()
                || section.getDownStation() == newSection.getDownStation();
    }

    public List<Station> getStations() {
        if (this.sections.isEmpty()) {
            return Collections.emptyList();
        }

        List<Station> stations = this.sections.stream()
                .map(Section::getDownStation)
                .collect(Collectors.toList());
        stations.add(0, this.getUpStationTerminal());

        return stations;
    }

    private boolean isNotTerminalDownStation(Section newSection) {
        return getDownStationTerminal() != newSection.getUpStation();
    }

    private boolean isSameSection(Section newSection, Section section) {
        return section.getUpStation() == newSection.getUpStation()
                && section.getDownStation() == newSection.getDownStation();
    }
}
