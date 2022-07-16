package nextstep.subway.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Line line;

    private Long upStationId;

    private Long downStationId;

    private Integer distance;

    public Section(Long upStationId, Long downStationId, Integer distance) {
        this(null, upStationId, downStationId, distance);
    }

    @Builder
    public Section(Line line, Long upStationId, Long downStationId, Integer distance) {
        this.line = line;
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }


    public boolean existsStation(Long downStationId) {
        return this.downStationId.equals(downStationId)
                || this.upStationId.equals(downStationId);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Section section = (Section) o;
        return Objects.equals(upStationId, section.upStationId) && Objects.equals(downStationId, section.downStationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(upStationId, downStationId);
    }
}
