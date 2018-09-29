import groovy.text.SimpleTemplateEngine;
import groovy.text.TemplateEngine;
import org.junit.Test;
import org.springframework.util.Assert;
import site.binghai.lib.utils.GroovyEngineUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author huaishuo
 * @date 2018/09/28
 */
public class GroovyTemplateTest {

    @Test
    public void testMake() throws Exception {
        Map ctx = new HashMap();
        ctx.put("myName", 121);
        String ret = GroovyEngineUtils.instanceGroovyEngine("{My Name is ${myName+1}}",ctx);
        Assert.notNull(ret);
        System.out.println(ret);
    }
}
