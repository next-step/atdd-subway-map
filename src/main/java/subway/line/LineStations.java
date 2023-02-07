package subway.line;

import lombok.*;

import javax.persistence.*;
import java.util.*;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LineStations {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LineStation> values = new ArrayList<>();

    public LineStations add(final LineStation lineStation) {
        this.values.add(lineStation);
        return this;
    }

    public List<LineStation> getValues() {

        return List.copyOf(this.values);
    }
}
