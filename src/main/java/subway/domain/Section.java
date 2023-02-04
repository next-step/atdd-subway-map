package subway.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import javax.persistence.*;

@Getter
@Entity
public class Section {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "section_id")
    private Long id;
    private Long upStationId;
    private Long downStationId;
    private int distance;
    @ManyToOne
    @JoinColumn(name = "line_id")
    @JsonIgnore
    private Line line;

    public Section() {
    }

    public Section(Long upStationId, Long downStationId, int distance) {
        this.upStationId = upStationId;
        this.downStationId = downStationId;
        this.distance = distance;
    }

    public void setLine(Line line) {
        this.line = line;
    }
}
