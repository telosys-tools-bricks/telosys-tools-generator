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
package org.telosys.tools.generator.context;

import java.util.LinkedList;
import java.util.List;

import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.generator.GeneratorException;
import org.telosys.tools.generator.context.doc.VelocityMethod;
import org.telosys.tools.generator.context.doc.VelocityNoDoc;
import org.telosys.tools.generator.context.doc.VelocityObject;
import org.telosys.tools.generator.context.doc.VelocityReturnType;
import org.telosys.tools.generator.context.names.ContextName;
import org.telosys.tools.generator.context.tools.AnnotationsBuilder;
import org.telosys.tools.generator.context.tools.JpaAnnotations;
import org.telosys.tools.generator.context.tools.ListBuilder;
import org.telosys.tools.generator.context.tools.SqlTableNameProvider;
import org.telosys.tools.generic.model.enums.BooleanValue;
import org.telosys.tools.generic.model.enums.FetchType;

//-------------------------------------------------------------------------------------
@VelocityObject(
		contextName=ContextName.JPA,
		text = { 
				"Object providing a set of utility functions for JPA (Java Persistence API) code generation",
				""
		},
		since = "2.0.7"
 )
//-------------------------------------------------------------------------------------
public class JpaInContext {

	private boolean   genTargetEntity = false ; // v 3.3.0
	private String    collectionType  = "List" ; // next ver after v 3.3.0
	
	private FetchType linkManyToOneFetchType  = FetchType.UNDEFINED; // v 3.3.0
	private FetchType linkOneToOneFetchType   = FetchType.UNDEFINED; // v 3.3.0
	private FetchType linkOneToManyFetchType  = FetchType.UNDEFINED; // v 3.3.0
	private FetchType linkManyToManyFetchType = FetchType.UNDEFINED; // v 3.3.0
	
	private BooleanValue joinColumnInsertable = BooleanValue.UNDEFINED; // v 3.3.0
	private BooleanValue joinColumnUpdatable  = BooleanValue.UNDEFINED; // v 3.3.0
	
	private boolean  genColumnDefinition = false ; // v 3.4.0
	
