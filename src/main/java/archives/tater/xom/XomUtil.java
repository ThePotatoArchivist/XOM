package archives.tater.xom;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class XomUtil {
    private XomUtil() {}

    public static <K, V> Collector<K, ?, Map<K, V>> associateWith(Function<K, V> getValue) {
        return Collectors.toMap(it -> it, getValue);
    }
}
