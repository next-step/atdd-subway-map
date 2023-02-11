package subway.line;

import lombok.*;

import javax.persistence.*;
import java.util.*;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Sections {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "line", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Section> values = new ArrayList<>();

    public Sections add(final Section section) {
        this.values.add(section);
        return this;
    }

    public List<Section> getValues() {

        return List.copyOf(this.values);
    }
}
