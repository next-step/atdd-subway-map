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

    public static LineResponse of(LineDto lineDto) {
        return LineResponse.builder()
                .id(lineDto.getId())
                .build();
    }
}