	//-------------------------------------------------------------------------------------
	// CONSTRUCTOR
	//-------------------------------------------------------------------------------------
	public JpaInContext() {
		super();
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
		"Defines if 'targetEntity' must be generated in @ManyToMany, @OneToMany, etc",
		"Can be set to 'true' or 'false' (default value is 'false') "
		},
	example={ 
		"#set( $jpa.genTargetEntity = true )"
		},
	since = "3.3.0"
		)
	public boolean getGenTargetEntity() {
		return genTargetEntity;
	}

	@VelocityNoDoc // just the setter for 'genTargetEntity'
	public void setGenTargetEntity(boolean v) {
		this.genTargetEntity = v;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
		"Defines if 'columnDefinition' must be generated in 'Column' annotation",
		"Can be set to 'true' or 'false' (default value is 'false') "
		},
	example={ 
		"#set( $jpa.genColumnDefinition = true )"
		},
	since = "3.4.0"
		)
	public boolean getGenColumnDefinition() { // v 3.4.0
		return genColumnDefinition;
	}

	@VelocityNoDoc // just the setter 
	public void setGenColumnDefinition(boolean v) { // v 3.4.0
		this.genColumnDefinition = v;
	}

	//-------------------------------------------------------------------------------------
	@VelocityNoDoc  // for future use  ( currently $env.collectionType is used )
	public String getCollectionType() {
		return collectionType;
	}
	@VelocityNoDoc  // for future use ( currently $env.collectionType is used )
	public void setCollectionType(String v) {
		this.collectionType = v;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
		"Defines default FETCH-TYPE ('LAZY' or 'EAGER') for 'ManyToOne' cardinality "
		},
	example={ 
		"#set( $jpa.manyToOneFetchType = 'LAZY' )"
		},
	since = "3.3.0"
		)
	public FetchType getManyToOneFetchType() {
		return this.linkManyToOneFetchType;
	}
	@VelocityNoDoc // just the setter 
	public void setManyToOneFetchType(String s) {
		this.linkManyToOneFetchType = getFetchType(s);
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
		"Defines default FETCH-TYPE ('LAZY' or 'EAGER') for 'OneToOne' cardinality "
		},
	example={ 
		"#set( $jpa.oneToOneFetchType = 'LAZY' )"
		},
	since = "3.3.0"
		)
	public FetchType getOneToOneFetchType() {
		return this.linkOneToOneFetchType;
	}
	@VelocityNoDoc // just the setter 
	public void setOneToOneFetchType(String s) {
		this.linkOneToOneFetchType = getFetchType(s);
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
		"Defines default FETCH-TYPE ('LAZY' or 'EAGER') for 'OneToMany' cardinality "
		},
	example={ 
		"#set( $jpa.oneToManyFetchType = 'EAGER' )"
		},
	since = "3.3.0"
		)
	public FetchType getOneToManyFetchType() {
		return this.linkOneToManyFetchType;
	}
	@VelocityNoDoc // just the setter 
	public void setOneToManyFetchType(String s) {
		this.linkOneToManyFetchType = getFetchType(s);
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
		"Defines default FETCH-TYPE ('LAZY' or 'EAGER') for 'ManyToMany' cardinality "
		},
	example={ 
		"#set( $jpa.manyToManyFetchType = 'EAGER' )"
		},
	since = "3.3.0"
		)
	public FetchType getManyToManyFetchType() {
		return this.linkManyToManyFetchType;
	}
	@VelocityNoDoc // just the setter 
	public void setManyToManyFetchType(String s) {
		this.linkManyToManyFetchType = getFetchType(s);
	}

	//-------------------------------------------------------------------------------------
	private FetchType getFetchType(String s) {
		if ( ! StrUtil.nullOrVoid(s) ) {
			String ft = s.toUpperCase();
			if ("EAGER".equals(ft) ) {
				return FetchType.EAGER; 
			}
			if ("LAZY".equals(ft) ) {
				return FetchType.LAZY; 
			}
		}
		// Invalid value 
		return FetchType.UNDEFINED;
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
		"Defines the value for 'insertable' attribute in '@JoinColumn' annotation",
		"Can be set to 'true' or 'false' (default value is 'nothing') "
		},
	example={ 
		"#set( $jpa.joinColumnInsertable = true )"
		},
	since = "3.3.0"
		)
	public String getJoinColumnInsertable() {
		return this.joinColumnInsertable.getText() ;
	}
	@VelocityNoDoc // just the setter
	public void setJoinColumnInsertable(String s) {
		this.joinColumnInsertable = getBooleanValue(s);
	}
	@VelocityNoDoc // just the setter
	public void setJoinColumnInsertable(boolean b) {
		this.joinColumnInsertable = getBooleanValue(b);
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
	text={	
		"Defines the value for 'updatable' attribute in '@JoinColumn' annotation",
		"Can be set to 'true' or 'false' (default value is 'nothing') "
		},
	example={ 
		"#set( $jpa.joinColumnUpdatable = true )"
		},
	since = "3.3.0"
		)
	public String getJoinColumnUpdatable() {
		return this.joinColumnUpdatable.getText() ;
	}
	@VelocityNoDoc // just the setter
	public void setJoinColumnUpdatable(String s) {
		this.joinColumnUpdatable = getBooleanValue(s);
	}
	@VelocityNoDoc // just the setter
	public void setJoinColumnUpdatable(boolean b) {
		this.joinColumnUpdatable = getBooleanValue(b);
	}

	private BooleanValue getBooleanValue(String s) {
		if ( ! StrUtil.nullOrVoid(s) ) {
			String ft = s.toUpperCase();
			if ("TRUE".equals(ft) ) {
				return BooleanValue.TRUE; 
			}
			if ("FALSE".equals(ft) ) {
				return BooleanValue.FALSE; 
			}
		}
		// Invalid value 
		return BooleanValue.UNDEFINED;
	}
	private BooleanValue getBooleanValue(boolean b) {
		if ( b ) {
			return BooleanValue.TRUE; 
		}
		else {
			return BooleanValue.FALSE; 
		}
	}

	//-------------------------------------------------------------------------------------
	// JPA IMPORTS
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( 
		text= { 
			"Returns a list of all the Java classes required by the current entity for JPA",
			"( this version always returns 'javax.persistence.*' )"
		},
		parameters = {
			"entity : the entity "
		},
		example={	
			"#foreach( $import in $jpa.imports($entity) )",
			"import $import;",
			"#end" 
		},
		since = "2.0.7"
	)
	@VelocityReturnType("List of 'String'")
	public List<String> imports(EntityInContext entity) {
		List<String> list = new LinkedList<>();
		list.add("javax.persistence.*");
		return list;
	}
	
	//-------------------------------------------------------------------------------------
	// ENTITY JPA ANNOTATIONS
	//-------------------------------------------------------------------------------------
	@VelocityMethod ( 
		text= { 
			"Returns a multiline String containing all the Java JPA annotations required for the current entity",
			"with the given left marging before each line"
		},
		parameters = {
			"leftMargin : the left margin (number of blanks)",
			"entity : the entity to be annotated"
		},
		example={	
			"$jpa.entityAnnotations(4, $entity)"
		},
		since = "2.0.7"
	)
	public String entityAnnotations(int iLeftMargin, EntityInContext entity)
    {
		AnnotationsBuilder b = new AnnotationsBuilder(iLeftMargin);
		
		b.addLine("@Entity");
		
		String s = "@Table(name=\"" + SqlTableNameProvider.getTableName(entity) + "\"" ;
		if ( ! StrUtil.nullOrVoid( entity.getDatabaseSchema() ) ) {
			s = s + ", schema=\"" + entity.getDatabaseSchema() + "\"" ; 
		}
		if ( ! StrUtil.nullOrVoid( entity.getDatabaseCatalog() ) ) {
			s = s + ", catalog=\"" + entity.getDatabaseCatalog() + "\"" ; 
		}
		s = s + " )" ;

		b.addLine(s);
		
		return b.getAnnotations();
    }
	
	//-------------------------------------------------------------------------------------
	// LINK JPA ANNOTATIONS
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns a string containing all the JPA annotations for the given link",
			"The list of mapped fields is used to determine if a JoinColumn is already mapped as a field",
			"If a JoinColumn is based on a field already mapped then 'insertable=false, updatable=false' is set"
			},
		example={ 
			"$jpa.linkAnnotations( 4, $link, $listOfMappedFields )" },
		parameters = { 
			"leftMargin : the left margin (number of blanks)",
			"link : the link to be annotated",
			"alreadyMappedFields : list of all the fields already mapped by JPA as 'simple fields' "},
		since = "2.0.7"
			)
	public String linkAnnotations( int leftMargin, LinkInContext link, List<AttributeInContext> alreadyMappedFields )
				throws GeneratorException {		
		AnnotationsBuilder annotations = new AnnotationsBuilder(leftMargin);
		processLinkCardinalityAnnotation(annotations, link) ;
		processLinkJoinAnnotation(annotations, link, alreadyMappedFields );
		return annotations.getAnnotations();
	}
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns a string containing all the JPA annotations for the given link",
			"@ManyToOne, @OneToMany, etc",
			"@JoinColumns / @JoinColumn "
			},
		example={ 
			"$jpa.linkAnnotations( 4, $link )" },
		parameters = { 
			"leftMargin : the left margin (number of blanks)",
			"link : the link from which the JPA annotations will be generated"},
		since = "3.3.0"
			)
	public String linkAnnotations( int leftMargin, LinkInContext link )
				throws GeneratorException {
		AnnotationsBuilder annotations = new AnnotationsBuilder(leftMargin);
		processLinkCardinalityAnnotation(annotations, link) ;
		processLinkJoinAnnotation(annotations, link, null );
		return annotations.getAnnotations();
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns a string containing the JPA cardinality annotation for the given link",
			"( e.g. @ManyToOne, @OneToMany, etc )"},
		example={ 
			"$jpa.linkCardinalityAnnotation( 4, $link )" },
		parameters = { 
			"leftMargin : the left margin (number of blanks)",
			"link : the link from which the JPA annotation will be generated"},
		since = "3.3.0"
			)
	public String linkCardinalityAnnotation(int leftMargin, LinkInContext link ) {
		AnnotationsBuilder annotations = new AnnotationsBuilder(leftMargin);
		processLinkCardinalityAnnotation(annotations, link) ;
		return annotations.getAnnotations();
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns a string containing the JPA JoinColumn(s) annotations for the given link",
			"The list of mapped fields is used to determine if a JoinColumn is already mapped as a field",
			"If a JoinColumn is based on a field already mapped then 'insertable=false, updatable=false' is set"
			},
		example={ 
			"$jpa.linkJoinAnnotation( 4, $link, $listOfMappedFields )" },
		parameters = { 
			"leftMargin : the left margin (number of blanks)",
			"link : the link from which the JPA annotation will be generated",
			"alreadyMappedFields : list of all the fields already mapped by JPA as 'simple fields' "},
		since = "3.3.0"
			)
	public String linkJoinAnnotation(int leftMargin, LinkInContext link, List<AttributeInContext> alreadyMappedFields ) throws GeneratorException {
		AnnotationsBuilder annotations = new AnnotationsBuilder(leftMargin);
		processLinkJoinAnnotation(annotations, link, alreadyMappedFields );
		return annotations.getAnnotations();
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns a string containing the JPA JoinColumn(s) annotations for the given link"},
		example={ 
			"$jpa.linkJoinAnnotation(4,$link)" },
		parameters = { 
			"leftMargin : the left margin (number of blanks)",
			"link : the link from which the JPA annotation will be generated"},
		since = "3.3.0"
			)
	public String linkJoinAnnotation(int leftMargin, LinkInContext link ) throws GeneratorException {
		AnnotationsBuilder annotations = new AnnotationsBuilder(leftMargin);
		processLinkJoinAnnotation(annotations, link, null );
		return annotations.getAnnotations();
	}
	
	private String buildCardinalityAnnotation( LinkInContext link ) {
		String cardinality = null;
		if ( link.isCardinalityOneToOne() ) {
			cardinality = "OneToOne"; 
		} 
		else if ( link.isCardinalityManyToOne() ) {
			cardinality = "ManyToOne"; 
		} 
		else if ( link.isCardinalityManyToMany() ) {
			cardinality = "ManyToMany"; 
		}
		else if ( link.isCardinalityOneToMany() ) {
			cardinality = "OneToMany"; 
		}
		if ( cardinality != null ) {
			StringBuilder sb = new StringBuilder();
			sb.append( "@" ) ;
			sb.append( cardinality ) ;
			String options = buildCardinalityFurtherInformation(link);
			if ( ! StrUtil.nullOrVoid(options) ) {
				sb.append( "(" );
				sb.append( options );
				sb.append( ")" );
			}
			return sb.toString();
		}
		return "";
	}
	//----------------------------------------------------------------
