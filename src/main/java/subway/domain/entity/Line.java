package subway.domain.entity;

import lombok.Builder;
import lombok.Getter;
import subway.domain.command.LineCommand;

import javax.persistence.*;

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

    @Column(nullable = false)
    private Long upStationId;

    @Column(nullable = false)
    private Long downStationId;

    @Column(nullable = false)
    private Long distance;

    protected Line() {
    }

    @Builder
    public Line(String name, String color, Long upStationId, Long downStationId, Long distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public static Line init(LineCommand.CreateLine command) {
        return new Line(
                command.getName(),
                command.getColor(),
                command.getUpStationId(),
                command.getDownStationId(),
                command.getDistance()
        );
    }

    public void update(LineCommand.UpdateLine command) {
        this.name = command.getName();
        this.color = command.getColor();
        this.upStationId = command.getUpStationId();
        this.downStationId = command.getDownStationId();
        this.distance = command.getDistance();
    }
}
