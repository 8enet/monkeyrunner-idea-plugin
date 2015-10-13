package com.zzzmode.idea.plugin.monkeyrunner;

import com.intellij.openapi.components.*;
import com.intellij.openapi.options.*;
import com.intellij.openapi.project.*;
import com.intellij.openapi.ui.*;
import org.jetbrains.annotations.*;

import javax.swing.*;
import java.awt.*;

/**
 * Created by zl on 15/10/12.
 */
public class MonkeyrunnerConfigurable implements Configurable  {

    private final Project myProject;
    private OptionsConfiguration myState;
    private JTextArea jtaAddMKPath;
    private JTextArea jtaAddMkArgs;
    private JTextArea jtaAddScriptArgs;

    public MonkeyrunnerConfigurable(Project project) {
        myProject = project;
    }

    @Nls
    @Override
    public String getDisplayName() {
        return Res.string.settings_plugin;
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        myState = ServiceManager.getService(myProject, OptionsConfiguration.class);

        GridLayout layout = new GridLayout(3, 1, 6, 6);

        JPanel result = new JPanel(layout);

        jtaAddMKPath = new JTextArea(myState.monkeyrunnerPath);
        jtaAddMkArgs = new JTextArea(myState.monkeyrunnerArgs);
        jtaAddScriptArgs = new JTextArea(myState.monkeyrunnerScriptArgs);

        result.add(LabeledComponent.create(jtaAddMKPath, Res.string.settings_monkeyrunner_path));

        result.add(LabeledComponent.create(jtaAddMkArgs, Res.string.settings_monkeyrunner_args));

        result.add(LabeledComponent.create(jtaAddScriptArgs, Res.string.settings_py_script_args));

        return result;
    }

    @Override
    public boolean isModified() {
        return !jtaAddMKPath.getText().equals(myState.monkeyrunnerPath)
            || !jtaAddMkArgs.getText().equals(myState.monkeyrunnerArgs)
            || !jtaAddScriptArgs.getText().equals(myState.monkeyrunnerScriptArgs);
    }

    @Override
    public void apply() throws ConfigurationException {
        OptionsConfiguration state = new OptionsConfiguration();
        state.monkeyrunnerPath = jtaAddMKPath.getText().trim();
        state.monkeyrunnerArgs = jtaAddMkArgs.getText().trim();
        state.monkeyrunnerScriptArgs = jtaAddScriptArgs.getText().trim();
        ServiceManager.getService(myProject, OptionsConfiguration.class).loadState(state);
    }

    @Override
    public void reset() {
        jtaAddMKPath.setText(myState.monkeyrunnerPath);
        jtaAddMkArgs.setText(myState.monkeyrunnerArgs);
        jtaAddScriptArgs.setText(myState.monkeyrunnerScriptArgs);
    }

    @Override
    public void disposeUIResources() {
    }
}