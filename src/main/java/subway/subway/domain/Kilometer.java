package subway.subway.domain;

import java.math.BigDecimal;

public class Kilometer {

    private final BigDecimal kilometer;

    public Kilometer(BigDecimal kilometer) {
        this.kilometer = kilometer;
    }

    public BigDecimal getKilometer() {
        return kilometer;
    }
}
