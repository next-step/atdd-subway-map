package subway.domain.section;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@NoArgsConstructor
@Embeddable
public class Distance {
    @Column(nullable = false)
    private Long distance;

    public Distance(Long distance) {
        this.distance = distance;
    }

}
