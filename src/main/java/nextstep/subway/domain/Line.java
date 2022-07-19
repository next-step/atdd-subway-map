package nextstep.subway.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.exception.NotFoundException;

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
    @Embedded
    private Sections sections = new Sections();

    public void modifyNameAndColor(String name, String color) {
        this.name = name;
        this.color = color;
    }

    public void addSection(Section section) {
        this.sections.addSection(section);
        section.setLine(this);
    }

    public Section findSectionByDownStationId(Long downStationId) {
        return this.sections.findSectionByDownStationId(downStationId);
    }

    public List<Long> getAllStationIds() {
        return this.sections.getAllStationIds();
    }

    public Long getFinalDownStationId(Long downStationId) {
        return this.sections.getFinalDownStationId(downStationId);
    }
}
