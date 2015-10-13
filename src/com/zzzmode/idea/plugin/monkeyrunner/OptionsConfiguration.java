package com.zzzmode.idea.plugin.monkeyrunner;

import com.intellij.openapi.components.*;
import org.jetbrains.annotations.*;

/**
 * Created by zl on 15/10/12.
 */
@State(name = "MonkeyrunnerPluginSettings", storages = {@Storage(file = StoragePathMacros.PROJECT_CONFIG_DIR + "/monkeyrunner_cfg.xml")})
public class OptionsConfiguration implements PersistentStateComponent<OptionsConfiguration> {

    public String monkeyrunnerPath = "";
    public String monkeyrunnerArgs = "";
    public String monkeyrunnerScriptArgs = "";

    @NotNull
    @Override
    public OptionsConfiguration getState() {
        return this;
    }

    @Override
    public void loadState(OptionsConfiguration state) {
        monkeyrunnerPath = state.monkeyrunnerPath;
        monkeyrunnerArgs = state.monkeyrunnerArgs;
        monkeyrunnerScriptArgs = state.monkeyrunnerScriptArgs;
    }
}