package client;

import com.netflix.astyanax.AstyanaxContext;
import com.netflix.astyanax.Keyspace;
import com.netflix.astyanax.connectionpool.NodeDiscoveryType;
import com.netflix.astyanax.connectionpool.impl.ConnectionPoolConfigurationImpl;
import com.netflix.astyanax.connectionpool.impl.CountingConnectionPoolMonitor;
import com.netflix.astyanax.impl.AstyanaxConfigurationImpl;
import com.netflix.astyanax.thrift.ThriftFamilyFactory;

/**
 * Created with IntelliJ IDEA.
 * User: huixiao200068
 * Date: 13-8-16
 * Time: 上午11:19
 * Cassandra Client Factory
 */
public class AstyanaxClientFactory {

    private final static String Default_ClusterName = "Test Cluster";
    private final static String Default_Seeds = "127.0.0.1";
    private final static int Default_Port = 9160;
    private final static int Default_MinConn = 1;
    private final static int Default_MaxConn = 5;

    private static AstyanaxContext<Keyspace> context;

    private static AstyanaxClientFactory factory = new AstyanaxClientFactory();

    public static AstyanaxClientFactory buildFactory(String KesSpace) {
        return buildFactory(Default_ClusterName, KesSpace, Default_Seeds, Default_Port, Default_MinConn, Default_MaxConn);
    }

    public static AstyanaxClientFactory buildFactory(String ClusterName, String KeySpace, String seeds, int port, int minConn, int maxConn) {
        if(context == null) {
            context = new AstyanaxContext.Builder()
                    .forCluster(ClusterName)
                    .forKeyspace(KeySpace)
                    .withAstyanaxConfiguration(
                            new AstyanaxConfigurationImpl().setDiscoveryType(NodeDiscoveryType.RING_DESCRIBE)
                                .setCqlVersion("3.0.0")
                                .setTargetCassandraVersion("1.2")
                    )
                    .withConnectionPoolConfiguration(
                            new ConnectionPoolConfigurationImpl("MyConnectionPool")
                                    .setSeeds(seeds)
                                    .setPort(port)
                                    .setInitConnsPerHost(minConn)
                                    .setMaxConnsPerHost(maxConn)
                    )
                    .withConnectionPoolMonitor(new CountingConnectionPoolMonitor())
                    .buildKeyspace(ThriftFamilyFactory.getInstance());
            context.start();
        }
        return factory;
    }

    public AstyanaxClient getClient() {
        return new AstyanaxClient(context.getClient());
    }
}
