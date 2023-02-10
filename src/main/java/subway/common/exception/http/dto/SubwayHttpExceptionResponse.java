package subway.common.exception.http.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import subway.common.exception.SubwayException;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SubwayHttpExceptionResponse {

    private String purpose;
    private String location;
    private String message;

    @Builder(access = AccessLevel.PRIVATE)
    protected SubwayHttpExceptionResponse(String purpose, String location, String message) {
        this.purpose = purpose;
        this.location = location;
        this.message = message;
    }

    public static SubwayHttpExceptionResponse from(SubwayException subwayException) {
        return builder()
                .location(subwayException.getLocation())
                .message(subwayException.getMessage())
                .purpose(subwayException.getPurpose().label())
                .build();
    }

}