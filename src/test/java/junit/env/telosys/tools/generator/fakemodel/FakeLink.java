/**
 *  Copyright (C) 2008-2015  Telosys project org. ( http://www.telosys.org/ )
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

import java.util.List;

import org.telosys.tools.generic.model.CascadeOptions;
import org.telosys.tools.generic.model.Link;
import org.telosys.tools.generic.model.LinkAttribute;
import org.telosys.tools.generic.model.TagContainer;
import org.telosys.tools.generic.model.enums.BooleanValue;
import org.telosys.tools.generic.model.enums.Cardinality;
import org.telosys.tools.generic.model.enums.FetchType;
import org.telosys.tools.generic.model.enums.Optional;

/**
 * Fake attribute for fake entity <br>
 * 
 * @author Laurent Guerin
 *
 */
public class FakeLink implements Link {

    private final String      fieldName; 
    private final String      targetEntityClassName;
    private final Cardinality cardinality ;

    private List<LinkAttribute> linkAttributes;  // v 3.4.0
    
    private boolean owningSide;
    private String mappedBy;
    
    private boolean inverseSide;
    
    private Optional       optional    = Optional.UNDEFINED;
    private FetchType      fetchType   = FetchType.UNDEFINED;
    private CascadeOptions cascadeOptions = new CascadeOptions();
    
    private boolean basedOnForeignKey;
    private String foreignKeyName;
    
    private boolean isBasedOnJoinEntity = false;
    private String joinEntityName = null;
    
    private boolean isEmbedded = false ;
    private boolean isTransient = false ;
    
    private BooleanValue insertable = BooleanValue.UNDEFINED; // Added in v 3.3.0
    private BooleanValue updatable  = BooleanValue.UNDEFINED; // Added in v 3.3.0
    
	private TagContainer tagContainer = new FakeTagContainer();
	
	private boolean orphanRemoval = false;

    /**
     * Constructor
     * @param fieldName
     * @param targetEntityClassName
     * @param cardinality
     */
    public FakeLink(String fieldName, String targetEntityClassName, Cardinality cardinality) {
		super();
		this.fieldName = fieldName ;
		this.targetEntityClassName = targetEntityClassName;
		this.cardinality = cardinality ;
		if ( cardinality == Cardinality.MANY_TO_ONE ) {
			this.owningSide = true ;
			this.inverseSide = false ;
		}
		else {
			this.owningSide = false ;
			this.inverseSide = true ;
		}
	}

    @Override
    public String getReferencedEntityName() {
        return targetEntityClassName;
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }
    
    @Override
    public boolean isOwningSide() {
        return owningSide;
    }

    public void setOwningSide(boolean owningSide) {
        this.owningSide = owningSide;
    }

    @Override
    public String getMappedBy() {
        return mappedBy;
    }

    public void setMappedBy(String mappedBy) {
        this.mappedBy = mappedBy;
    }

    @Override
    public boolean isSelected() {
        return true; // Always "TRUE" ( "SELECTED" )
    }

    @Override
    public boolean isInverseSide() {
        return inverseSide;
    }
    public void setInverseSide(boolean inverseSide) {
        this.inverseSide = inverseSide;
    }

    @Override
    public Optional getOptional() {
        return optional;
    }
    public void setOptional(Optional optional) {
        this.optional = optional;
    }

    @Override
    public Cardinality getCardinality() {
        return cardinality;
    }

    @Override
    public CascadeOptions getCascadeOptions() {
        return cascadeOptions;
    }

    public void setCascadeOptions(CascadeOptions cascadeOptions) {
        this.cascadeOptions = cascadeOptions;
    }

    @Override
    public FetchType getFetchType() {
        return fetchType;
    }

    public void setFetchType(FetchType fetchType) {
        this.fetchType = fetchType;
    }

    @Override
    public boolean isBasedOnForeignKey() {
        return basedOnForeignKey;
    }

    public void setBasedOnForeignKey(boolean basedOnForeignKey) {
        this.basedOnForeignKey = basedOnForeignKey;
    }

    @Override
    public String getForeignKeyName() {
        return foreignKeyName;
    }

    public void setForeignKeyName(String foreignKeyName) {
        this.foreignKeyName = foreignKeyName;
    }

    @Override
    public boolean isEmbedded() {
        return this.isEmbedded;
    }
    public void setEmbedded(boolean b) {
        this.isEmbedded = b;
    }

    @Override
    public boolean isTransient() { // v 3.3.0
        return this.isTransient;
    }
    public void setTransient(boolean b) { // v 3.3.0
        this.isTransient = b;
    }

    @Override
    public BooleanValue getInsertable() {
        return this.insertable;
    }
    public void setInsertable(BooleanValue b) {
        this.insertable = b;
    }

    @Override
    public BooleanValue getUpdatable() {
        return this.updatable;
    }
    public void setUpdatable(BooleanValue b) {
        this.updatable = b;
    }

	@Override
	public TagContainer getTagContainer() { // v 3.4.0
		return tagContainer;
	}

	@Override
	public List<LinkAttribute> getAttributes() {
		return this.linkAttributes;
	}

	@Override
	public String getJoinEntityName() {
		return joinEntityName;
	}

	@Override
	public boolean isBasedOnJoinEntity() {
		return isBasedOnJoinEntity;
	}

	@Override
	public boolean isOrphanRemoval() {
		return orphanRemoval;
	}
	public void setOrphanRemoval(boolean v) {
		this.orphanRemoval = v;
	}
}
