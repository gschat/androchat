package com.gschat.app.robobinding;

import com.dd.processbutton.iml.ActionProcessButton;

import org.robobinding.viewattribute.property.PropertyViewAttribute;

/**
 * implement attribute mapping
 */
public class ActionProcessButtonProcessAttribute implements PropertyViewAttribute<ActionProcessButton,Integer>{
    @Override
    public void updateView(ActionProcessButton actionProcessButton, Integer process) {
        actionProcessButton.setProgress(process);
    }
}
