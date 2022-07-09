package nextstep.subway.ui.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nextstep.subway.applicaion.dto.LineDto;

@Getter
@Builder
@RequiredArgsConstructor
public class LineResponse {

    private final Long id;
    private final String name;
    private final String color;

    public static LineResponse of(LineDto lineDto) {
        return LineResponse.builder()
                .id(lineDto.getId())
                .name(lineDto.getName())
                .color(lineDto.getColor())
                .build();
    }
}
