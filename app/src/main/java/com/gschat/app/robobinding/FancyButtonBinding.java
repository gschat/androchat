package com.gschat.app.robobinding;

import org.robobinding.viewattribute.BindingAttributeMappings;
import org.robobinding.viewattribute.ViewBinding;
import org.robobinding.viewattribute.property.PropertyViewAttribute;

import mehdi.sakout.fancybuttons.FancyButton;

/**
 * bind fancy button
 */
public class FancyButtonBinding implements ViewBinding<FancyButton> {
    @Override
    public void mapBindingAttributes(BindingAttributeMappings<FancyButton> mappings) {

        mappings.mapEvent(FancyButtonOnClickEvent.class,"onClick");

        mappings.mapProperty(FancyButtonIconResourceAttribute.class, "fontIconResource");

        mappings.mapProperty(FancyButtonTextAttribute.class, "text");
    }
}


