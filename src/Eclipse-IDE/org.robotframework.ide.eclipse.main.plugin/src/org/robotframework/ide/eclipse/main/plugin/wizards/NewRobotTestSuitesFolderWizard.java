/*
 * Copyright 2015 Nokia Solutions and Networks
 * Licensed under the Apache License, Version 2.0,
 * see license.txt file for details.
 */
package org.robotframework.ide.eclipse.main.plugin.wizards;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;
import org.robotframework.ide.eclipse.main.plugin.model.RobotSuiteFile;
import org.robotframework.ide.eclipse.main.plugin.tableeditor.RobotFormEditor;

public class NewRobotTestSuitesFolderWizard extends BasicNewResourceWizard {

    private WizardNewRobotFolderMainPage mainPage;

    @Override
    public void init(final IWorkbench workbench, final IStructuredSelection currentSelection) {
        super.init(workbench, currentSelection);
        setNeedsProgressMonitor(true);
        setWindowTitle("New Robot suites folder");
    }

    @Override
    public void addPages() {
        super.addPages();

        mainPage = new WizardNewRobotFolderMainPage("New Robot suites Folder", getSelection(), "robot", "txt", "tsv");
        mainPage.setWizard(this);
        mainPage.setTitle("Robot Suites Folder");
        mainPage.setDescription("Create new Robot suites folder");

        this.addPage(mainPage);
    }

    @Override
    public boolean performFinish() {
        final IFolder newFolder = mainPage.createNewFolder();
        selectAndReveal(newFolder);

        if (mainPage.shouldInitFileBeCreated()) {
            try {
                final IFile initFile = RobotSuiteFile.createRobotInitializationFile(newFolder,
                        mainPage.getInitFileExtension());
                selectAndReveal(initFile);
                if (initFile.exists()) {
                    RobotFormEditor.tryToOpen(initFile,
                            PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage());
                }
            } catch (final CoreException e) {
                throw new SuiteCreatingException("Unable to create suites directory " + newFolder.getName(), e);
            }
        }

        return true;
    }

    @SuppressWarnings("serial")
    private static class SuiteCreatingException extends RuntimeException {

        public SuiteCreatingException(final String msg, final CoreException cause) {
            super(msg, cause);
        }
    }
}
