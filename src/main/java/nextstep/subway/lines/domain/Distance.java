package nextstep.subway.lines.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Distance {

    @Column(name = "distance")
    private int value;

    protected Distance() {

    }

    public Distance(final int value) {
        validate(value);
        this.value = value;
    }

    private void validate(final int value) {
        if(value <= 0) {
            throw new IllegalArgumentException("지하철 노선의 거리는 0보다 커야합니다.");
        }
    }
}
