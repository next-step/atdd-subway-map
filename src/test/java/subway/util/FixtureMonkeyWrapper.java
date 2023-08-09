package subway.util;

import com.navercorp.fixturemonkey.ArbitraryBuilder;
import com.navercorp.fixturemonkey.FixtureMonkey;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@UtilityClass
public class FixtureMonkeyWrapper {
    private static final FixtureMonkey FIXTURE_MONKEY = FixtureMonkey.builder()
            .defaultNotNull(true)
            .build();

    public static <T> List<T> giveMe(ArbitraryBuilder<T> builder, int size) {
        return Stream.generate(builder::sample)
                .limit(size)
                .collect(Collectors.toList());
    }

    public static FixtureMonkey create() {
        return FIXTURE_MONKEY;
    }
}
