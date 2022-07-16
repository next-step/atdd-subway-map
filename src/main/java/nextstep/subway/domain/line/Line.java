package nextstep.subway.domain.line;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.subway.applicaion.line.dto.LineResponse;
import nextstep.subway.domain.station.Station;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String color;

    @OneToMany(mappedBy = "id")
    private List<Station> stations = new ArrayList<>();

    public LineResponse toResponse() {
        return LineResponse.builder()
                .id(id)
                .name(name)
                .color(color)
                .stations(stations)
                .build();
    }
}
