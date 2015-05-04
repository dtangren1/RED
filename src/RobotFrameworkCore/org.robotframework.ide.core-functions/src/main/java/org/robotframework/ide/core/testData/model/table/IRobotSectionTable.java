package org.robotframework.ide.core.testData.model.table;

import org.robotframework.ide.core.testData.model.common.IOptional;
import org.robotframework.ide.core.testData.parser.IDataLocator;
import org.robotframework.ide.core.testData.parser.IParsePositionMarkable;


/**
 * Common interface for all table sections including: Test Case, Settings and
 * etc.
 * 
 * @author wypych
 * @serial RobotFramework 2.8.6
 * @serial 1.0
 * 
 * @see TestCaseTable
 * @see KeywordTable
 * @see SettingTable
 * @see VariablesTable
 */
public interface IRobotSectionTable extends IOptional {

    /**
     * enable this table in model
     */
    void setPresent();


    /**
     * disable this table in model
     */
    void unsetPresent();


    /**
     * @return position of table inside Robot File
     */
    IDataLocator<? extends IParsePositionMarkable> getTableSectionPosition();
}
