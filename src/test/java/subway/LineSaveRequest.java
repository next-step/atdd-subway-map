package subway;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LineSaveRequest {

    private String name;
    private String color;
    private Long upStationId;
    private Long downStationId;
    private Long distance;
}
