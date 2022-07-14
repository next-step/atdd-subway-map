package nextstep.subway.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Integer distance;

    public Line(String name, String color) {
        this(name, color, 0L, 0L, 0);
    }

    public Line(String name, String color, Long upStationId, Long downStationId, Integer distance) {
        this(0L, name, color, upStationId, downStationId, distance);
    }

    @Builder
    public Line(Long id, String name, String color, Long upStationId, Long downStationId, Integer distance) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }
}
