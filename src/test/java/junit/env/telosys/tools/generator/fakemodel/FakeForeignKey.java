/**
 *  Copyright (C) 2008-2017  Telosys project org. ( http://www.telosys.org/ )
 *
 *  Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.gnu.org/licenses/lgpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package junit.env.telosys.tools.generator.fakemodel;

import java.util.LinkedList;
import java.util.List;

import org.telosys.tools.generic.model.ForeignKey;
import org.telosys.tools.generic.model.ForeignKeyAttribute;

public class FakeForeignKey implements ForeignKey {
	
    private final String fkName; 
//    private final String tableName; // table holding this FK
//    private final String referencedTableName; // table referenced by this FK
    
    private final String originEntityName;
    private final String referencedEntityName;
    
//    private final List<ForeignKeyColumn> columns;
    private final List<ForeignKeyAttribute> attributes;
    
//    private String deferrable;
//    private int deferrableCode;
//    private String deleteRule;
//    private int deleteRuleCode;
//    private String updateRule;
//    private int updateRuleCode;

    
//    public FakeForeignKey(String fkName, String tableName, String referencedTableName) {
    public FakeForeignKey(String fkName, String originEntityName, String referencedEntityName) {
		super();
        this.fkName = fkName;
//        this.tableName = tableName;
//        this.referencedTableName = referencedTableName;
        this.originEntityName = originEntityName ;
        this.referencedEntityName = referencedEntityName ;
        
        //this.columns = new LinkedList<>();
        this.attributes = new LinkedList<>();
	}

	@Override
    public String getName() {
        return fkName;
    }
	@Override
    public String getOriginEntityName() {
        return originEntityName;
    }
	@Override
    public String getReferencedEntityName() {
        return referencedEntityName;
    }
    @Override
    public List<ForeignKeyAttribute> getAttributes() {
        return attributes;
    }
    public void addAttribute(ForeignKeyAttribute fkAttribute) {
    	// fkAttribute has always valid attributes (not null & not void)
        this.attributes.add(fkAttribute);
    }
    @Override
    public boolean isComposite() {
    	return this.attributes.size() > 1 ;
    }
//    @Override
//    public String getTableName() {
//        return tableName;
//    }
//
//    @Override
//    public String getReferencedTableName() {
//        return referencedTableName;
//    }
//
//    @Override
//    public List<ForeignKeyColumn> getColumns() {
//        return columns;
//    }
//
//    public void addColumn(ForeignKeyColumn fkCol) {
//        this.columns.add(fkCol);
//    }
//
//    @Override
//    public String getDeferrable() {
//        return deferrable;
//    }
//
//    public void setDeferrable(String deferrable) {
//        this.deferrable = deferrable;
//    }
//
//    @Override
//    public int getDeferrableCode() {
//        return deferrableCode;
//    }
//
//    public void setDeferrableCode(int deferrableCode) {
//        this.deferrableCode = deferrableCode;
//    }
//
//    @Override
//    public String getDeleteRule() {
//        return deleteRule;
//    }
//
//    public void setDeleteRule(String deleteRule) {
//        this.deleteRule = deleteRule;
//    }
//
//    @Override
//    public int getDeleteRuleCode() {
//        return deleteRuleCode;
//    }
//
//    public void setDeleteRuleCode(int deleteRuleCode) {
//        this.deleteRuleCode = deleteRuleCode;
//    }
//
//    @Override
//    public String getUpdateRule() {
//        return updateRule;
//    }
//
//    public void setUpdateRule(String updateRule) {
//        this.updateRule = updateRule;
//    }
//
//    @Override
//    public int getUpdateRuleCode() {
//        return updateRuleCode;
//    }
//
//    public void setUpdateRuleCode(int updateRuleCode) {
//        this.updateRuleCode = updateRuleCode;
//    }
}
