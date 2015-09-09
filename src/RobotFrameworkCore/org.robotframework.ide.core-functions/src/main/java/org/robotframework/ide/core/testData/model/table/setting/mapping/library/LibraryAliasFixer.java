/*
 * Copyright 2015 Nokia Solutions and Networks
 * Licensed under the Apache License, Version 2.0,
 * see license.txt file for details.
 */
package org.robotframework.ide.core.testData.model.table.setting.mapping.library;

import java.util.List;
import java.util.Stack;

import org.robotframework.ide.core.testData.model.RobotFileOutput;
import org.robotframework.ide.core.testData.model.table.mapping.ElementsUtility;
import org.robotframework.ide.core.testData.model.table.setting.LibraryAlias;
import org.robotframework.ide.core.testData.model.table.setting.LibraryImport;
import org.robotframework.ide.core.testData.text.read.ParsingState;
import org.robotframework.ide.core.testData.text.read.recognizer.ATokenRecognizer;
import org.robotframework.ide.core.testData.text.read.recognizer.RobotToken;
import org.robotframework.ide.core.testData.text.read.recognizer.RobotTokenType;
import org.robotframework.ide.core.testData.text.read.recognizer.settings.LibraryAliasRecognizer;

import com.google.common.annotations.VisibleForTesting;


public class LibraryAliasFixer {

    private final ElementsUtility utility;


    public LibraryAliasFixer(final ElementsUtility utility) {
        this.utility = utility;
    }


    public void checkAndFixLine(final RobotFileOutput robotFileOutput,
            final Stack<ParsingState> processingState) {
        ParsingState state = utility
                .findNearestNotCommentState(processingState);
        if (state == ParsingState.SETTING_LIBRARY_IMPORT_ALIAS) {
            LibraryImport lib = utility
                    .findNearestLibraryImport(robotFileOutput);

            applyFixes(robotFileOutput, lib, null, processingState);
        } else if (state == ParsingState.SETTING_LIBRARY_ARGUMENTS) {
            LibraryImport lib = utility
                    .findNearestLibraryImport(robotFileOutput);

            List<RobotToken> arguments = lib.getArguments();
            int argumentsSize = arguments.size();
            if (argumentsSize >= 2) {
                RobotToken argumentPossibleAlias = arguments
                        .get(argumentsSize - 2);
                ATokenRecognizer rec = new LibraryAliasRecognizer();
                if (rec.hasNext(argumentPossibleAlias.getText(),
                        argumentPossibleAlias.getLineNumber())) {
                    argumentPossibleAlias
                            .setType(RobotTokenType.SETTING_LIBRARY_ALIAS);
                    LibraryAlias alias = new LibraryAlias(argumentPossibleAlias);
                    RobotToken aliasValue = arguments.get(argumentsSize - 1);
                    aliasValue
                            .setType(RobotTokenType.SETTING_LIBRARY_ALIAS_VALUE);
                    alias.setLibraryAlias(aliasValue);

                    lib.setAlias(alias);
                    arguments.remove(argumentsSize - 1);
                    arguments.remove(argumentsSize - 2);
                    replaceArgumentsByAliasDeclaration(processingState);
                }
            }
        }
    }


    @VisibleForTesting
    protected void replaceArgumentsByAliasDeclaration(
            final Stack<ParsingState> processingState) {
        int removedArguments = 0;
        for (int i = processingState.size() - 1; i >= 0; i--) {
            ParsingState state = processingState.get(i);
            if (state == ParsingState.SETTING_LIBRARY_ARGUMENTS) {
                if (removedArguments == 0) {
                    // it is value
                    processingState.set(i,
                            ParsingState.SETTING_LIBRARY_IMPORT_ALIAS_VALUE);
                    removedArguments++;
                } else if (removedArguments == 1) {
                    // it is alias
                    processingState.set(i,
                            ParsingState.SETTING_LIBRARY_IMPORT_ALIAS);
                    break;
                }
            }
        }
    }


    public void applyFixes(final RobotFileOutput robotFileOutput,
            final LibraryImport lib, final RobotToken additionalToken,
            final Stack<ParsingState> processingState) {
        LibraryAlias alias = lib.getAlias();
        if (additionalToken == null) {
            // end of line
            if (alias.isPresent()) {
                if (alias.getLibraryAlias() == null) {
                    // FIXME: add info that WITH NAME do not have any aliases so
                    // will be get as argument
                    RobotToken aliasToken = alias.getLibraryAliasDeclaration();
                    aliasToken.setType(RobotTokenType.SETTING_LIBRARY_ARGUMENT);
                    lib.getArguments().add(aliasToken);
                    lib.setAlias(new LibraryAlias(null));
                    removeLibraryAliasState(processingState);
                }
            }
        } else {
            // case when we check if we have alias value
            if (alias.isPresent()) {
                RobotToken libraryAlias = alias.getLibraryAlias();
                ATokenRecognizer rec = new LibraryAliasRecognizer();
                if (rec.hasNext(libraryAlias.getText(),
                        libraryAlias.getLineNumber())) {
                    // alias value has WITH NAME case and we have additional
                    // argument case: WITH NAME (lib argument) WITH NAME p
                    RobotToken aliasDeclared = alias
                            .getLibraryAliasDeclaration();
                    aliasDeclared
                            .setType(RobotTokenType.SETTING_LIBRARY_ARGUMENT);
                    lib.addArgument(aliasDeclared);
                    libraryAlias.setType(RobotTokenType.SETTING_LIBRARY_ALIAS);
                    LibraryAlias correctedAlias = new LibraryAlias(libraryAlias);
                    additionalToken
                            .setType(RobotTokenType.SETTING_LIBRARY_ALIAS_VALUE);
                    correctedAlias.setLibraryAlias(additionalToken);
                    lib.setAlias(correctedAlias);
                } else {
                    // case when we have already correct alias and we get
                    // additional value for alias
                    RobotToken libraryAliasDeclaration = alias
                            .getLibraryAliasDeclaration();
                    libraryAliasDeclaration
                            .setType(RobotTokenType.SETTING_LIBRARY_ARGUMENT);
                    lib.addArgument(libraryAliasDeclaration);
                    libraryAlias
                            .setType(RobotTokenType.SETTING_LIBRARY_ARGUMENT);
                    lib.addArgument(libraryAlias);
                    additionalToken
                            .setType(RobotTokenType.SETTING_LIBRARY_ARGUMENT);
                    lib.addArgument(additionalToken);
                    cleanAliasAndValueFromState(processingState);
                    processingState
                            .push(ParsingState.SETTING_LIBRARY_ARGUMENTS);
                }
            }
        }
    }


    @VisibleForTesting
    protected void cleanAliasAndValueFromState(
            final Stack<ParsingState> processingState) {
        removeLibraryAliasValueState(processingState);
        removeLibraryAliasState(processingState);
    }


    @VisibleForTesting
    protected void removeLibraryAliasValueState(
            Stack<ParsingState> processingState) {
        for (int i = processingState.size() - 1; i >= 0; i--) {
            ParsingState state = processingState.get(i);
            if (state == ParsingState.SETTING_LIBRARY_IMPORT_ALIAS_VALUE) {
                processingState.remove(i);
            } else {
                break;
            }
        }
    }


    @VisibleForTesting
    protected void removeLibraryAliasState(Stack<ParsingState> processingState) {
        for (int i = processingState.size() - 1; i >= 0; i--) {
            ParsingState state = processingState.get(i);
            if (state == ParsingState.SETTING_LIBRARY_IMPORT_ALIAS) {
                processingState.remove(i);
            } else {
                break;
            }
        }
    }
}
