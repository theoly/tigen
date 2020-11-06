package org.ukmms.tigen.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author theoly
 * @date 2020/11/6
 */
public class NameUtils {
    /**
     * 转驼峰命名正则匹配规则
     */
    private static final Pattern TO_HUMP_PATTERN = Pattern.compile("[-_]([a-z0-9])");
    private static final Pattern TO_LINE_PATTERN = Pattern.compile("[A-Z]+");

    public static String getEntityName(String name){
        if (StringUtils.isEmpty(name)) {
            return name;
        }
        // 强转全小写
        name = name.toLowerCase();
        Matcher matcher = TO_HUMP_PATTERN.matcher(name.toLowerCase());
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(buffer, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }

    public static String getClassName(String name){
        return StringUtils.capitalize(getEntityName(name));
    }
}
