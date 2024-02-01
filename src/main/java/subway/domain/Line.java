package subway.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Entity
@Table(name = "line")
@Builder
@Getter
@AllArgsConstructor
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 20, nullable = false)
    private String name;
    @Column(length = 20, nullable = false)
    private String color;
    @Embedded
    private Stations stations;

    public Line() {

    }

    public static Line of(LineRequest lineRequest, Stations stations) {
        return Line.builder()
                   .name(lineRequest.getName())
                   .color(lineRequest.getColor())
                   .stations(stations)
                   .build();

    }
}
