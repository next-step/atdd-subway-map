package subway.line;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * StationLine
 *
 * @author JungGyun.Choi
 * @version 1.0.0
 * @since 2023. 01. 29.
 */
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StationLine {

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
    private Integer distance;

    @Builder
    public StationLine(Long id, String name, String color, Long upStationId, Long downStationId, Integer distance) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public void putStationLine(StationLine stationLine) {
        this.name = stationLine.getName();
        this.color = stationLine.getColor();
    }
}
