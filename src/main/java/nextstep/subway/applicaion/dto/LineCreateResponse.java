package nextstep.subway.applicaion.dto;

import java.time.LocalDateTime;

public class LineCreateResponse extends BaseLineResponse{
    public LineCreateResponse(Long id, String name, String color, LocalDateTime createdDate, LocalDateTime modifiedDate) {
        super(id, name, color, createdDate, modifiedDate);
    }
}
