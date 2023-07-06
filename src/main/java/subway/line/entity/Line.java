package subway.line.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.station.entity.Station;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 20, nullable = false)
    private String color;

    @OneToOne
    @JoinColumn
    private Station upStation;

    @OneToOne
    @JoinColumn
    private Station downStation;

    @Column(nullable = false)
    private Integer distance;

    @Builder
    public Line(
            String name,
            String color,
            Station upStation,
            Station downStation,
            Integer distance
    ) {
        this.name = name;
        this.color = color;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }
}
