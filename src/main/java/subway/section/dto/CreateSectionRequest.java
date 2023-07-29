package subway.section.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.common.error.InvalidSectionRequestException;
import subway.line.domain.Line;
import subway.station.domain.Station;

import java.util.Map;
import java.util.Objects;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateSectionRequest {
    private Long upStationId;
    private Long downStationId;
    private int distance;

    public void validateUpStationId(Line line) {
        if (!Objects.equals(upStationId, line.getTerminalStationId())) {
            throw new InvalidSectionRequestException("해당 노선의 하행종점역이 아닌 역이 상행역으로 설정되었습니다.",
                    Map.of(
                            "lineId", line.getId().toString(),
                            "upStationId", upStationId.toString(),
                            "downStationId", downStationId.toString()
                    ));
        }
    }

    public void validateDownStationId(Line line, Station downStation) {
        if (line.getStations().contains(downStation)) {
            throw new InvalidSectionRequestException("이미 노선에 등록된 역을 새로운 구간의 하행역으로 등록하였습니다.",
                    Map.of(
                            "lineId", line.getId().toString(),
                            "upStationId", upStationId.toString(),
                            "downStationId", downStationId.toString()
                    ));
        }
    }
}
