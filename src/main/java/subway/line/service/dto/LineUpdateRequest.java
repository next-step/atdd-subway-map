package subway.line.service.dto;

import org.springframework.util.StringUtils;
import subway.line.exception.CreateRequestNotValidException;

public class LineUpdateRequest {
    private String name;
    private String color;
    private LineUpdateRequest() {
    }

    public LineUpdateRequest(final String name, final String color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public void validate() {
        if(!StringUtils.hasLength(name)) {
            throw new CreateRequestNotValidException("name can not be empty");
        }
        if(!StringUtils.hasLength(color)) {
            throw new CreateRequestNotValidException("color can not be empty");
        }
    }
}
