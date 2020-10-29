import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.StringTemplateResourceLoader;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author theoly
 * @date 2020/10/28
 */
public class BeetlTest {
    @Test
    public void testBeetl(){
        String code = "package ${package};";
        StringTemplateResourceLoader resourceLoader = new StringTemplateResourceLoader();
        Configuration cfg = null;
        try {
            cfg = Configuration.defaultConfiguration();
        } catch (IOException e) {
            e.printStackTrace();
        }
        GroupTemplate gt = new GroupTemplate(resourceLoader, cfg);
        Template t = gt.getTemplate(code);
        Map<String, String> map = new HashMap<>();
        map.put("package", "com.wmz");
        map.put("preName", "wmz_");
        t.binding(map);
        code = t.render();
        System.out.println(code);
    }
}
