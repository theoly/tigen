package org.ukmms.tigen.service;

import freemarker.template.TemplateException;

import java.io.IOException;
import java.util.Map;

/**
 * @author theoly
 * @date 2020/10/29
 */
public interface GenerateService {
    String generate(String template, Map<String, Object> info) throws Exception;
}
