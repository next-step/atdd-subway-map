package subway.station.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import subway.common.BaseEntity;
import subway.line.domain.Line;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Station extends BaseEntity {
    @Column(length = 20, nullable = false)
    private String name;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    private Line line;

    public Station(String name) {
        this.name = name;
    }
}