//	/**
//	 * Build an return the cardinality annotation for an "OWNING SIDE"
//	 * Example : "@ManyToOne ( cascade = CascadeType.ALL, fetch = FetchType.EAGER ) "
//	 * @param link
//	 * @param cardinality
//	 * @param targetEntityClassName the target entity ( or null if none ) 
//	 * @return
//	 */
//	private String buildCardinalityAnnotationForOwningSide( LinkInContext link, String cardinality, String targetEntityClassName ) 
//	{
//		StringBuilder sb = new StringBuilder();
//		sb.append( "@" + cardinality ) ;
//		//String options = buildCardinalityOptions(link, targetEntityClassName);
//		String options = buildCardinalityFurtherInformation(link);
//		if ( ! StrUtil.nullOrVoid(options) ) {
//			sb.append( "(" );
//			sb.append( options );
//			sb.append( ")" );
//		}
//		return sb.toString();
//	}
	
	//-------------------------------------------------------------------------------------
//	private String buildCardinalityOptions(LinkInContext link, String targetEntityClassName) {
//		boolean notVoid = false ;
//		StringBuilder sb = new StringBuilder();
//		//--- Common further information : cascade, fetch and optional
//		// ie "cascade = CascadeType.ALL, fetch = FetchType.EAGER"
//		String sCardinalityFurtherInformation = buildCardinalityFurtherInformation(link);
//		if ( ! StrUtil.nullOrVoid(sCardinalityFurtherInformation) ) {
//			sb.append( sCardinalityFurtherInformation );
//			notVoid = true ;
//		}
//		//--- targetEntity ( for ManyToMany and OneToMany )
//		if ( this.genTargetEntity && targetEntityClassName != null ) {
//			if ( notVoid ) {
//				sb.append( ", " );
//			}
//			sb.append( "targetEntity=" + targetEntityClassName + ".class" ) ;			
//		}
//		return sb.toString();
//	}
	//-------------------------------------------------------------------------------------
