package guru.qa.niffler.data.tpl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Connections {
    private Connections() {
    }

    private static final Map<String, JdbcConnectionHolder> holders = new ConcurrentHashMap<>();

    public JdbcConnectionHolder holder(String jdbcUrl) {
        return holders.computeIfAbsent(
                jdbcUrl,
                key ->
                        new JdbcConnectionHolder(DataSources.dataSource(jdbcUrl)
                        )
        );
    }

    public static void closeAllConnections() {
        holders.values().forEach(JdbcConnectionHolder::closeAllConnections);
    }

}
