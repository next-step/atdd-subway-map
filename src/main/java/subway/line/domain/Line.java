package subway.line.domain;

import lombok.*;
import subway.line.dto.LineModifyRequest;
import subway.station.domain.Station;

import javax.persistence.*;

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

    @ManyToOne
    private Station upStation;

    @ManyToOne
    private Station downStation;

    @Column(nullable = false)
    private int distance;

    @Builder
    public Line(Long id, String name, String color, Station upStation, Station downStation, int distance) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public void modify(LineModifyRequest request) {
        modifyForName(request.getName());
        modifyForColor(request.getColor());
    }

    private void modifyForName(String name) {
        if (name != null && !name.isBlank()) {
            this.name = name;
        }
    }

    private void modifyForColor(String color) {
        if (color != null && !color.isBlank()) {
            this.color = color;
        }
    }

}