package subway.line;

import subway.commons.ErrorCode;
import subway.commons.HttpException;
import subway.section.Section;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class Sections {

    @OneToMany(mappedBy = "line",fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.PERSIST)
    @OrderColumn(name = "section_order")
    private List<Section> sections = new ArrayList<>();

    public void addSection(Section section) {
        validateCreateSection(section);
        sections.add(section);
    }

    public void deleteSection(Long stationId) {
        validateDeleteSection(stationId);
        sections.remove(sections.size() - 1);
    }

    private void validateCreateSection(Section creatingSection) {
        // 하행 종점이 같은 것이 존재하면 x
        boolean isSameDownTerminalExist = sections.stream().anyMatch(section -> section.getDownStationId() == creatingSection.getDownStationId());
        if (isSameDownTerminalExist) {
            throw new HttpException(ErrorCode.DOWN_STATION_NOT_VALID, creatingSection.getDownStationId().toString());
        }
        // 상행 종점이 같은 것이 존재하면 x
        boolean isSameUpTerminalExist = sections.stream().anyMatch(section -> section.getUpStationId() == creatingSection.getUpStationId());
        if (isSameUpTerminalExist) {
            throw new HttpException(ErrorCode.UP_STATION_NOT_VALID);
        }
        // 등록하는 상행역이 기존 하행종점과 같지 않으면 x
        Section lastSection = sections.get(sections.size() - 1);
        if (lastSection.getDownStationId() != creatingSection.getUpStationId()) {
            throw new HttpException(ErrorCode.UP_STATION_NOT_VALID);
        }
    }

    private void validateDeleteSection(Long stationId) {
        // 마지막 구간 제거 금지
        if (sections.size() == 1) {
            throw new HttpException(ErrorCode.CANNOT_REMOVE_LAST_SECTION);
        }
        // 제거하는 역이 하행종점이 아니면 x
        Section lastSection = sections.get(sections.size() - 1);
        if (lastSection.getDownStationId() != stationId) {
            throw new HttpException(ErrorCode.IS_NOT_TERMINAL_STATION, stationId.toString());
        }

    }
}
