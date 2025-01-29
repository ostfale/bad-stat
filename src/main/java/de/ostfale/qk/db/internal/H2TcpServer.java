package de.ostfale.qk.db.internal;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import org.h2.tools.Server;
import org.jboss.logging.Logger;

import java.sql.SQLException;

@ApplicationScoped
public class H2TcpServer {
    private Server tcpServer;

    private static final Logger log = Logger.getLogger(H2TcpServer.class);

    public void onStart(@Observes StartupEvent ev) {
        try {
            tcpServer = Server.createTcpServer("-tcp", "-tcpAllowOthers", "-tcpPort", "9092").start();
            log.info("H2 TCP server started on port 9092");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to start H2 TCP server", e);
        }
    }

    public void onStop(@Observes ShutdownEvent ev) {
        if (tcpServer != null) {
            tcpServer.stop();
            log.info("H2 TCP server stopped");
        }
    }
}
