package com.zzzmode.idea.plugin.monkeyrunner;

import com.intellij.execution.*;
import com.intellij.execution.configurations.*;
import com.intellij.execution.console.*;
import com.intellij.execution.process.*;
import com.intellij.execution.runners.*;
import com.intellij.openapi.components.*;
import com.intellij.openapi.project.*;
import com.intellij.openapi.util.*;
import com.intellij.openapi.vfs.*;
import org.jetbrains.annotations.*;

/**
 * Created by zl on 15/10/12.
 */
public class ConsoleRunner extends AbstractConsoleRunnerWithHistory<ConsoleView> {

    private static final Key<Boolean> MONKEYRUNNER_SHELL = Key.create("MONKEYRUNNER_SHELL");

    private String currFile;

    public ConsoleRunner(@NotNull Project project, String currFile) {
        super(project, Res.string.console_title, "/tmp");
        this.currFile=currFile;
    }

    @Override
    protected ConsoleView createConsoleView() {
        ConsoleView res = new ConsoleView(getProject());
        VirtualFile file = res.getConsoleEditor().getVirtualFile();
        if(file != null)
        file.putUserData(MONKEYRUNNER_SHELL, Boolean.TRUE);

        return res;
    }

    @Nullable
    @Override
    protected Process createProcess() throws ExecutionException {
        OptionsConfiguration options = ServiceManager.getService(getProject(), OptionsConfiguration.class);

        final GeneralCommandLine commandLine = new GeneralCommandLine();
        commandLine.setExePath(options.monkeyrunnerPath);
        if(!Utils.isEmpty(options.monkeyrunnerArgs)){
            commandLine.addParameter(options.monkeyrunnerArgs);
        }
        commandLine.addParameter(currFile);

        if(!Utils.isEmpty(options.monkeyrunnerScriptArgs)){
            commandLine.addParameter(options.monkeyrunnerScriptArgs);
        }

        return commandLine.createProcess();
    }

    @Override
    protected OSProcessHandler createProcessHandler(Process process) {
        return new OSProcessHandler(process, null);
    }

    @NotNull
    @Override
    protected ProcessBackedConsoleExecuteActionHandler createExecuteActionHandler() {
        return new ProcessBackedConsoleExecuteActionHandler(getProcessHandler(), false);
    }

}