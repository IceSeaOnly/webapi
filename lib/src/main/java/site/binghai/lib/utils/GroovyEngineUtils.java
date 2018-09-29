package site.binghai.lib.utils;

import groovy.text.SimpleTemplateEngine;
import groovy.text.TemplateEngine;
import org.springframework.util.Assert;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GroovyEngineUtils {
    private static Map<String, GroovyLogicEngine> groovyEngineMap;
    private static final String prefix = "def Map invoke(Map context){def result=[:];";
    private static final String suffix = "return result;}";

    static {
        groovyEngineMap = new ConcurrentHashMap<>();
    }

    /**
     * 只需输入逻辑即可，例如 result.ret = context.a * context.b; 调用invoke方法得到结果Map中即可通过ret获取
     */
    public static GroovyLogicEngine instanceGroovyLogicEngine(String script) throws ScriptException {
        String md5 = MD5.encryption(script);
        GroovyLogicEngine engine = groovyEngineMap.get(md5);
        if (engine != null) { return engine; }
        engine = new GroovyLogicEngine(prefix + script + suffix, md5);
        groovyEngineMap.put(engine.getMd5(), engine);
        return engine;
    }

    public static String instanceGroovyEngine(String template, Map context) throws Exception {
        TemplateEngine engine = new SimpleTemplateEngine();
        return engine.createTemplate(template).make(context).toString();
    }

    public static class GroovyLogicEngine {
        private ScriptEngine scriptEngine;
        private String script;
        private String md5;

        public GroovyLogicEngine(String script, String md5) throws ScriptException {
            ScriptEngineManager factory = new ScriptEngineManager();
            ScriptEngine engine = factory.getEngineByName("groovy");
            engine.eval(script);
            this.md5 = md5;
            this.script = script;
            this.scriptEngine = engine;
        }

        public Map invoke(Map params) throws ScriptException, NoSuchMethodException {
            Invocable inv = (Invocable)scriptEngine;
            return (Map)inv.invokeFunction("invoke", params);
        }

        public ScriptEngine getScriptEngine() {
            return scriptEngine;
        }

        public String getScript() {
            return script;
        }

        public String getMd5() {
            return md5;
        }
    }

}
