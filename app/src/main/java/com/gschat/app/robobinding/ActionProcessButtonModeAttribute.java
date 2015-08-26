package com.gschat.app.robobinding;

import com.dd.processbutton.iml.ActionProcessButton;

import org.robobinding.viewattribute.property.PropertyViewAttribute;

/**
 * implement customer view attribute binding
 */
public class ActionProcessButtonModeAttribute implements PropertyViewAttribute<ActionProcessButton,ActionProcessButton.Mode> {
    @Override
    public void updateView(ActionProcessButton actionProcessButton, ActionProcessButton.Mode mode) {
        actionProcessButton.setMode(mode);
    }
}
