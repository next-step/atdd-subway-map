package nextstep.subway.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String color;

    @Builder.Default
    @OneToMany(mappedBy = "line")
    private List<Section> sections = new ArrayList<>();

    public void modifyNameAndColor(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(Section section) {
        this.sections.add(section);
        section.setLine(this);
    }

    public List<Long> getAllStationIds() {
        return this.getSections().stream()
                .map(section -> List.of(section.getUpStationId(), section.getDownStationId()))
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());
    }

    public Long getFinalDownStationId(Long downStationId) {
        List<Long> upStationIds = getUpStationIds();
        List<Long> downStationIds = getDownStationIds();

        return downStationIds.stream()
                .filter(down -> !upStationIds.contains(down))
                .findAny()
                .orElse(downStationId);
    }

    private List<Long> getUpStationIds() {
        return this.sections.stream().map(Section::getUpStationId).collect(Collectors.toList());
    }

    private List<Long> getDownStationIds() {
        return this.sections.stream().map(Section::getDownStationId).collect(Collectors.toList());
    }
}
