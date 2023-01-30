package subway;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor()
public class SubwayLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(length = 31, nullable = false)
    private String name;
    @Column(length = 31, nullable = false)
    private String color;
    @Column(nullable = false)
    private Long upStationId;
    @Column(nullable = false)
    private Long downStationId;
    @Column(nullable = false)
    private Integer distance;

    public SubwayLine(long id, String name, String color, Long upStationId,
        Long downStationId, Integer distance) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }
}
