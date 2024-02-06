package subway.domain.line.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line")
    private List<Section> sections = new ArrayList<>();

    public boolean isSectionsNullOrEmpty(){
        return sections == null || sections.isEmpty();
    }

    public boolean hasMoreThanOne(Long stationId) {
        if (sections == null || sections.isEmpty()) {
            return false;
        }

        return sections.size() > 1;
    }

    public Section getLastSection() {
        return sections.get(sections.size() - 1);
    }
}
