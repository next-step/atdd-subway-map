package subway.line.dto.request;

import lombok.*;
import lombok.experimental.Accessors;
import subway.line.entity.Line;
import subway.station.entity.Station;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Accessors(chain = true)
public class SaveLineRequestDto {

    @NotBlank(message = "지하철 노선 이름은 빈칸일 수 없습니다.")
    @Size(max = 20, message = "지하철역 이름은 20자 이내여야 합니다.")
    private String name;

    @NotBlank(message = "지하철 노선 색깔은 빈칸일 수 없습니다.")
    @Size(max = 7, message = "지하철 노선 색깔은 7자 이내여야 합니다.")
    private String color;

    @Setter
    @NotNull
    private Long upStationId;

    @Setter
    @NotNull
    private Long downStationId;

    @NotNull
    private Integer distance;

    public Line toEntity(Station upStation, Station downStation) {
        return Line.builder()
                .name(name)
                .color(color)
                .upStation(upStation)
                .downStation(downStation)
                .distance(distance)
                .build();
    }

}
