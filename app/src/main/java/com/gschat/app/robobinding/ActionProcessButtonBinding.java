package com.gschat.app.robobinding;

import android.widget.Button;

import com.dd.processbutton.iml.ActionProcessButton;

import org.robobinding.viewattribute.BindingAttributeMappings;
import org.robobinding.viewattribute.ViewBinding;

/**
 * implement customer robobinding ViewBinding
 */
public class ActionProcessButtonBinding implements ViewBinding<ActionProcessButton> {
    @Override
    public void mapBindingAttributes(BindingAttributeMappings<ActionProcessButton> mappings) {
        mappings.mapProperty(ActionProcessButtonProcessAttribute.class,"process");

        mappings.mapProperty(ActionProcessButtonModeAttribute.class,"mode");

        //mappings.mapEvent(FancyButtonOnClickEvent.class,"onClick");
    }
}
