package subway.line.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.station.model.Station;

import javax.persistence.*;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Line line;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Station upStation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Station downStation;

    @Column(nullable = false)
    private Long distance;
}
