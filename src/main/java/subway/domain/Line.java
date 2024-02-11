package subway.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.exception.InvalidDownStationException;
import subway.exception.SingleSectionDeleteException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


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
        return this.sections.get(this.sections.size()-1).getDownStation().getId();
    }

    public List<Long> getStationIds() {
        return this.sections.stream()
                .flatMap(section -> Stream.of(section.getUpStation().getId(), section.getDownStation().getId()))
                .collect(Collectors.toList());

    }

}
