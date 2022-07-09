package nextstep.subway.applicaion.dto;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class StationDto {

    private final Long id;
    private final String name;
}
