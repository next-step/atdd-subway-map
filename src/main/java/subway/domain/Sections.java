package subway.domain;

import subway.exception.ApplicationException;

import java.util.ArrayList;
import java.util.List;

public class Sections {
    private final List<Section> sections;

    public Sections(List<Section> sections) {
        validateSize(sections);
        this.sections = new ArrayList<>(sections);
    }

    private void validateSize(List<Section> sections) {
        if(sections.isEmpty()) {
            throw new IllegalArgumentException("구간이 존재하지 않습니다.");
        }
    }

    public void validateUpStationRegisterBy(Station upStation) {
        if(sections.stream().anyMatch(section -> section.isUpStation(upStation))) {
            throw new ApplicationException("새로운 구간의 상행역은 노선의 하행 종점역에만 등록할 수 있습니다.");
        }
    }

}
