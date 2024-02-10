package subway.line.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import subway.exception.InvalidDownStationException;
import subway.exception.SingleSectionDeleteException;
import subway.section.entity.Section;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> sections = new ArrayList<>();

    @Builder
    public Line(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void update(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(Section section) {
        this.getSections().add(section);
    }

    public void deleteSection(Long stationId) {
        if (sections.size() == 1)
            throw new SingleSectionDeleteException("구간이 한개인 경우 삭제할 수 없습니다.");

        Long downStationId = findDownStationId();
        if (downStationId != stationId) {
            throw new InvalidDownStationException("삭제 역이 하행 종점역이 아닙니다.");
        }
        sections.remove(sections.size() - 1);
    }

    public Long findDownStationId() {
        Long downStationId = null;

        for (int i = this.sections.size() - 1; i >= 0; i--) {
            Section section = this.sections.get(i);
            if (section.getDownStation() != null) {
                downStationId = section.getDownStation().getId();
                break;
            }
        }
        return downStationId;
    }

    public List<Long> getStationIds() {
        List<Long> stationIds = new ArrayList<>();
        for (Section section : this.sections) {
            stationIds.add(section.getUpStation().getId());
            stationIds.add(section.getDownStation().getId());
        }
        return stationIds;
    }

}
