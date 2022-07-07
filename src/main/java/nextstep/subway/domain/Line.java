package nextstep.subway.domain;

import javax.persistence.*;

@Entity
public class Line {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private LineInfo lineInfo;
    @Embedded
    private Stations stations;

    protected Line() {
    }
}
