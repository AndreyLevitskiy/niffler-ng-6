package guru.qa.niffler.data.tpl;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class JdbcConnectionHolder implements AutoCloseable {

    private final DataSource dataSources;
    private final Map<Long, Connection> threadConnections = new ConcurrentHashMap<>();

    public JdbcConnectionHolder(DataSource dataSources) {
        this.dataSources = dataSources;
    }

    @Override
    public void close() throws Exception {
        Optional.ofNullable(threadConnections.remove(Thread.currentThread().threadId()))
                .ifPresent(connection -> {
                    try {
                        if (!connection.isClosed()){
                            connection.close();
                        }
                    } catch (Exception e) {
                        //NOP
                    }
                });
    }

    public void closeAllConnections() {
        threadConnections.values().forEach(
                connection -> {
                    try {
                        if (connection != null && !connection.isClosed()){
                            connection.close();
                        }
                    } catch (Exception e) {
                        //NOP
                    }
                }
        );
    }
}
