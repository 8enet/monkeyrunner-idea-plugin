package com.zzzmode.idea.plugin.monkeyrunner;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.components.*;
import com.intellij.openapi.fileEditor.*;
import com.intellij.openapi.project.*;
import com.intellij.openapi.ui.*;
import com.intellij.openapi.util.*;
import com.intellij.openapi.vfs.*;

import javax.swing.*;
import java.io.*;

/**
 * Created by zl on 15/10/12.
 */
public class MonkeyrunnerRunAction extends AnAction {

    public static final Icon sMonkeyrunnerIcon = IconLoader.getIcon("/android_logo.png");

    private static OptionsConfiguration sConfiguration;

    @Override
    public void actionPerformed(AnActionEvent e) {
        FileDocumentManager.getInstance().saveAllDocuments();
        final Project project = e.getData(PlatformDataKeys.PROJECT);
        assert project != null;

        final OptionsConfiguration configuration = ServiceManager.getService(project, OptionsConfiguration.class);
        if(configuration == null || Utils.isEmpty(configuration.monkeyrunnerPath)){
            Messages.showMessageDialog(Res.string.msg_no_monkeyrunner,Res.string.msg_title,Messages.getErrorIcon());
            return;
        }


        final VirtualFile data = e.getData(PlatformDataKeys.VIRTUAL_FILE);
        if (data != null) {
            String f = data.getPath();
            System.out.println(f);
            try {
                ConsoleRunner consoleRunner = new ConsoleRunner(project, f);
                consoleRunner.initAndRun();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }


    @Override
    public void update(AnActionEvent e) {
        Presentation presentation = e.getPresentation();
        presentation.setIcon(sMonkeyrunnerIcon);
        final VirtualFile data = e.getData(PlatformDataKeys.VIRTUAL_FILE);
        presentation.setEnabledAndVisible(data != null && verifyFile(data));

        final Project project = e.getData(PlatformDataKeys.PROJECT);
        assert project != null;

        if(sConfiguration == null){
            sConfiguration = ServiceManager.getService(project, OptionsConfiguration.class);
        }

        if(sConfiguration == null || Utils.isEmpty(sConfiguration.monkeyrunnerPath)){
            String path = getDefaultMonkeyrunnerPath(Res.array.unixWhich);
            if(path == null){
                path=getDefaultMonkeyrunnerPath(Res.array.winWhere);
            }
            if(path != null){
                OptionsConfiguration configuration=new OptionsConfiguration();
                configuration.monkeyrunnerPath=path;
                sConfiguration=configuration;
                ServiceManager.getService(project, OptionsConfiguration.class).loadState(configuration);
            }
        }
    }

    private static String getDefaultMonkeyrunnerPath(String[] cmds){
        Process exec = null;
        BufferedReader reader=null;
        try {
            exec = Runtime.getRuntime().exec(cmds);
            reader=new BufferedReader(new InputStreamReader(exec.getInputStream()));
            return  reader.readLine();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try{
                if(reader != null)
                    reader.close();
            }catch (Exception e){
                e.printStackTrace();
            }
            if (exec != null)
                exec.destroy();
        }
        return null;
    }

    private boolean verifyFile(VirtualFile file) {
        return "py".equalsIgnoreCase(file.getFileType().getDefaultExtension());
    }

}
