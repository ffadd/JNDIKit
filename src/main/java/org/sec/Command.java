package org.sec;

import com.beust.jcommander.Parameter;

public class Command {
    @Parameter(names = {"-h", "--help"}, description = "Help Info", help = true)
    public boolean help;

    @Parameter(names = {"--cmd"}, description = "Use Command")
    public String command;

    @Parameter(names = {"--lp"}, description = "LDAP Server Port")
    public int ldapPort = 1389;

    @Parameter(names = {"--hp"}, description = "HTTP Server Port")
    public int httpPort = 8000;
}
