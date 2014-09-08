/**
 *  Copyright (C) 2008-2013  Telosys project org. ( http://www.telosys.org/ )
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
import org.telosys.tools.generator.context.doc.VelocityObject;
import org.telosys.tools.generator.context.doc.VelocityReturnType;
import org.telosys.tools.generator.context.names.ContextName;
import org.telosys.tools.generator.context.tools.AnnotationsBuilder;
import org.telosys.tools.generator.context.tools.AnnotationsForJPA;

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
public class Jpa {

//	private final static int ONE_TO_ONE   = 1 ;
//	private final static int MANY_TO_ONE  = 2 ;
//	private final static int ONE_TO_MANY  = 3 ;
//	private final static int MANY_TO_MANY = 4 ;
	
	private final static List<String> VOID_STRINGS_LIST = new LinkedList<String>();

	//-------------------------------------------------------------------------------------
	// CONSTRUCTOR
	//-------------------------------------------------------------------------------------
	public Jpa() {
		super();
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
	//public List<String> imports(JavaBeanClass entity) 
	public List<String> imports(EntityInContext entity) 
	{
		ImportsList _importsJpa = buildJpaImportsList(entity) ;
		if ( _importsJpa != null )
		{
			return _importsJpa.getList() ;
		}
		return VOID_STRINGS_LIST ;
	}
	
	//private JavaBeanClassImports getImports(JavaBeanClass entity) {
	private ImportsList buildJpaImportsList(EntityInContext entity) {
		ImportsList jpaImports = new ImportsList();

		jpaImports.declareType("javax.persistence.*");
		
		/*
		jpaImports.declareType("javax.persistence.Entity");
		jpaImports.declareType("javax.persistence.Table");
		jpaImports.declareType("javax.persistence.Id");
		
		jpaImports.declareType("javax.persistence.UniqueConstraint");
		jpaImports.declareType("javax.persistence.EmbeddedId");
		jpaImports.declareType("javax.persistence.Embeddable");
		jpaImports.declareType("javax.persistence.AttributeOverride");
		jpaImports.declareType("javax.persistence.AttributeOverrides");

		jpaImports.declareType("javax.persistence.OneToOne");
		jpaImports.declareType("javax.persistence.ManyToMany");
		jpaImports.declareType("javax.persistence.ManyToOne");
		jpaImports.declareType("javax.persistence.OneToMany");

		jpaImports.declareType("javax.persistence.GeneratedValue");
		jpaImports.declareType("javax.persistence.GenerationType");
		jpaImports.declareType("javax.persistence.SequenceGenerator");
		jpaImports.declareType("javax.persistence.TableGenerator");
		*/
		return jpaImports ;
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
	//public String entityAnnotations(int iLeftMargin, JavaBeanClass entity)
	public String entityAnnotations(int iLeftMargin, EntityInContext entity)
    {
		AnnotationsBuilder b = new AnnotationsBuilder(iLeftMargin);
		
		b.addLine("@Entity");
		
		String s = "@Table(name=\"" + entity.getDatabaseTable() + "\"" ;
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
	public String linkAnnotations( int marginSize, LinkInContext entityLink, List<AttributeInContext> alreadyMappedFields )
				throws GeneratorException {
		
		//Link   _link         = entityLink.getLink();
		
		//Entity _targetEntity = entityLink.getTargetEntity() ;
		//String targetEntityClassName = entityLink.getTargetEntitySimpleType() ; // refactoring v 2.1.0
		String targetEntityClassName = entityLink.getTargetEntity().getName();

		AnnotationsBuilder annotations = new AnnotationsBuilder(marginSize);
		
		//if ( _link.isOwningSide() ) 
		if ( entityLink.isOwningSide() ) 
		{
			//if (_link.isTypeOneToOne()) 
			if ( entityLink.isCardinalityOneToOne() ) 
			{
				// Examples :
				//   @OneToOne 
			    //   @JoinColumn(name="BADGE_NUMBER", referencedColumnName="BADGE_NUMBER")

				annotations.addLine(getOwningSideCardinalityAnnotation( entityLink, "OneToOne", null ) ); 
				//processJoinColumns(annotations, _link.getJoinColumns(), ONE_TO_ONE, fieldsList );
				processJoinColumns(annotations, entityLink, entityLink.getJoinColumns(), alreadyMappedFields );
			} 
			//else if (_link.isTypeManyToOne()) 
			else if ( entityLink.isCardinalityManyToOne() ) 
			{
				annotations.addLine(getOwningSideCardinalityAnnotation( entityLink, "ManyToOne", null ) ); 
				//processJoinColumns(annotations, _link.getJoinColumns(), MANY_TO_ONE, fieldsList );
				processJoinColumns(annotations, entityLink, entityLink.getJoinColumns(), alreadyMappedFields );
			} 
			//else if (_link.isTypeManyToMany()) 
			else if ( entityLink.isCardinalityManyToMany() ) 
			{
				annotations.addLine(getOwningSideCardinalityAnnotation( entityLink, "ManyToMany", targetEntityClassName ) ); 
				//processJoinTable(annotations, _link.getJoinTable(), MANY_TO_MANY) ;
				processJoinTable(annotations, entityLink) ;
			}
			//else if (_link.isTypeOneToMany()) 
			else if ( entityLink.isCardinalityOneToMany() ) 
			{
				//--- Possible for unidirectional "OneToMany" relationship ( whithout inverse side )
				annotations.addLine(getOwningSideCardinalityAnnotation( entityLink, "OneToMany", targetEntityClassName ) ); 
				//processJoinTable(annotations, _link.getJoinTable(), ONE_TO_MANY) ;				
				processJoinTable(annotations, entityLink) ;				
			} 
			else 
			{
				// Error 
			}
		} 
		else 
		{
			//--- INVERSE SIDE
			if (entityLink.isCardinalityOneToOne()) 
			{
				annotations.addLine(getInverseSideCardinalityAnnotation( entityLink, "OneToOne" ) ); 
			} 
			else if (entityLink.isCardinalityOneToMany()) 
			{
				annotations.addLine(getInverseSideCardinalityAnnotation( entityLink, "OneToMany" ) ); 
			} 
			else if (entityLink.isCardinalityManyToMany()) 
			{
				annotations.addLine(getInverseSideCardinalityAnnotation( entityLink, "ManyToMany" ) ); 
			} 
			else if (entityLink.isCardinalityManyToOne()) 
			{
				// Not supposed to occur for an INVERSE SIDE !
				annotations.addLine(getInverseSideCardinalityAnnotation( entityLink, "ManyToOne" ) ); 
			} 
			else 
			{
				// Error 
			}
		}
		
		return annotations.getAnnotations();
	}
	
	//----------------------------------------------------------------
	/**
	 * Build an return the cardinality annotation for an "OWNING SIDE"
	 * Example : "@ManyToOne ( cascade = CascadeType.ALL, fetch = FetchType.EAGER ) "
	 * @param cardinality
	 * @param targetEntityClassName the target entity ( or null if none ) 
	 * @return
	 */
	private String getOwningSideCardinalityAnnotation( LinkInContext entityLink, String cardinality, String targetEntityClassName ) 
	{
		//Link _link = entityLink.getLink(); 

		StringBuilder sb = new StringBuilder();
		sb.append( "@" + cardinality ) ;
		if ( targetEntityClassName != null ) {
			sb.append( "(" );
			//--- Common further information : cascade, fetch and optional
			// ie "cascade = CascadeType.ALL, fetch = FetchType.EAGER"
			//String sCardinalityFurtherInformation = getCardinalityFurtherInformation(_link);
			String sCardinalityFurtherInformation = getCardinalityFurtherInformation(entityLink);
			if ( ! StrUtil.nullOrVoid(sCardinalityFurtherInformation)) {
				sb.append( sCardinalityFurtherInformation );
				sb.append( ", " );
			}
			//--- targetEntity ( for ManyToMany and OneToMany )
			sb.append( "targetEntity=" + targetEntityClassName + ".class" ) ;			
			sb.append( ")" );
		}
		else {
			//--- Common further information : cascade, fetch and optional
			// ie "cascade = CascadeType.ALL, fetch = FetchType.EAGER"
			//String sCardinalityFurtherInformation = getCardinalityFurtherInformation(_link);
			String sCardinalityFurtherInformation = getCardinalityFurtherInformation(entityLink);
			if ( ! StrUtil.nullOrVoid(sCardinalityFurtherInformation)) {
				sb.append( "(" );
				sb.append( sCardinalityFurtherInformation );
				sb.append( ")" );
			}
		}
		return sb.toString();
	}
	
	//-------------------------------------------------------------------------------------
	/**
	 * Build an return the cardinality annotation for an "INVERSE SIDE"
	 * Example : "@OneToMany ( mappedBy="fieldName", targetEntity=TheClass.class ) "
	 * @param cardinality
	 * @return
	 */
	private String getInverseSideCardinalityAnnotation( LinkInContext entityLink, String cardinality ) 
					throws GeneratorException  {
		//Link   _link         = entityLink.getLink();
		
		//Entity _targetEntity = entityLink.getTargetEntity() ;
		//String targetEntityClassName = entityLink.getTargetEntitySimpleType() ; // refactoring v 2.1.0
		String targetEntityClassName = entityLink.getTargetEntity().getName();
		
		StringBuilder annotation = new StringBuilder();
		annotation.append( "@" + cardinality ) ;
		annotation.append( "(" );
		//--- Common further information : cascade, fetch and optional
		// ie "cascade = CascadeType.ALL, fetch = FetchType.EAGER"
		//String sCardinalityFurtherInformation = getCardinalityFurtherInformation(_link);
		String sCardinalityFurtherInformation = getCardinalityFurtherInformation(entityLink);
		if ( ! StrUtil.nullOrVoid(sCardinalityFurtherInformation)) {
			annotation.append( sCardinalityFurtherInformation );
			annotation.append( ", " ); 
		}
		//--- mappedBy - NB : no "mappedBy" for ManyToOne (see JPA javadoc) ( cannot be an inverse side )
		//if ( ! _link.isTypeManyToOne() ) { 
		if ( ! entityLink.isCardinalityManyToOne() ) { 
			annotation.append( "mappedBy=\"" + entityLink.getMappedBy() + "\"" );
			annotation.append( ", " ); 
		}
		//--- targetEntity ( always usable, even with ManyToOne )
		//annotation.append( "targetEntity=" + _targetEntity.getBeanJavaClass() + ".class" ); // No quote for "targetEntity"
		annotation.append( "targetEntity=" + targetEntityClassName + ".class" ); // No quotes 
		//---
		annotation.append( ")" );
		return annotation.toString();
	}
	
	//-------------------------------------------------------------------------------------
	/**
	 * Return the further information for the cardinality annotation ( cascade, fetch, optional ) <br>
	 * ie : "cascade = CascadeType.ALL, fetch = FetchType.EAGER"
	 * @param link
	 * @return
	 */
	//private String getCardinalityFurtherInformation(Link link)
	private String getCardinalityFurtherInformation(LinkInContext link)
	{
		/*
		 * JPA documentation
		 * OneToOne   : cascade + fecth + optional
		 * ManyToOne  : cascade + fecth + optional
		 * OneToMany  : cascade + fecth 
		 * ManyToMany : cascade + fecth 
		 */
		int n = 0 ;
		StringBuilder sb = new StringBuilder();

		//--- CASCADE 
		String sCascade = buildCascade(link); // "cascade = ..." 
		if ( ! StrUtil.nullOrVoid( sCascade ) ) {
			if ( n > 0 ) sb.append(", ");
			sb.append(sCascade);
			n++ ;
		}

		//--- FETCH 
		String sFetch = buildFetch(link); // "fetch = ..." 
		if ( ! StrUtil.nullOrVoid( sFetch ) ) {
			if ( n > 0 ) sb.append(", ");
			sb.append(sFetch);
			n++ ;
		}
		
		//--- OPTIONAL ( only for OneToOne and ManyToOne )
		//if ( link.isTypeOneToOne() || link.isTypeManyToOne() ) {
		if ( link.isCardinalityOneToOne() || link.isCardinalityManyToOne() ) {
			String sOptional = buildOptional(link); // "optional=true|false" 
			if ( ! StrUtil.nullOrVoid( sOptional ) ) {
				if ( n > 0 ) sb.append(", ");
				sb.append(sOptional);
				n++ ;
			}
		}
		
		return sb.toString();
	}

	//-------------------------------------------------------------------------------------
	/**
	 * Builds a string with the cascade attribute <br>
	 * ie : "", "cascade = CascadeType.ALL", "cascade = CascadeType.PERSIST", "cascade = { CascadeType.PERSIST, CascadeType.REMOVE }"
	 * @param link
	 * @return
	 */
	//private String buildCascade(Link link)
	private String buildCascade(LinkInContext link)
	{
		// JPA doc : By default no operations are cascaded
		if ( link.isCascadeALL() ) { 
			return "cascade = CascadeType.ALL" ; 
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
				sb.append("cascade = ");
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
	//private String buildFetch(Link link)
	private String buildFetch(LinkInContext link)
	{
		// JPA doc : default = EAGER
		if ( link.isFetchEAGER() ) { 
			return "fetch = FetchType.EAGER" ; 
		}
		if ( link.isFetchLAZY()  ) { 
			return "fetch = FetchType.LAZY" ;
		}
		return "";
	}
	
	//-------------------------------------------------------------------------------------
	//private String buildOptional(Link link)
	private String buildOptional(LinkInContext link)
	{
		// JPA doc : default = true
		if ( link.isOptionalTrue() ) { 
			return "optional = true" ; 
		}
		if ( link.isOptionalFalse() ) { 
			return "optional = false" ; 
		}
		return "";
	}

	//-------------------------------------------------------------------------------------
	/**
	 * Generates a "@JoinColumn" (single column) or "@JoinColumns" (multiple columns) annotation
	 * @param annotations
	 * @param joinColumns
	 * @param linkCardinality
	 * @param alreadyMappedFields
	 */
//	private void processJoinColumns(AnnotationsBuilder annotations, JoinColumns joinColumns, 
//			int linkCardinality, List<AttributeInContext> fieldsList ) 
//	{
//		if ( joinColumns != null ) 
//		{
//			String[] jc = getJoinColumnAnnotations( joinColumns.getAll(), linkCardinality, fieldsList );
//			if ( jc != null ) {
//				if ( jc.length == 1 ) 
//				{
//					// Single Join Column
//					// Example :
//					//   @JoinColumn(name="MGR_COUNTRY", referencedColumnName="COUNTRY") 
//					
//					annotations.addLine( jc[0] );
//				}
//				else 
//				{
//					// Multiple Join Columns
//					// Example :
//					// @JoinColumns( {
//					//   @JoinColumn(name="MGR_COUNTRY", referencedColumnName="COUNTRY") ,
//					//   @JoinColumn(name="MGR_ID", referencedColumnName="EMP_ID") } )
//					
//					annotations.addLine("@JoinColumns( { " );
//					for ( int i = 0 ; i < jc.length ; i++ ) {
//						String end = ( i < jc.length - 1) ? "," : " } )" ;
//						annotations.addLine("    " + jc[i] + end );
//					}
//				}
//			}
//		}
//	}
	
	private void processJoinColumns(AnnotationsBuilder annotations, LinkInContext link, List<JoinColumnInContext> joinColumns, List<AttributeInContext> alreadyMappedFields ) 
	{
		//String[] jc = getJoinColumnAnnotations( joinColumns.getAll(), linkCardinality, fieldsList );
		String[] jc = getJoinColumnAnnotations( link, joinColumns, alreadyMappedFields );
		if ( jc != null ) {
			if ( jc.length == 1 ) 
			{
				// Single Join Column
				// Example :
				//   @JoinColumn(name="MGR_COUNTRY", referencedColumnName="COUNTRY") 
				
				annotations.addLine( jc[0] );
			}
			else 
			{
				// Multiple Join Columns
				// Example :
				// @JoinColumns( {
				//   @JoinColumn(name="MGR_COUNTRY", referencedColumnName="COUNTRY") ,
				//   @JoinColumn(name="MGR_ID", referencedColumnName="EMP_ID") } )
				
				annotations.addLine("@JoinColumns( { " );
				for ( int i = 0 ; i < jc.length ; i++ ) {
					String end = ( i < jc.length - 1) ? "," : " } )" ;
					annotations.addLine("    " + jc[i] + end );
				}
			}
		}
	}	
	//-------------------------------------------------------------------------------------
	/**
	 * Generates the join table annotation : "@JoinTable"
	 * @param annotations
	 * @param joinTable
	 * @param linkCardinality
	 */
	//private void processJoinTable(AnnotationsBuilder annotations, JoinTable joinTable, int linkCardinality) 	 
	private void processJoinTable(AnnotationsBuilder annotations, LinkInContext link ) 	 
	{
		JoinTableInContext joinTable = link.getJoinTable();
		if ( joinTable != null ) {
			annotations.addLine("@JoinTable(name=\"" + joinTable.getName() + "\", " );
			
			//JoinColumns joinColumns = joinTable.getJoinColumns();
			LinkedList<JoinColumnInContext> joinColumns = joinTable.getJoinColumns();
			if ( joinColumns != null ) 
			{
				//processJoinTableColumns(annotations, "joinColumns", joinColumns.getAll(), ",", linkCardinality);
				processJoinTableColumns(annotations, link, joinColumns, "joinColumns", "," );
			}
			
			//InverseJoinColumns inverseJoinColumns = joinTable.getInverseJoinColumns();
			LinkedList<JoinColumnInContext> inverseJoinColumns = joinTable.getInverseJoinColumns();
			if ( inverseJoinColumns != null ) 
			{
				//processJoinTableColumns(annotations, "inverseJoinColumns", inverseJoinColumns.getAll(), "", linkCardinality);
				processJoinTableColumns(annotations, link, inverseJoinColumns, "inverseJoinColumns", "" );
			}
			annotations.addLine(" ) \n" );
		}
		else {
			annotations.addLine( "@JoinTable(name=\"NO_JOIN_TABLE_FOR_THIS_LINK\" ) \n" );
		}
	}

	//-------------------------------------------------------------------------------------
	//private void processJoinTableColumns( AnnotationsBuilder annotations, String name, JoinColumn[] joinColumns, String end, int linkCardinality )
	private void processJoinTableColumns( AnnotationsBuilder annotations, LinkInContext link, List<JoinColumnInContext> joinColumns, String name,  String end )
	
	{
		//String[] jc = getJoinColumnAnnotations( joinColumns, linkCardinality, null );
		String[] jc = getJoinColumnAnnotations( link, joinColumns, null );
		if ( jc != null ) {
			if ( jc.length == 1 ) 
			{
				// Single Join Column
				// Example :
				//   joinColumns=@JoinColumn(name="MGR_COUNTRY", referencedColumnName="COUNTRY") 
				
				annotations.addLine("  " + name + "=" + jc[0] + end);
			}
			else 
			{
				// Multiple Join Columns
				// Example :
				//   joinColumns={
				//     @JoinColumn(name="MGR_COUNTRY", referencedColumnName="COUNTRY") ,
				//     @JoinColumn(name="MGR_ID", referencedColumnName="EMP_ID") }
				
				annotations.addLine("  " + name + "={" );
				for ( int i = 0 ; i < jc.length ; i++ ) {
					String jcEnd = ( i < jc.length - 1) ? "," : ( "}"+end ) ;
					annotations.addLine("    " + jc[i] + jcEnd );
				}
			}
		}
	}
	
	//-------------------------------------------------------------------------------------
	/**
	 * Returns an array of string containing the annotations <br>
	 * Example : <br>
	 *  0 : "@JoinColumn(name="MGR_COUNTRY", referencedColumnName="COUNTRY")"
	 *  1 : "@JoinColumn(name="MGR_ID", referencedColumnName="EMP_ID")"
	 *  
	 * @param joinColumns
	 * @param linkCardinality
	 * @return
	 */
	//private String[] getJoinColumnAnnotations( JoinColumn[] joinColumns, int linkCardinality, List<AttributeInContext> fieldsList ) 
	private String[] getJoinColumnAnnotations( LinkInContext link, List<JoinColumnInContext> joinColumns, List<AttributeInContext> alreadyMappedFields ) 
	{
//		if ( null == joinColumns ) return null ;
//		if ( joinColumns.length == 0 ) return null ;
//		String[] annotations = new String[joinColumns.length];
//		for ( int i = 0 ; i < joinColumns.length ; i++ ) {
//			annotations[i] = getJoinColumnAnnotation(joinColumns[i], linkCardinality, fieldsList);
//		}
//		return annotations;
		
		String[] annotations = new String[joinColumns.size()];
		int i = 0 ;
		for ( JoinColumnInContext jc : joinColumns ) {
			annotations[i++] = getJoinColumnAnnotation(jc, link, alreadyMappedFields);
		}
		return annotations;
	}
	
	//-------------------------------------------------------------------------------------
	/**
	 * Build and return a single "@JoinColumn" annotation 
	 * @param joinColumn
	 * @param linkCardinality
	 * @param alreadyMappedFields
	 * @return
	 */
	//private String getJoinColumnAnnotation(JoinColumn joinColumn, int linkCardinality, List<AttributeInContext> mappedFields ) {
	private String getJoinColumnAnnotation(JoinColumnInContext joinColumn, LinkInContext link, List<AttributeInContext> alreadyMappedFields ) {
		StringBuilder annotation = new StringBuilder();
		annotation.append( "@JoinColumn(");
		annotation.append( "name=\"" + joinColumn.getName()+"\"" );
		annotation.append( ", " );
		annotation.append( "referencedColumnName=\"" + joinColumn.getReferencedColumnName()+"\"" );
		// TODO 
		// columnDefinition
		// nullable
		// table
		// unique
		//if ( linkCardinality == MANY_TO_ONE || linkCardinality == ONE_TO_ONE ) {
		if ( link.isCardinalityManyToOne() || link.isCardinalityOneToOne() ) {
			/*
			 * Defining "insertable=false, updatable=false" is useful when you need 
			 * to map a field more than once in an entity, typically:
			 *  - when using a composite key
			 *  - when using a shared primary key
			 *  - when using cascaded primary keys
			 */
			if ( isColumnAlreadyMappedAsAField (joinColumn, alreadyMappedFields ) ) {
				annotation.append( ", " );
				annotation.append( "insertable=false" ); 
				annotation.append( ", " );
				annotation.append( "updatable=false" ); 
			}
		}
		annotation.append( ")");
		return annotation.toString();
	}
	
	//private boolean isFieldAlreadyMapped (JoinColumn joinColumn, List<AttributeInContext> mappedFields ) {
	/**
	 * Returns TRUE if the given 'join column' is already mapped as a simple field 
	 * @param joinColumn
	 * @param alreadyMappedFields list of all the fields already mapped 
	 * @return
	 */
	private boolean isColumnAlreadyMappedAsAField (JoinColumnInContext joinColumn, List<AttributeInContext> alreadyMappedFields ) {
		if ( alreadyMappedFields != null ) {
			String dbColumnName = joinColumn.getName(); // ie "PUBLISHER_ID" in "BOOK"
			if ( dbColumnName != null ) {
				for ( AttributeInContext field : alreadyMappedFields ) {
					if ( dbColumnName.equals( field.getDatabaseName() ) ) {
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
//	/**
//	 * Returns the JPA annotations without left margin 
//	 * Usage : $jpa.fieldAnnotations() 
//	 * @return
//	 */
//	@VelocityMethod(
//			text={	
//				"Returns the JPA annotations for the attribute (without left margin)"
//				}
//			)
//	public String fieldAnnotations(JavaBeanClassAttribute attribute)
//    {
//		return fieldAnnotations(0, attribute);
//    }
//
//	/**
//	 * Returns the JPA annotations for EmbeddedID without left margin 
//	 * Usage : $x.jpaAnnotationsEmbeddedID() 
//	 * @return
//	 */
//	@VelocityMethod(
//		text={	
//			"Returns the JPA annotations for EmbeddedID (without left margin)"
//			}
//		)
//	public String embeddedIdAnnotations(JavaBeanClassAttribute attribute)
//    {
//		return embeddedIdAnnotations(0, attribute);
//    }
	
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
	public String fieldAnnotations(int iLeftMargin, AttributeInContext attribute )
    {
		AnnotationsForJPA annotationsJPA = new AnnotationsForJPA(attribute);
		return annotationsJPA.getJpaAnnotations(iLeftMargin, AnnotationsForJPA.EMBEDDED_ID_FALSE );
    }

	//-------------------------------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the JPA annotations for an 'embedded id' (with a left margin)",
			"( there's no '@Id' for an embedded id )"
			},
		example={ 
			"$jpa.embeddedIdAnnotations( 4, $field )" },
		parameters = { 
			"leftMargin : the left margin (number of blanks) ",
			"field : the field to be annotated "
			},
		since = "2.0.7"
		)
	public String embeddedIdAnnotations(int iLeftMargin, AttributeInContext attribute )
    {
		AnnotationsForJPA annotationsJPA = new AnnotationsForJPA(attribute);
		return annotationsJPA.getJpaAnnotations(iLeftMargin, AnnotationsForJPA.EMBEDDED_ID_TRUE );
    }
	//-------------------------------------------------------------------------------------------------------------
	
}
