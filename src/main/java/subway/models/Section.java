package subway.models;

import java.util.Arrays;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private Long distance;
    @Column
    private Integer sequence;
    @ManyToOne
    private Line line;
    @ManyToOne
    private Station upStation;
    @ManyToOne
    private Station downStation;

    @Builder
    private Section(Long distance, Integer sequence, Line line, Station upStation,
        Station downStation) {
        this.distance = distance;
        this.sequence = sequence;
        this.line = line;
        this.upStation = upStation;
        this.downStation = downStation;
    }

    public List<Station> getStations() {
        return Arrays.asList(upStation, downStation);
    }
}
