package subway.domain.station.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.domain.line.entity.Section;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@NoArgsConstructor
@EqualsAndHashCode()
public class Station {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Column(length = 20, nullable = false)
    @Getter
    private String name;

    @EqualsAndHashCode.Exclude
    @ManyToMany
    private List<Section> sections;

    public Station(String name) {
        this.name = name;
    }
}
