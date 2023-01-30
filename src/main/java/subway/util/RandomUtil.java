package subway.util;

import java.util.Random;

public class RandomUtil {

    private RandomUtil() {
    }

    private static final Random random = new Random();

    public static Long getLandomLong() {
        return random.nextLong();
    }

}
