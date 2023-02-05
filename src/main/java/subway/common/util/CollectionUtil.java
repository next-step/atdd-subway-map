package subway.common.util;

import java.util.List;

public class CollectionUtil {

    public static <R> R lastItemOfList(List<R> list) {

        if (list.isEmpty())
            return null;

        int size = list.size();
        return list.get(size - 1);
    }

}
