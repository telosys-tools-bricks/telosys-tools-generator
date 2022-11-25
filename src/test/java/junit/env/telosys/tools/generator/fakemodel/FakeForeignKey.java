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
    private final String originEntityName;
    private final String referencedEntityName;
    private final List<ForeignKeyAttribute> attributes;
    
    public FakeForeignKey(String fkName, String originEntityName, String referencedEntityName) {
		super();
        this.fkName = fkName;
        this.originEntityName = originEntityName ;
        this.referencedEntityName = referencedEntityName ;        
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

	@Override
	public boolean isExplicit() {
		return true;
	}

}
