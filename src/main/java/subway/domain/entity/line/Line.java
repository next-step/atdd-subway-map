package subway.domain.entity.line;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import subway.domain.command.LineCommand;
import subway.domain.exception.SubwayDomainException;
import subway.domain.exception.SubwayDomainExceptionType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@ToString
@Getter
@Entity(name = "lines")
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 50, nullable = false)
    private String color;

    @OneToMany(mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LineSection> sections;

    protected Line() {
    }

    @Builder
    public Line(String name, String color, List<LineSection> sections) {
        this.name = name;
        this.color = color;
        this.sections = sections;
    }

    public static Line init(LineCommand.CreateLine command) {
        Line line = new Line(command.getName(), command.getColor(), new ArrayList<>());
        line.addSection(command.getUpStationId(), command.getDownStationId(), command.getDistance());
        return line;
    }

    private void verifyDownStationAlreadyExisted(Long stationId) {
        boolean existed = sections.stream()
                .flatMap(section -> Stream.of(section.getUpStationId(), section.getDownStationId()))
                .anyMatch(existedStationId -> existedStationId.equals(stationId));
        if (existed) {
            throw new SubwayDomainException(SubwayDomainExceptionType.INVALID_DOWN_STATION);
        }
    }

    public void addSection(Long upStationId, Long downStationId, Long distance) {
        // 새로운 구간의 상행역이 노선의 하행종창역이 아니도록 구간인 경우 에러
        if (!sections.isEmpty() && !sections.get(sections.size() - 1).getDownStationId().equals(upStationId)) {
            throw new SubwayDomainException(SubwayDomainExceptionType.INVALID_UP_STATION);
        }

        // 새로운 구간의 하행역이 이미 노선에 포함되어 있는 경우 에러
        verifyDownStationAlreadyExisted(downStationId);


        LineSection section = new LineSection(this, upStationId, downStationId, distance);
        this.sections.add(section);
    }


    public void update(LineCommand.UpdateLine command) {
        this.name = command.getName();
        this.color = command.getColor();
    }
}
