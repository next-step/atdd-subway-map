package subway.domain;

import subway.service.command.SubwayLineCreateCommand;

import javax.persistence.*;

@Entity
public class SubwayLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    @Column(nullable = false)
    private Long upStationId;

    @Column(nullable = false)
    private Long downStationId;

    @Column(nullable = false)
    private Long distance;

    protected SubwayLine() {
    }

    private SubwayLine(String name, String color, Long upStationId, Long downStationId, Long distance) {
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public static SubwayLine create(SubwayLineCreateCommand createCommand) {
        return new SubwayLine(createCommand.getName(), createCommand.getColor(), createCommand.getUpStationId(), createCommand.getDownStationId(), createCommand.getDistance());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public Long getUpStationId() {
        return upStationId;
    }

    public Long getDownStationId() {
        return downStationId;
    }

    public Long getDistance() {
        return distance;
    }

    public void modify(String lineName, String color) {
        this.name = lineName;
        this.color = color;
    }
}