//	/**
//	 * Build an return the cardinality annotation for an "INVERSE SIDE" <br>
//	 * Example : "@OneToMany ( mappedBy="fieldName", targetEntity=TheClass.class ) "
//	 * 
//	 * @param link
//	 * @param cardinality
//	 * @return
//	 * @throws GeneratorException
//	 */
//	private String buildCardinalityAnnotationForInverseSide( LinkInContext link, String cardinality ) {
//		String targetEntityClassName = link.getTargetEntityName();
//		
//		StringBuilder annotation = new StringBuilder();
//		annotation.append( "@" + cardinality ) ;
//		annotation.append( "(" );
//		boolean notVoid = false ;
//		//--- Common further information : cascade, fetch and optional
//		// ie "cascade = CascadeType.ALL, fetch = FetchType.EAGER"
//		String sCardinalityFurtherInformation = buildCardinalityFurtherInformation(link);
//		if ( ! StrUtil.nullOrVoid(sCardinalityFurtherInformation)) {
//			annotation.append( sCardinalityFurtherInformation );
//			notVoid = true ;
//		}
////		//--- mappedBy - NB : no "mappedBy" for ManyToOne (see JPA javadoc) ( cannot be an inverse side )
////		if ( ! link.isCardinalityManyToOne() ) { 
////			// try to get "mapped by" information
////			String mappedBy = link.getMappedBy(); 
////			if ( mappedBy == null ) {
////				// No defined in link => try to infer 
////				mappedBy = inferMappedBy(link);
////			}
////			if ( mappedBy != null ) {
////				if ( notVoid ) {
////					annotation.append( ", " ); 
////				}
////				annotation.append( "mappedBy=\"" + mappedBy + "\"" );
////				notVoid = true ;
////			}
////		}
////		//--- targetEntity ( always usable, even with ManyToOne )
////		if ( this.genTargetEntity && targetEntityClassName != null ) {
////			if ( notVoid ) {
////				annotation.append( ", " ); 
////			}
////			annotation.append( "targetEntity=" + targetEntityClassName + ".class" ); // No quotes
////		}
//		//---
//		annotation.append( ")" );
//		return annotation.toString();
//	}
	
	/**
	 * Try to infer the 'mappedBy' value by searching in the 'target entity' links 
	 * @param link
	 * @return
	 */
	private String inferMappedBy(LinkInContext link) {
		String entityName = link.getEntity().getName(); // name of the entity holding the link
		String mappedBy = null ;
		int count = 0 ;
		try {
			EntityInContext targetEntity = link.getTargetEntity();
			if ( targetEntity != null ) {
				// search fields referencing the entity with "toOne" cardinality
				for ( LinkInContext inverseSideLink : targetEntity.getLinks() ) {
					String inverseSideLinkTargetEntityName = inverseSideLink.getTargetEntityName(); // v 3.4.0
					if (   inverseSideLink.isCardinalityManyToOne()
						&& entityName.equals(inverseSideLinkTargetEntityName)) {
						count++;
						mappedBy = inverseSideLink.getFieldName(); 
					}
				}
			}
		} catch (GeneratorException e) {
			return null ;
		}
		if ( count == 1 ) { // only one found => it's the right one
			return mappedBy ;
		}
		else {
			return null ;
		}
	}
	//-------------------------------------------------------------------------------------
	/**
	 * Return the further information for the cardinality annotation ( cascade, fetch, optional ) <br>
	 * ie : "cascade = CascadeType.ALL, fetch = FetchType.EAGER"
	 * @param link
	 * @return
	 */
	private String buildCardinalityFurtherInformation(LinkInContext link) {
		/*
		 * JPA documentation
		 * OneToOne   : mappedBy + fetch + cascade + targetEntity + optional + orphanRemoval 
		 * ManyToOne  :          + fetch + cascade + targetEntity + optional                 
		 * OneToMany  : mappedBy + fetch + cascade + targetEntity            + orphanRemoval 
		 * ManyToMany : mappedBy + fetch + cascade + targetEntity                            
		 */
		ListBuilder lb = new ListBuilder(", ");

		lb.append(buildMappedBy(link)); // "mappedBy = ..." 

		lb.append(buildFetch(link)); // "fetch = ..." 
		
		lb.append(buildCascade(link)); // "cascade = ..." 

		lb.append(buildTargetEntity(link)); // "targetEntity = ..." 
		
		lb.append(buildOptional(link)); // "optional=true|false" 
		
		//--- orphanRemoval
		if ( link.isCardinalityOneToOne() || link.isCardinalityOneToMany() ) {
			lb.append(buildOrphanRemoval(link)); // "orphanRemoval=true|false" 
		}
		
		return lb.toString();
	}

	//-------------------------------------------------------------------------------------
	/**
	 * Builds a string with the cascade attribute <br>
	 * ie : "", "cascade = CascadeType.ALL", "cascade = CascadeType.PERSIST", "cascade = { CascadeType.PERSIST, CascadeType.REMOVE }"
	 * @param link
	 * @return
	 */
	private String buildCascade(LinkInContext link) {
		// JPA doc : By default no operations are cascaded
		if ( link.isCascadeALL() ) { 
			return "cascade=CascadeType.ALL" ; 
		}
		else {
			int n = 0 ;
			if ( link.isCascadeMERGE() ) n++ ;
			if ( link.isCascadePERSIST() ) n++ ;
			if ( link.isCascadeREFRESH() ) n++ ;
			if ( link.isCascadeREMOVE() ) n++ ;
			if ( n == 0 ) {
				return "" ;
			}
			else {
				StringBuilder sb = new StringBuilder();
				sb.append("cascade=");
				if ( n > 1 ) {
					sb.append("{ ");
				}
				int c = 0 ;
				if ( link.isCascadeMERGE()  ) { 
					if ( c > 0 ) { sb.append(", "); } 
					sb.append("CascadeType.MERGE"  ); 
					c++; 
				}
				if ( link.isCascadePERSIST()) { 
					if ( c > 0 ) { sb.append(", "); } 
					sb.append("CascadeType.PERSIST"); 
					c++; 
				}
				if ( link.isCascadeREFRESH()) { 
					if ( c > 0 ) { sb.append(", "); } 
					sb.append("CascadeType.REFRESH"); 
					c++; 
				}
				if ( link.isCascadeREMOVE() ) { 
					if ( c > 0 ) { sb.append(", "); } 
					sb.append("CascadeType.REMOVE" ); 
					c++; 
				}
				if ( n > 1 ) {
					sb.append(" }");
				}
				return sb.toString();
			}
		}
	}

	//-------------------------------------------------------------------------------------
	/**
	 * JPA doc : Whether the association should be lazily loaded or must be eagerly fetched.
	 * Usable with all cardinalities 
	 * @param link
	 * @return
	 */
	private String buildFetch(LinkInContext link) {
		String fetchType = determineFetchType(link);
		if ( ! StrUtil.nullOrVoid(fetchType) ) {
			return "fetch=FetchType." + fetchType ;
		}
		return ""; // Nothing => use JPA default fetch type
	}
	private String determineFetchType(LinkInContext link) {
		if ( link.isFetchEAGER() ) { 
			// has been defined in the model => use it
			return "EAGER" ; 
		}
		if ( link.isFetchLAZY()  ) { 
			// has been defined in the model => use it
			return "LAZY" ;
		}
		if ( link.isFetchDEFAULT() ) {
			// has not been defined in the model => use $jpa default fetch type if any 
			if ( link.isCardinalityOneToOne() && this.linkOneToOneFetchType != FetchType.UNDEFINED ) {
				return this.linkOneToOneFetchType.getText();
			}
			if ( link.isCardinalityManyToOne() && this.linkManyToOneFetchType != FetchType.UNDEFINED ) {
				return this.linkManyToOneFetchType.getText();
			}
			if ( link.isCardinalityOneToMany() && this.linkOneToManyFetchType != FetchType.UNDEFINED ) {
				return this.linkOneToManyFetchType.getText();
			}
			if ( link.isCardinalityManyToMany() && this.linkManyToManyFetchType != FetchType.UNDEFINED ) {
				return this.linkManyToManyFetchType.getText();
			}
		}
		return ""; // Nothing => use JPA default fetch type
	}
	
	//-------------------------------------------------------------------------------------
	/**
	 * JPA doc : (Optional) The entity class that is the target of the association.
	 * Usable with all kinds of cardinality 
	 * @param link
	 * @return
	 */
	private String buildTargetEntity(LinkInContext link) {
		if ( this.genTargetEntity ) {
			String targetEntityClassName = link.getTargetEntityName();
			if ( ! StrUtil.nullOrVoid(targetEntityClassName) ) {
				return "targetEntity=" + targetEntityClassName + ".class";
			}
		}
		return "";
	}
	
	/**
	 * JPA doc : The field that owns the relationship. 
	 * Required for "inverse side"
	 * Usable with OneToOne, ManyToMany and OneToMany
	 * Optional for OneToOne, not Optional for ManyToMany and OneToMany
	 * @param link
	 * @return
	 */
	private String buildMappedBy(LinkInContext link) {
		if ( ! link.isCardinalityManyToOne() ) { 
			// try to get explicit "mapped by" information
			String mappedBy = link.getMappedBy(); 
			if ( mappedBy == null ) {
				// not explicitly defined in the model
				if ( link.isCardinalityManyToMany() ||  link.isCardinalityOneToMany()) {
					// ManyToMany and OneToMany : mandatory => try to infer
					mappedBy = inferMappedBy(link);
				}
				else {
					// OneToOne : optional (but required for inverse side)
					if ( ! link.isOwningSide() ) {
						// OneToOne inverse side : required => try to infer
						mappedBy = inferMappedBy(link);
					}
				}
			}
			if ( mappedBy != null ) {
				return "mappedBy=\"" + mappedBy + "\"";
			}
		}
		return "";
	}
	//-------------------------------------------------------------------------------------
	/**
	 * JPA doc : Whether the association is optional.
	 * Usable with OneToOne and ManyToOne
	 * @param link
	 * @return
	 */
	private String buildOptional(LinkInContext link) {
		if ( link.isCardinalityOneToOne() || link.isCardinalityManyToOne() ) {			
			// JPA doc : default = true
			if ( link.isOptionalTrue() ) { 
				return "optional=true" ; 
			}
			if ( link.isOptionalFalse() ) { 
				return "optional=false" ; 
			}
		}
		return "";
	}

	private String buildOrphanRemoval(LinkInContext link) {
		if ( link.isOrphanRemoval() ) { 
			return "orphanRemoval=true" ; 
		}
		return "";
	}

	private void processLinkCardinalityAnnotation(AnnotationsBuilder annotations, LinkInContext link) {
//		String annotation = "" ;
//		String targetEntityClassName = link.getTargetEntityName(); 
//		if ( link.isOwningSide() ) {
//			if ( link.isCardinalityOneToOne() ) {
//				annotation = buildCardinalityAnnotationForOwningSide( link, "OneToOne", null ) ; 
//			} 
//			else if ( link.isCardinalityManyToOne() ) {
//				annotation = buildCardinalityAnnotationForOwningSide( link, "ManyToOne", null ) ; 
//			} 
//			else if ( link.isCardinalityManyToMany() ) {
//				annotation = buildCardinalityAnnotationForOwningSide( link, "ManyToMany", targetEntityClassName ) ; 
//			}
//			else if ( link.isCardinalityOneToMany() ) {
//				//--- Possible for unidirectional "OneToMany" relationship ( whithout inverse side )
//				annotation = buildCardinalityAnnotationForOwningSide( link, "OneToMany", targetEntityClassName ) ; 
//			} 
//		} 
//		else {
//			//--- INVERSE SIDE
//			if (link.isCardinalityOneToOne()) {
//				annotation = buildCardinalityAnnotationForInverseSide( link, "OneToOne" ); 
//			} 
//			else if (link.isCardinalityOneToMany()) {
//				annotation = buildCardinalityAnnotationForInverseSide( link, "OneToMany" ); 
//			} 
//			else if (link.isCardinalityManyToMany()) {
//				annotation = buildCardinalityAnnotationForInverseSide( link, "ManyToMany" ); 
//			} 
//			else if (link.isCardinalityManyToOne()) {
//				// Not supposed to occur for an INVERSE SIDE !
//				annotation = buildCardinalityAnnotationForInverseSide( link, "ManyToOne" ) ; 
//			} 
//		}
		String annotation = buildCardinalityAnnotation(link);
		if ( ! StrUtil.nullOrVoid(annotation) ) {
			annotations.addLine(annotation);
		}
	}
	
	private void processLinkJoinAnnotation(AnnotationsBuilder annotations, LinkInContext link, 
			List<AttributeInContext> alreadyMappedFields ) throws GeneratorException {
		if ( link.isCardinalityManyToMany() ) {
			processJoinTable(annotations, link) ;
		} 
		else {
		    // Example : @JoinColumn(name="BADGE_NUMBER", referencedColumnName="BADGE_NUMBER")
			// used in owning side ManyToOne ( or OneToOne )
			// can be used also for unidirectional OneToMany relationship (since JPA 2.x)
			processJoinColumns(annotations, link, alreadyMappedFields );
		}
	}	
	//-------------------------------------------------------------------------------------
	/**
	 * Generates a "@JoinColumn" (single column) or "@JoinColumns" (multiple columns) annotation
	 * @param annotations
	 * @param link
	 * @param joinColumns 
	 * @param alreadyMappedFields (not used if null)
	 */
	private void processJoinColumns(AnnotationsBuilder annotations, LinkInContext link, 
					List<AttributeInContext> alreadyMappedFields ) throws GeneratorException
	{
		List<String> jc = buildJoinColumnAnnotations( link, alreadyMappedFields );
		if ( jc.size() == 1 ) {
			// Single Join Column
			// Example :
			//   @JoinColumn(name="MGR_COUNTRY", referencedColumnName="COUNTRY") 
			annotations.addLine( jc.get(0) );
		}
		else if ( jc.size() > 1) {
			// Multiple Join Columns
			// Example :
			// @JoinColumns( {
			//   @JoinColumn(name="MGR_COUNTRY", referencedColumnName="COUNTRY") ,
			//   @JoinColumn(name="MGR_ID", referencedColumnName="EMP_ID") } )			
			annotations.addLine("@JoinColumns( { " );
			int i = 0 ;
			for ( String s : jc ) {
				String jcEnd = ",";
				if ( i == jc.size() - 1) {
					jcEnd = "} )" ;
				}
				annotations.addLine("    " + s + jcEnd );
				i++;
			}
		}
		// else ( jc.length == 0 ) : no join columns
	}	
	//-------------------------------------------------------------------------------------
	/**
	 * Generates the join table annotation : "@JoinTable"
	 * @param annotations
	 * @param link
	 */
	private void processJoinTable(AnnotationsBuilder annotations, LinkInContext link ) {
/*****
 * 
 * TODO
 * 
 *******
		JoinTableInContext joinTable = link.getJoinTable();
		EntityInContext entity = link.getJoinEntity(); // TODO 
//		if ( joinTable != null ) {
		if ( entity != null ) {
//			annotations.addLine("@JoinTable(name=\"" + joinTable.getName() + "\", " );
			annotations.addLine("@JoinTable(name=\"" + entity.getSqlTableName() + "\", " );
			
			List<JoinColumnInContext> joinColumns = joinTable.getJoinColumns();
			if ( joinColumns != null ) {
				processJoinTableColumns(annotations, link, joinColumns, "joinColumns", "," );
			}
			
			List<JoinColumnInContext> inverseJoinColumns = joinTable.getInverseJoinColumns();
			if ( inverseJoinColumns != null ) 
			{
				processJoinTableColumns(annotations, link, inverseJoinColumns, "inverseJoinColumns", "" );
			}
//			annotations.addLine(" ) \n" );
			annotations.addLine(" )" );
		}
		// else : no join table (not an error)
//		else {
//			annotations.addLine( "// @JoinTable ERROR : NO JOIN TABLE FOR THIS LINK ! " );
//		}
 *******/
	}

	//-------------------------------------------------------------------------------------
	private void processJoinTableColumns( AnnotationsBuilder annotations, LinkInContext link, 
			 String name,  String end ) throws GeneratorException
	{
		List<String> jc = buildJoinColumnAnnotations( link, null );
		if ( jc != null ) {
			if ( jc.size() == 1 ) 
			{
				// Single Join Column
				// Example :
				//   joinColumns=@JoinColumn(name="MGR_COUNTRY", referencedColumnName="COUNTRY") 
				
				annotations.addLine("  " + name + "=" + jc.get(0) + end);
			}
			else 
			{
				// Multiple Join Columns
				// Example :
				//   joinColumns={
				//     @JoinColumn(name="MGR_COUNTRY", referencedColumnName="COUNTRY") ,
				//     @JoinColumn(name="MGR_ID", referencedColumnName="EMP_ID") }
				
				annotations.addLine("  " + name + "={" );
				int i = 0 ;
				for ( String s : jc ) {
					String jcEnd = ",";
					if ( i == jc.size() - 1) {
						jcEnd = "}" + end ;
					}
					annotations.addLine("    " + s + jcEnd );
					i++;
				}
			}
		}
	}
	
	//-------------------------------------------------------------------------------------
	/**
	 * Returns a list of strings containing the annotations <br>
	 * Example : <br>
	 *  0 : "@JoinColumn(name="MGR_COUNTRY", referencedColumnName="COUNTRY")"<br>
	 *  1 : "@JoinColumn(name="MGR_ID", referencedColumnName="EMP_ID")"<br>
	 *  
	 * @param link
	 * @param alreadyMappedFields
	 * @return
	 * @throws GeneratorException
	 */
	private List<String> buildJoinColumnAnnotations( LinkInContext link,
			List<AttributeInContext> alreadyMappedFields ) throws GeneratorException {
		List<String> annotations = new LinkedList<>();
		for ( LinkAttributeInContext linkAttribute : link.getAttributes() ) {
			String annotation = buildJoinColumnAnnotation(linkAttribute, link, alreadyMappedFields);
			annotations.add(annotation);
		}
		return annotations;
	}
	
	//-------------------------------------------------------------------------------------
	/**
	 * Builds and returns a single "@JoinColumn" annotation 
	 * @param linkAttribute
	 * @param link
	 * @param alreadyMappedFields
	 * @return
	 * @throws GeneratorException
	 */
	private String buildJoinColumnAnnotation(LinkAttributeInContext linkAttribute, LinkInContext link, 
			List<AttributeInContext> alreadyMappedFields ) throws GeneratorException {
		StringBuilder sb = new StringBuilder();
		sb.append( "@JoinColumn(");
		sb.append( "name=\"" + linkAttribute.getOriginAttribute().getSqlColumnName() +"\"" );
		sb.append( ", " );
		sb.append( "referencedColumnName=\"" + linkAttribute.getReferencedAttribute().getSqlColumnName() +"\"" );
		
		/*
		 * Defining "insertable=false, updatable=false" is useful when you need 
		 * to map a field more than once in an entity, typically:
		 *  - when using a composite key
		 *  - when using a shared primary key
		 *  - when using cascaded primary keys
		 */
		if ( alreadyMappedFields != null ) {
			sb.append( buildJoinColumnInsertableUpdatable(linkAttribute, alreadyMappedFields) );
		}
		else {
			sb.append( buildJoinColumnInsertableUpdatable(link) );
		}
			
		sb.append( ")");
		return sb.toString();
	}
	
	/**
	 * @param link
	 * @return
	 */
	private String buildJoinColumnInsertableUpdatable(LinkInContext link) {
		StringBuilder sb = new StringBuilder();
		if ( getFlag(this.joinColumnInsertable, link.getInsertableFlag() ) == BooleanValue.FALSE ) {
			sb.append( ", insertable=false" ); 
		}
		if ( getFlag(this.joinColumnUpdatable, link.getUpdatableFlag() ) == BooleanValue.FALSE ) {
			sb.append( ", updatable=false" ); 
		}
		return sb.toString();
	}
	private BooleanValue getFlag(BooleanValue jpaFlag, BooleanValue linkFlag) {
		if ( linkFlag == BooleanValue.FALSE ) { 
			// explicitly set to FALSE at link level 
			return BooleanValue.FALSE ;
		}
		if ( jpaFlag == BooleanValue.FALSE && linkFlag != BooleanValue.TRUE ) {
			// not explicitly set to TRUE at link level (FALSE or UNDEFINED)
			return BooleanValue.FALSE ;
		}
		return BooleanValue.UNDEFINED;
	}
	
	private String buildJoinColumnInsertableUpdatable(LinkAttributeInContext linkAttribute, List<AttributeInContext> alreadyMappedFields ) {
		StringBuilder sb = new StringBuilder();
		if ( isColumnAlreadyMappedAsAField (linkAttribute, alreadyMappedFields ) ) {
			sb.append( ", " );
			sb.append( "insertable=false" ); 
			sb.append( ", " );
			sb.append( "updatable=false" ); 
		}
		return sb.toString();
	}
	
	/**
	 * Returns TRUE if the given 'join column' is already mapped as a simple field 
	 * @param joinColumn
	 * @param alreadyMappedFields list of all the fields already mapped 
	 * @return
	 */
	private boolean isColumnAlreadyMappedAsAField (LinkAttributeInContext linkAttribute, List<AttributeInContext> alreadyMappedFields ) {
		if ( alreadyMappedFields != null ) {
			String attributeName = linkAttribute.getOriginAttributeName(); // ie "publisherId" in "Book"
			if ( attributeName != null ) {
				for ( AttributeInContext attribute : alreadyMappedFields ) {
					if ( attributeName.equals( attribute.getName() ) ) {
						// Found in the list of mapped fields => already mapped as a field
						return true ;
					}
				}
			}
		}
		return false ;
	}
	
	//-------------------------------------------------------------------------------------------------------------
	// J.P.A. ANNOTATIONS FOR FIELDS
	//-------------------------------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the JPA annotations for the given field (with a left margin)"
			},
		example={ 
			"$jpa.fieldAnnotations( 4, $field )" },
		parameters = { 
			"leftMargin : the left margin (number of blanks) ",
			"field : the field to be annotated "
			},
		since = "2.0.7"
	)
	public String fieldAnnotations(int leftMargin, AttributeInContext attribute )
    {
		JpaAnnotations annotationsJPA = new JpaAnnotations(attribute, genColumnDefinition); // v 3.4.0
		return annotationsJPA.getJpaAnnotations(leftMargin, JpaAnnotations.EMBEDDED_ID_FALSE );
    }

	//-------------------------------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the JPA annotations for an 'embedded id' (with a left margin)",
			"( there's no '@Id' for an embedded id )"
			},
		example={ 
			"$jpa.embeddedIdAnnotations( 4, $attribute )" },
		parameters = { 
			"leftMargin : the left margin (number of blanks) ",
			"attribute : the attribute to be annotated "
			},
		since = "2.0.7"
		)
	public String embeddedIdAnnotations(int leftMargin, AttributeInContext attribute )
    {
		JpaAnnotations annotationsJPA = new JpaAnnotations(attribute, genColumnDefinition); // v 3.4.0
		return annotationsJPA.getJpaAnnotations(leftMargin, JpaAnnotations.EMBEDDED_ID_TRUE );
    }
	//-------------------------------------------------------------------------------------------------------------
	
}
