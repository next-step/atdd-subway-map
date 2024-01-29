package subway.line;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 10, nullable = false)
    private String color;

    @Column(nullable = false)
    private Integer upStationId;

    @Column(nullable = false)
    private Integer downStationId;

    @Column(nullable = false)
    private Integer distance;

    public Line updateLine(LineRequest req) {
        this.name = req.getName();
        this.color = req.getColor();
        return this;
    }
}
