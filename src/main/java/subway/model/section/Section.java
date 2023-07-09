package subway.model.section;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.model.line.Line;
import subway.model.station.Station;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Section {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "downStationId", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Station downStation;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "upStationId", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Station upStation;

    @Column(nullable = false)
    private Long distance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lineId", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Line line;

    public void setLine(Line line) {
        this.line = line;
    }
}
