package subway.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/** 지하철 노선 엔티티 */
@Entity
public class Line {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 지하철 노선명 */
    @Column(length = 20, nullable = false)
    private String name;

    /** 지하철 노선 색 */
    @Column(length = 20, nullable = false)
    private String color;

    // TODO: 추후 역과 연관관계 매핑?
    /** 상행종점역 */
    @Column
    private Long upStationId;

    /** 하행종점역 */
    @Column
    private Long downStationId;

    @Column
    private Integer distance;

    protected Line() {}

    private Line(
        final Long id,
        final String name,
        final String color,
        final Long upStationId,
        final Long downStationId,
        final Integer distance
    ) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public static Line withId(
        final Long id,
        final String name,
        final String color,
        final Long upStationId,
        final Long downStationId,
        final Integer distance
    ) {
        return new Line(id, name, color, upStationId, downStationId, distance);
    }

    public static Line of(
        final String name,
        final String color,
        final Long upStationId,
        final Long downStationId,
        final Integer distance
    ) {
        return new Line(null, name, color, upStationId, downStationId, distance);
    }

    public Line update(final String name, final String color) {
        return Line.withId(id, name, color, upStationId, downStationId, distance);
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

    public Integer getDistance() {
        return distance;
    }
}
