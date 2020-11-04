package org.ukmms.tigen.event;

import java.util.EventObject;

/**
 * @author theoly
 * @date 2020/11/4
 */
public class TemplateChangeEvent extends EventObject {
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public TemplateChangeEvent(Object source) {
        super(source);
    }
}
