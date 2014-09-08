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

import org.telosys.tools.generator.GeneratorException;
import org.telosys.tools.generator.context.doc.VelocityMethod;
import org.telosys.tools.generator.context.doc.VelocityObject;
import org.telosys.tools.generator.context.names.ContextName;
import org.telosys.tools.generator.context.tools.LinesBuilder;

//-------------------------------------------------------------------------------------
@VelocityObject(
	contextName=ContextName.JAVA,
	text = { 
		"Object providing a set of utility functions for JAVA language code generation",
		""
	},
	since = "2.0.7"
 )
//-------------------------------------------------------------------------------------
public class Java {

	private final static List<String> VOID_STRINGS_LIST = new LinkedList<String>();

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns a string containing all the code for a Java 'equals' method",
			"Tabulations are used for code indentation"
			},
		example={ 
			"$java.equalsMethod( $entity.name, $entity.attributes )" },
		parameters = { 
			"className : the Java class name (simple name or full name)",
			"fieldsList : list of fields to be used in the equals method"},
		since = "2.0.7"
			)
	public String equalsMethod( String className, List<AttributeInContext> fieldsList ) {
		
		return equalsMethod( className , fieldsList, new LinesBuilder() ); 
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns a string containing all the code for a Java 'equals' method",
			"Spaces are used for code indentation"
			},
		example={ 
			"$java.equalsMethod( $entity.name, $entity.attributes, 4 )" },
		parameters = { 
			"className : the Java class name (simple name or full name)",
			"fieldsList : list of fields to be used in the equals method",
			"indentSpaces : number of spaces to be used for each indentation level"},
		since = "2.0.7"
			)
	public String equalsMethod( String className, List<AttributeInContext> fieldsList, int indentSpaces ) {
		
		return equalsMethod( className , fieldsList, new LinesBuilder(indentSpaces) ); 
	}
	
	//-------------------------------------------------------------------------------------
	private String equalsMethod( String className, List<AttributeInContext> fieldsList, LinesBuilder lb ) {

		int indent = 1 ;
		lb.append(indent, "public boolean equals(Object obj) { ");
		
		indent++;
		lb.append(indent, "if ( this == obj ) return true ; ");
		lb.append(indent, "if ( obj == null ) return false ;");
		lb.append(indent, "if ( this.getClass() != obj.getClass() ) return false ; ");
		
		// Cast, ie : MyClass other = (MyClass) obj;
		lb.append( indent, className + " other = (" + className + ") obj; ");
		
		if ( fieldsList != null ) {
			for ( AttributeInContext attribute : fieldsList ) {
				
				String attributeName = attribute.getName() ;
				lb.append(indent, "//--- Attribute " + attributeName );
				if ( attribute.isPrimitiveType() ) {
					if ( attribute.isFloatType() ) {
						// float
						lb.append(indent, 
								"if ( Float.floatToIntBits(" + attributeName 
								+ ") != Float.floatToIntBits(other." + attributeName + ") ) return false ; ");
					}
					else if ( attribute.isDoubleType() ) {
						// double 
						lb.append(indent, 
								"if ( Double.doubleToLongBits(" + attributeName 
								+ ") != Double.doubleToLongBits(other." + attributeName + ") ) return false ; ");
					}
					else {
						// char, byte, short, int, long, boolean 
						lb.append(indent, "if ( " + attributeName + " != other." + attributeName + " ) return false ; ");
					}
				}
				else if ( attribute.isArrayType() ) {
					// char[], byte[], String[], ...
					lb.append(indent, "if ( ! Arrays.equals(" + attributeName + ", other." + attributeName + ") ) return false ; ");
				}
				else {
					lb.append(indent, "if ( " + attributeName + " == null ) { ");
						lb.append(indent+1, "if ( other." + attributeName + " != null ) ");
							lb.append(indent+2, "return false ; ");
					lb.append(indent, "} else if ( ! " + attributeName + ".equals(other."+attributeName+") ) " );
						lb.append(indent+1, "return false ; ");
				}
			}
		} 
		
		lb.append(indent, "return true; ");
		
		indent--;
		lb.append(indent, "} ");

		return lb.toString();
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
			text={	
				"Returns a string containing all the code for a Java 'hashCode' method",
				"Tabulations are used for code indentation"
				},
			example={ 
				"$java.hashCode( $entity.name, $entity.attributes )" },
			parameters = { 
				"className  : the Java class name (simple name or full name)",
				"fieldsList : list of fields to be used in the equals method"},
			since = "2.0.7"
				)
	public String hashCodeMethod( String className, List<AttributeInContext> fieldsList ) {
		return hashCodeMethod( className , fieldsList, new LinesBuilder() ); 
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
			text={	
				"Returns a string containing all the code for a Java 'hashCode' method",
				"Spaces are used for code indentation"
				},
			example={ 
				"$java.hashCode( $entity.name, $entity.attributes, 4 )" },
			parameters = { 
				"className  : the Java class name (simple name or full name)",
				"fieldsList : list of fields to be used in the equals method",
				"indentSpaces : number of spaces to be used for each indentation level"},
			since = "2.0.7"
				)
	public String hashCodeMethod( String className, List<AttributeInContext> fieldsList, int indentSpaces ) {
		return hashCodeMethod( className , fieldsList, new LinesBuilder(indentSpaces) ); 
	}
	
	//-------------------------------------------------------------------------------------
	private String hashCodeMethod( String className, List<AttributeInContext> fieldsList, LinesBuilder lb ) {

		int indent = 1 ;
		lb.append(indent, "public int hashCode() { ");

		boolean long_temp = false ;
		indent++;
			lb.append(indent, "final int prime = 31; ");
			lb.append(indent, "int result = 1; ");
			lb.append(indent, "");
			
			if ( fieldsList != null ) {
				for ( AttributeInContext attribute : fieldsList ) {
					
					String attributeName = attribute.getName() ;
					lb.append(indent, "//--- Attribute " + attributeName );
					if ( attribute.isPrimitiveType() ) {
						//--- Primitive types
						if ( attribute.isBooleanType() ) {
							// boolean
							lb.append(indent, "result = prime * result + (" + attributeName + " ? 1231 : 1237 );");
						}
						else if ( attribute.isLongType() ) {
							// long (must be converted to int)
							lb.append(indent, "result = prime * result + (int) (" + attributeName 
									+ " ^ (" + attributeName + " >>> 32));");
						}
						else if ( attribute.isFloatType() ) {
							// float
							lb.append(indent, "result = prime * result + Float.floatToIntBits(" + attributeName + ");");
						}
						else if ( attribute.isDoubleType() ) {
							// double
							if ( long_temp == false ) {
								lb.append(indent, "long temp;");
								long_temp = true ;
							}
							lb.append(indent, "temp = Double.doubleToLongBits(" + attributeName + ");");
							lb.append(indent, "result = prime * result + (int) (temp ^ (temp >>> 32));");
						}
						else {
							// char, byte, short, int 
							lb.append(indent, "result = prime * result + " + attributeName + ";");
						}
					}
					else if ( attribute.isArrayType() ) {
						// char[], byte[], String[], ...
						lb.append(indent, "result = prime * result + Arrays.hashCode(" + attributeName + ");");
					}
					else {
						//--- Objects : just use the 'hashCode' method
						lb.append(indent, "result = prime * result + ((" + attributeName + " == null) ? 0 : " 
								+ attributeName + ".hashCode() ) ; ");
					}
				}
			} 

			lb.append(indent, "");
			lb.append(indent, "return result; ");
		indent--;
		lb.append(indent, "} ");

		return lb.toString();
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
			text={	
				"Returns the list of Java classes to be imported for the given attributes",
				""
				},
			example={ 
				"#foreach( $import in $java.imports($entity.attributes) )",
				"import $import;",
				"#end)" },
			parameters = {
				"attributes : list of attributes" },
			since = "2.0.7"
				)
	public List<String> imports( List<AttributeInContext> attributesList ) {
		if ( attributesList != null ) {
			ImportsList imports = new ImportsList();
			for ( AttributeInContext attribute : attributesList ) {
				// register the type to be imported if necessary
				imports.declareType( attribute.getFullType() ); 
			}
			List<String> resultList = imports.getList();
			java.util.Collections.sort(resultList);
			return resultList ;
		}
		return VOID_STRINGS_LIST ;
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
			text={	
				"Returns the list of Java classes to be imported for the given entity",
				"The imports are determined using all the entity attributes  ",
				"and all the 'OneToMany' any 'ManyToMany' links found in the entity"
				},
			example={ 
				"#foreach( $import in $java.imports($entity) )",
				"import $import;",
				"#end)" },
			parameters = {
				"entity : entity to be used " },
			since = "2.0.7"
				)
	public List<String> imports( EntityInContext entity ) throws GeneratorException {
		if ( entity != null ) {
			ImportsList imports = new ImportsList();
			//--- All the attributes
			for ( AttributeInContext attribute : entity.getAttributes() ) {
				// register the type to be imported if necessary
				imports.declareType( attribute.getFullType() ); 
			}
			//--- All the links 
			for ( LinkInContext link : entity.getLinks() ) {
				if ( link.isCardinalityOneToMany() || link.isCardinalityManyToMany() ) {
					// "java.util.List", "java.util.Set", ... 
					imports.declareType( link.getFieldFullType() ); 
				}
				else {
					// ManyToOne or OneToOne => bean ( "Book", "Person", ... )
					// Supposed to be in the same package
				}
			}
			//--- Resulting list of imports
			List<String> resultList = imports.getList();
			java.util.Collections.sort(resultList);
			return resultList ;
		}
		return VOID_STRINGS_LIST ;
	}

	//-------------------------------------------------------------------------------------
	// toString METHOD GENERATION
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns a string containing all the code for a Java 'toString' method",
			"Generates a 'toString' method using all the attributes of the given entity",
			"(excluded types are 'array', 'Clob', 'Blob', and 'Long Text String') "
			},
		example={ 
			"$java.toStringMethod( $entity, 4 )" },
		parameters = { 
			"entity : the entity providing the attributes to be used in the 'toString' method",
			"indentSpaces : number of spaces to be used for each indentation level"},
		since = "2.1.0"
			)
	public String toStringMethod( EntityInContext entity, int indentSpaces ) {
		return toStringMethod(entity.getAttributes(), indentSpaces );
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns a string containing all the code for a Java 'toString' method",
			"Generates a 'toString' method using all the given attributes ",
			"(excluded types are 'array', 'Clob', 'Blob', and 'Long Text String') "
			},
		example={ 
			"$java.toStringMethod( $attributes, 4 )" },
		parameters = { 
			"attributes : list of attributes to be used in the 'toString' method",
			"indentSpaces : number of spaces to be used for each indentation level"},
		since = "2.1.0"
			)
	public String toStringMethod( List<AttributeInContext> attributes, int indentSpaces ) {

		LinesBuilder lb = new LinesBuilder(indentSpaces) ;
		int indent = 1 ;
		lb.append(indent, "public String toString() { ");
		
		indent++;
		lb.append(indent, "StringBuffer sb = new StringBuffer(); ");
		//--- All the given attributes 
		if ( attributes != null ) {
			toStringForAttributes( attributes, lb, indent );
		}
		lb.append(indent, "return sb.toString(); ");
		indent--;
		
		lb.append(indent, "} ");
		return lb.toString();
	}
    
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns a string containing all the code for a Java 'toString' method",
			"Generates a 'toString' method with the primary key attribute or the embedded key ",
			"and the given list of 'non key' attributes if their type is usable in a 'toString' method",
			"(excluded types are 'array', 'Clob', 'Blob', and 'Long Text String') "
			},
		example={ 
			"$java.toStringMethod( $entity, $nonKeyAttributes, $embeddedIdName, 4 )" },
		parameters = { 
			"entity : the entity to be used",
			"nonKeyAttributes : list of attributes that are not in the Primary Key",
			"embeddedIdName : variable name for the embedded id (used only if the entity has a composite primary key) " },
		since = "2.0.7"
			)
	public String toStringMethod( EntityInContext entity, List<AttributeInContext> nonKeyAttributes, String embeddedIdName ) {
			
		return toStringMethod( entity , nonKeyAttributes, embeddedIdName, new LinesBuilder() ); 
	}
		
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns a string containing all the code for a Java 'toString' method",
			"Generates a 'toString' method with the primary key attribute or the embedded key ",
			"and the given list of 'non key' attributes if their type is usable in a 'toString' method",
			"(excluded types are 'array', 'Clob', 'Blob', and 'Long Text String') "
			},
		example={ 
			"$java.toStringMethod( $entity, $nonKeyAttributes, $embeddedIdName, 4 )" },
		parameters = { 
			"entity : the entity to be used",
			"nonKeyAttributes : list of attributes that are not in the Primary Key",
			"embeddedIdName : variable name for the embedded id (used only if the entity has a composite primary key) ",
			"indentSpaces : number of spaces to be used for each indentation level"},
		since = "2.0.7"
			)
	public String toStringMethod( EntityInContext entity, List<AttributeInContext> nonKeyAttributes, String embeddedIdName, int indentSpaces ) {
		
		return toStringMethod( entity , nonKeyAttributes, embeddedIdName, new LinesBuilder(indentSpaces) ); 
	}
	
	//-------------------------------------------------------------------------------------
	private String toStringMethod( EntityInContext entity, List<AttributeInContext> nonKeyAttributes, String embeddedIdName, LinesBuilder lb ) {

		int indent = 1 ;
		lb.append(indent, "public String toString() { ");
		
		indent++;
		lb.append(indent, "StringBuffer sb = new StringBuffer(); ");
		
		int count = 0 ;
		lb.append(indent, "sb.append(\"[\"); ");
		//--- PRIMARY KEY attributes ( composite key or not )
		if ( entity.hasCompositePrimaryKey() && ( embeddedIdName != null ) ) {
			// Embedded id 
			count = count + toStringForEmbeddedId( embeddedIdName, lb, indent );
		}
		else {
			// No embedded id ( or no name for it )
			List<AttributeInContext> keyAttributes = entity.getKeyAttributes() ;
			count = count + toStringForAttributes( keyAttributes, lb, indent );
		}
		lb.append(indent, "sb.append(\"]:\"); ");
		
		//--- NON KEY attributes ( all the attributes that are not in the Primary Key )
		if ( nonKeyAttributes != null ) {
			count = count + toStringForAttributes( nonKeyAttributes, lb, indent );
		}
				
		lb.append(indent, "return sb.toString(); ");
		
		indent--;
		lb.append(indent, "} ");

		return lb.toString();
	}
	
    /**
     * Uses the given attributes except if their type is not usable   
     * @param attributes
     * @param lb
     * @param indent
     * @return
     */
    private int toStringForAttributes( List<AttributeInContext> attributes, LinesBuilder lb, int indent  )
    {    	
    	if ( null == attributes ) return 0 ;
    	int count = 0 ;
    	for ( AttributeInContext attribute : attributes ) {
    		if ( usableInToString( attribute ) ) {
                if ( count > 0 ) // if it's not the first one
                {
        			lb.append(indent, "sb.append(\"|\");" );
                }        		
    			lb.append(indent, "sb.append(" + attribute.getName() + ");" );
    			count++ ;
    		}
    		else {
    			String sLongText = attribute.isLongText() ? " Long Text" : "" ; 
    			lb.append(indent, "// attribute '" + attribute.getName() 
    					+ "' not usable (type = " + attribute.getType() + sLongText + ")");
    		}
    	}
    	return count ;
    }
    
    /**
     * Just use the embedded primary with its own 'toString'
     * @param embeddedIdName
     * @param lb
     * @param indent
     * @return
     */
    private int toStringForEmbeddedId( String embeddedIdName, LinesBuilder lb, int indent  )
    {
		lb.append(indent, "if ( " + embeddedIdName + " != null ) {  ");
		lb.append(indent, "    sb.append(" + embeddedIdName + ".toString());  ");
		lb.append(indent, "}  ");
		lb.append(indent, "else {  ");
		lb.append(indent, "    sb.append( \"(null-key)\" ); ");
		lb.append(indent, "}  ");
		return 1 ;
    }

    /**
     * Returns true if the given type is usable in a 'toString' method
     * @param sType
     * @return
     */
    private boolean usableInToString( AttributeInContext attribute )
    {
    	if ( attribute.isArrayType() ) return false ;
    	if ( attribute.isLongText() ) return false ;
    	
    	String sType = attribute.getType();
    	if ( null == sType ) return false ;
    	String s = sType.trim() ;
    	if ( s.endsWith("Blob") ) return false ; 
    	if ( s.endsWith("Clob") ) return false ; 
    	return true ;
    }
    
}
