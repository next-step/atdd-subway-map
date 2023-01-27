package subway;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StationRequest {
    private String name;

    @Builder
    private StationRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
