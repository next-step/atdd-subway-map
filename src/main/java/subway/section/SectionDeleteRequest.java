package subway.section;

import java.util.List;
import java.util.Objects;
import subway.lines.Line;

public class SectionDeleteRequest {
    private final Long stationId;

    public SectionDeleteRequest(Long stationId) {
        this.stationId = stationId;
    }

    public Long getStationId() {
        return stationId;
    }

    public void validateToDelete(Line line) {
        final List<Section> sections = line.getSections();
        if (sections.size() == 1) {
            throw new IllegalArgumentException();
        }

        if(!Objects.equals(stationId, line.getDownStationId())) {
            throw new IllegalArgumentException();
        }
    }
}
