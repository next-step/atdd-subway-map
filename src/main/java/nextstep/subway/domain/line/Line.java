package nextstep.subway.domain.line;

import lombok.*;
import nextstep.subway.domain.LineStation;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String color;
    private Integer distance;

    @OneToMany(mappedBy = "line", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<LineStation> lineStations = new ArrayList<>();


    @Builder
    protected Line(Long id, String name, String color, Integer distance, List<LineStation> lineStations) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.distance = distance;
        this.lineStations = lineStations;
    }
}
