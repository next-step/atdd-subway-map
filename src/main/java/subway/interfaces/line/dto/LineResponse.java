package subway.interfaces.line.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import subway.domain.line.LineInfo;
import subway.interfaces.station.dto.StationResponse;
import subway.domain.line.entity.Line;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class LineResponse {
    private final Long id;
    private final String name;
    private final String color;
    private final List<StationResponse> stations;

    public static LineResponse from(LineInfo.Main line) {
        List<StationResponse> stations = new ArrayList<>();
        stations.add(StationResponse.from(line.getSections().get(0).getUpStation()));
        stations.addAll(
                line.getSections().stream()
                        .map(section -> StationResponse.from(section.getDownStation()))
                        .collect(Collectors.toList())
        );

        return new LineResponse(line.getId(), line.getName(), line.getColor(), stations);
    }

}
