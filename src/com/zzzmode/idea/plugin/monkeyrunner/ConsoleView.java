package com.zzzmode.idea.plugin.monkeyrunner;

import com.intellij.execution.console.*;
import com.intellij.execution.process.*;
import com.intellij.lang.*;
import com.intellij.openapi.project.*;

/**
 * Created by zl on 15/10/12.
 */
public class ConsoleView extends LanguageConsoleImpl {
    public ConsoleView(Project project) {

        super(project, Res.string.console_label, Language.findLanguageByID("Python"));
    }


    @Override
    public void attachToProcess(ProcessHandler processHandler) {
        super.attachToProcess(processHandler);
        try {
            processHandler.getProcessInput().write("run -->> ".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}