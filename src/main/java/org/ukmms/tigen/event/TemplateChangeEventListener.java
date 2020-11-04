package org.ukmms.tigen.event;

import java.util.EventListener;

/**
 * @author theoly
 * @date 2020/11/4
 */
public interface TemplateChangeEventListener extends EventListener {
    public void onTemplateChange(TemplateChangeEvent event);
}
