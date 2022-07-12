package nextstep.subway.ui.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.LineDto;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
@RequiredArgsConstructor
public class LineResponse {

    private final Long id;
    private final String name;
    private final String color;
    private final List<StationResponse> stations;

    public static LineResponse of(LineDto lineDto) {
        return LineResponse.builder()
                .id(lineDto.getId())
                .name(lineDto.getName())
                .color(lineDto.getColor())
                .stations(lineDto.getStations().stream()
                        .map(StationResponse::of)
                        .collect(Collectors.toUnmodifiableList()))
                .build();
    }
}
