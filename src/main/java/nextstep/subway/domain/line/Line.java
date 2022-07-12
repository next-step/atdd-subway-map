package nextstep.subway.domain.line;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static java.util.Objects.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String color;
    @Column(nullable = false)
    private Integer distance;

    @Column(nullable = false)
    private Long upStationId;

    @Column(nullable = false)
    private Long downStationId;


    @Builder(toBuilder = true)
    protected Line(Long id, String name, String color, Integer distance, Long upStationId, Long downStationId) {
        requireNonNull(name, "name is required");
        requireNonNull(color, "color is required");
        requireNonNull(distance, "distance is required");
        requireNonNull(upStationId, "upStationId is required");
        requireNonNull(downStationId, "downStationId is required");

        this.id = id;
        this.name = name;
        this.color = color;
        this.distance = distance;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
    }
}
