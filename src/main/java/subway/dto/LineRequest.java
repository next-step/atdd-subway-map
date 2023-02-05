package subway.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LineRequest {
    @NotBlank(message = "name is not blank")
    private String name;
    @NotBlank(message = "color is not blank")
    private String color;
}
