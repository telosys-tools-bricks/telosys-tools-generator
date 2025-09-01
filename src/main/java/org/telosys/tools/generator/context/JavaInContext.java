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

import org.telosys.tools.generator.context.doc.VelocityMethod;
import org.telosys.tools.generator.context.doc.VelocityObject;
import org.telosys.tools.generator.context.names.ContextName;
import org.telosys.tools.generator.context.tools.JavaValidationAnnotations;
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
public class JavaInContext {

	private static final List<String> VOID_STRINGS_LIST = new LinkedList<>();

	private String buildIndentationWithSpaces(int nSpaces) {
		StringBuilder spaces = new StringBuilder();
		for ( int n = 0 ; n < nSpaces ; n++ ) {
			spaces.append(" ");
		}
		return spaces.toString();
	}
	
	//=====================================================================================
	// "equals" METHOD GENERATION
	//=====================================================================================
	@VelocityMethod(
		text={	
			"Returns a string containing all the code for a Java 'equals' method",
			"Tabulations are used for code indentation",
			"(!) DEPRECATED - do not use"
			},
		example={ 
			"$java.equalsMethod( $entity.name, $entity.attributes )" },
		parameters = { 
			"className : the Java class name (simple name or full name)",
			"attributes : list of attributes to be used in the equals method"},
		since = "2.0.7",
		deprecated=true
			)
	@Deprecated
	public String equalsMethod( String className, List<AttributeInContext> attributes ) {
		return buildEqualsMethod( className , attributes, 1, new LinesBuilder() ); 
	}
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns a string containing all the code for a Java 'equals' method",
			"Spaces are used for code indentation",
			"(!) DEPRECATED - do not use"
			},
		example={ 
			"$java.equalsMethod( $entity.name, $entity.attributes, 4 )" },
		parameters = { 
			"className : the Java class name (simple name or full name)",
			"attributes : list of attributes to be used in the equals method",
			"indentSpaces : number of spaces to be used for each indentation level"},
		since = "2.0.7",
		deprecated=true
			)
	@Deprecated
	public String equalsMethod( String className, List<AttributeInContext> attributes, int indentSpaces ) {
		return buildEqualsMethod( className , attributes, 1, new LinesBuilder(buildIndentationWithSpaces(indentSpaces)) ); 
	}
	//-------------------------------------------------------------------------------------
	// "equalsMethod" with standard arguments (since v 4.3.0)
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns a string containing all the code for a Java 'equals' method",
			"Generates the method using all the attributes of the given entity",
			"Indentation with TABS (1 tab for each indentation level)"
			},
		example={ 
			"$java.equalsMethod( $entity, 1 )" },
		parameters = { 
			"entity : the entity for which to generate the 'equals' method",
			"indentationLevel : initial indentation level" },
		since = "4.3.0"
		)
	public String equalsMethod(EntityInContext entity, int indentationLevel) {
		return buildEqualsMethod(entity, indentationLevel, new LinesBuilder()); 
	}
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns a string containing all the code for a Java 'equals' method",
			"Generates the method using all the attributes of the given entity",
			"Indentation with SPACES (1 'indentationString' for each indentation level)"
			},
		example={ 
			"$java.equalsMethod( $entity, 1, '   ' )" },
		parameters = { 
			"entity : the entity for which to generate the 'equals' method",
			"indentationLevel : initial indentation level",
			"indentationString : string to use for each indentation (usually N spaces)"},
		since = "4.3.0"
		)
	public String equalsMethod(EntityInContext entity, int indentationLevel, String indentationString) {
		return buildEqualsMethod(entity, indentationLevel, new LinesBuilder(indentationString) ); 
	}
	//-------------------------------------------------------------------------------------
	// No 'attributes' paramater for 'equals' (must use all the attributes) 
	//-------------------------------------------------------------------------------------
	private String buildEqualsMethod(EntityInContext entity, int indentationLevel, LinesBuilder linesBuilder) {
    	if ( entity == null ) {
    		throw new IllegalArgumentException("$java.equalsMethod(..) : entity arg is null");
    	}
		return buildEqualsMethod(entity.getName(), entity.getAttributes(), indentationLevel, linesBuilder ); 
	}
	//-------------------------------------------------------------------------------------
	private String buildEqualsMethod( String className, List<AttributeInContext> attributes, int indent, LinesBuilder lb ) {
		lb.append(indent, "public boolean equals(Object obj) { ");	
		
		indent++;
		lb.append(indent, "if ( this == obj ) return true ; ");
		lb.append(indent, "if ( obj == null ) return false ;");
		lb.append(indent, "if ( this.getClass() != obj.getClass() ) return false ; ");
		// Cast obj to the given className 
		lb.append( indent, className + " other = (" + className + ") obj; ");
		if ( attributes != null ) {
			for ( AttributeInContext attribute : attributes ) {
				
				String attributeName = attribute.getName() ;
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
				else if ( isJavaArrayType(attribute) ) {
					// char[], byte[], String[], ...
					// "java.util" prefix is used to avoid the import 
					lb.append(indent, "if ( ! java.util.Arrays.equals(" + attributeName + ", other." + attributeName + ") ) return false ; ");
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

	//=====================================================================================
	// "hashCode" METHOD GENERATION
	//=====================================================================================
	@VelocityMethod(
		text={	
			"Returns a string containing all the code for a Java 'hashCode' method",
			"Tabulations are used for code indentation",
			"(!) DEPRECATED - do not use"
			},
		example={ 
			"$java.hashCodeMethod( $entity.name, $entity.attributes )" },
		parameters = { 
			"className  : the Java class name (simple name or full name)",
			"attributes : list of attributes to be used in the equals method"},
		since = "2.0.7",
		deprecated=true
			)
	@Deprecated
	public String hashCodeMethod( String className, List<AttributeInContext> attributes ) {
		return buildHashCodeMethod(attributes, 1, new LinesBuilder() ); 
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns a string containing all the code for a Java 'hashCode' method",
			"Spaces are used for code indentation",
			"(!) DEPRECATED - do not use"
			},
		example={ 
			"$java.hashCodeMethod( $entity.name, $entity.attributes, 4 )" },
		parameters = { 
			"className  : the Java class name (simple name or full name)",
			"attributes : list of attributes to be used in the equals method",
			"indentSpaces : number of spaces to be used for each indentation level"},
		since = "2.0.7",
		deprecated=true
			)
	@Deprecated
	public String hashCodeMethod( String className, List<AttributeInContext> attributes, int indentSpaces ) {
		return buildHashCodeMethod(attributes, 1, new LinesBuilder(buildIndentationWithSpaces(indentSpaces)) ); 
	}
	//-------------------------------------------------------------------------------------
	// "hashCodeMethod" with standard arguments (since v 4.3.0)
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns a string containing all the code for a Java 'hashCode' method",
			"Generates the method using all the attributes of the given entity",
			"Indentation with TABS (1 tab for each indentation level)"
			},
		example={ 
			"$java.hashCodeMethod( $entity, 1 )" },
		parameters = { 
			"entity : the entity for which to generate the method",
			"indentationLevel : initial indentation level (usually 1)" },
		since = "4.3.0"
		)
	public String hashCodeMethod(EntityInContext entity, int indentationLevel) {
		return buildHashCodeMethod(entity, indentationLevel, new LinesBuilder()); 
	}
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns a string containing all the code for a Java 'hashCode' method",
			"Generates the method using all the attributes of the given entity",
			"Indentation with SPACES (1 'indentationString' for each indentation level)"
			},
		example={ 
			"$java.hashCodeMethod( $entity, 1, '   ' )" },
		parameters = { 
			"entity : the entity for which to generate the method",
			"indentationLevel : initial indentation level (usually 1)",
			"indentationString : string to use for each indentation (usually N spaces)"},
		since = "4.3.0"
		)
	public String hashCodeMethod(EntityInContext entity, int indentationLevel, String indentationString) {
		return buildHashCodeMethod(entity, indentationLevel, new LinesBuilder(indentationString) ); 
	}
	
	//-------------------------------------------------------------------------------------
	// No 'attributes' paramater for 'hashCode' (must use all the attributes) 
	//-------------------------------------------------------------------------------------
	private String buildHashCodeMethod(EntityInContext entity, int indentationLevel, LinesBuilder linesBuilder) {
    	if ( entity == null ) {
    		throw new IllegalArgumentException("$java.hashCodeMethod(..) : entity arg is null");
    	}
		return buildHashCodeMethod(entity.getAttributes(), indentationLevel, linesBuilder ); 	
	}
	//-------------------------------------------------------------------------------------
	private boolean atLeastOneDoubleAttribute(List<AttributeInContext> attributes) {
		for ( AttributeInContext attribute : attributes ) {
			if ( attribute.isPrimitiveType() && attribute.isDoubleType() ) {
				// neutral type 'double' + 'primitive type' => Java 'double'
				return true;
			}
		}
		return false;
	}
	//-------------------------------------------------------------------------------------
	private String buildHashCodeMethod(List<AttributeInContext> attributes, int indent, LinesBuilder lb ) {
		lb.append(indent, "public int hashCode() { ");
		indent++;
		lb.append(indent, "final int prime = 31; ");
		lb.append(indent, "int result = 1; ");
		// Declare a 'temp' variable if used for 'Double' 
		if ( atLeastOneDoubleAttribute(attributes) ) {
			lb.append(indent, "long temp;");
		}
		// Compute hash code for each attribute
		if ( attributes != null ) {
			for ( AttributeInContext attribute : attributes ) {
				buildHashCodeMethodForAttribute(attribute, indent, lb );
			}
		} 
		lb.append(indent, "return result; ");
		indent--;
		lb.append(indent, "} ");
		return lb.toString();
	}
	private void buildHashCodeMethodForAttribute(AttributeInContext attribute, int indent, LinesBuilder lb ) {
		String attributeName = attribute.getName() ;
		if ( attribute.isPrimitiveType() ) {
			//--- Primitive types
			if ( attribute.isBooleanType() ) {
				// Java 'boolean'
				lb.append(indent, "result = prime * result + (" + attributeName + " ? 1231 : 1237 );");
			}
			else if ( attribute.isLongType() ) {
				// Java 'long' (must be converted to int)
				lb.append(indent, "result = prime * result + (int) (" + attributeName 
						+ " ^ (" + attributeName + " >>> 32));");
			}
			else if ( attribute.isFloatType() ) {
				// Java 'float'
				lb.append(indent, "result = prime * result + Float.floatToIntBits(" + attributeName + ");");
			}
			else if ( attribute.isDoubleType() ) {
				// Java 'double' ('temp' var is declared before)
				lb.append(indent, "temp = Double.doubleToLongBits(" + attributeName + ");");
				lb.append(indent, "result = prime * result + (int) (temp ^ (temp >>> 32));");
			}
			else {
				// Other primitive types : char, byte, short, int 
				lb.append(indent, "result = prime * result + " + attributeName + ";");
			}
		}
		else if ( isJavaArrayType(attribute) ) { 
			//--- Array type : char[], byte[], String[], ...
			// "java.util" prefix is used to avoid the import 
			lb.append(indent, "result = prime * result + java.util.Arrays.hashCode(" + attributeName + ");");
		}
		else {
			//--- Other objects : just use the 'hashCode' method
			lb.append(indent, "result = prime * result + ((" + attributeName + " == null) ? 0 : " 
					+ attributeName + ".hashCode() ) ; ");
		}
	}

	//-------------------------------------------------------------------------------------
	// "imports"
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
			text={	
				"Returns the list of Java classes to be imported for the given attributes",
				""
				},
			example={ 
				"#foreach( $import in $java.imports($entity.attributes) )",
				"import $import;",
				"#end" },
			parameters = {
				"attributes : list of attributes" },
			since = "2.0.7"
				)
	public List<String> imports( List<AttributeInContext> attributesList ) {
		if ( attributesList != null ) {
			JavaImportsList imports = new JavaImportsList();
			for ( AttributeInContext attribute : attributesList ) {
				// register the type to be imported if necessary
				imports.declareType( attribute.getFullType() ); 
			}
			return imports.getFinalImportsList();			
		}
		return VOID_STRINGS_LIST ;
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
			text={	
				"Returns the list of Java classes to be imported for the given entity",
				"The imports are determined using all the entity attributes and links ",
				"Examples for attributes : 'java.time.LocalDateTime', 'java.math.BigDecimal', etc",
				"Examples for links : 'java.util.List', etc"
				},
			example={ 
				"#foreach( $import in $java.imports($entity) )",
				"import $import;",
				"#end" },
			parameters = {
				"entity : entity to be used " },
			since = "2.0.7"
				)
	public List<String> imports( EntityInContext entity ) {
		if ( entity != null ) {
			JavaImportsList imports = new JavaImportsList();
			//--- All the attributes
			for ( AttributeInContext attribute : entity.getAttributes() ) {
				// register the type to be imported if necessary
				imports.declareType( attribute.getFullType() ); 
			}
			//--- All the links 
			for ( LinkInContext link : entity.getLinks() ) {
				if ( link.isCardinalityOneToMany() || link.isCardinalityManyToMany() ) {
					//--- Collection type: List, Set, Map, etc
					imports.declareLinkType(link.getFieldType()); // "List<Foo>", "Set<Foo>", etc // ver 4.3.0
				}
				// ELSE = other cardinalities ('ManyToOne' or 'OneToOne') 
				// => just the referenced class name ( "Book", "Person", ... ) 
				// => Supposed to be in the same package => no import 
			}
			//--- Resulting list of imports
			return imports.getFinalImportsList();
		}
		return VOID_STRINGS_LIST ;
	}

	//-------------------------------------------------------------------------------------
	// "toString" METHOD GENERATION
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns a string containing all the code for a Java 'toString()' method",
			"Generates a 'toString' method using all the attributes of the given entity",
			"(except non-printable attributes)",
			"Indentation with TABS (1 tab for each indentation level)"
			},
		example={ 
			"$java.toStringMethod( $entity, 2 )" },
		parameters = { 
			"entity : the entity for which to generate the 'toString' method",
			"indentationLevel : initial indentation level" },
		since = "4.1.0"
			)
	public String toStringMethod( EntityInContext entity, int indentationLevel ) {
		return buildToStringMethod( entity, entity.getAttributes(), indentationLevel, new LinesBuilder() ); 		
	}
	
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns a string containing all the code for a Java 'toString()' method",
			"Generates a 'toString' method using all the attributes of the given entity",
			"(except non-printable attributes)",
			"Indentation with SPACES (1 'indentationString' for each indentation level)"
			},
		example={ 
			"$java.toStringMethod( $entity, 2, '  ' )" },
		parameters = { 
			"entity : the entity for which to generate the 'toString' method",
			"indentationLevel : initial indentation level",
			"indentationString : string to use for each indentation (usually N spaces)"},
		since = "4.1.0"
			)
	public String toStringMethod( EntityInContext entity, int indentationLevel, String indentationString ) {
		return buildToStringMethod( entity, entity.getAttributes(), indentationLevel, new LinesBuilder(indentationString) ); 
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns a string containing all the code for a Java 'toString()' method",
			"Generates a 'toString' method using the given attributes ",
			"(except non-printable attributes)",
			"Indent with TABS (1 tab for each indentation level)"
			},
		example={ 
			"$java.toStringMethod( $entity, $attributes, 2 )" },
		parameters = { 
			"entity : the entity for which to generate the 'toString' method",
			"attributes : list of attributes to be used in the 'toString' method",
			"indentationLevel : initial indentation level" },
		since = "4.1.0"
			)
	public String toStringMethod( EntityInContext entity, List<AttributeInContext> attributes, int indentationLevel ) {
		return buildToStringMethod( entity, attributes, indentationLevel, new LinesBuilder() ); 
	}

	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns a string containing all the code for a Java 'toString()' method",
			"Generates a 'toString' method using the given attributes ",
			"(except non-printable attributes)",
			"Indentation with spaces (1 'indentationString' for each indentation level)"
			},
		example={ 
			"$java.toStringMethod( $entity, $attributes, 2, '  ' )" },
		parameters = { 
			"entity : the entity for which to generate the 'toString' method",
			"attributes : list of attributes to be used in the 'toString' method",
			"indentationLevel : initial indentation level",
			"indentationString : string to use for each indentation (usually N spaces) "},
		since = "4.1.0"
			)
	public String toStringMethod( EntityInContext entity, List<AttributeInContext> attributes, int indentationLevel, String indentationString ) {
		return buildToStringMethod( entity, attributes, indentationLevel, new LinesBuilder(indentationString) ); 
	}

    /**
     * Returns true if Java array type: char[], byte[], String[], ...
     * @param attribute
     * @return
     * @since v 3.0.0
     */
    private boolean isJavaArrayType( AttributeInContext attribute ) {
    	String type = attribute.getSimpleType();
		return ( type != null && type.trim().endsWith("]") );    	
    }
    private boolean isJavaSpecialType( AttributeInContext attribute ) {
    	String type = attribute.getType();
    	if ( type != null ) {
        	String s = type.trim() ;
        	// java.sql.Blob
        	// java.sql.Clob
        	// java.sql.NClob
        	// java.sql.Array
        	return ( s.endsWith("Blob") || s.endsWith("Clob") || s.endsWith("Array") ) ;
    	}
    	return false ;    	
    }

	//-------------------------------------------------------------------------------------
	/**
	 * Returns a string containing all the code for the "toString" method
	 * @param entity
	 * @param attributes
	 * @param indentLevel
	 * @param lb
	 * @return
	 */
	private String buildToStringMethod( EntityInContext entity, List<AttributeInContext> attributes, int indentLevel, LinesBuilder lb ) {
    	if ( entity == null ) {
    		throw new IllegalArgumentException("$java.toStringMethod(..) : entity arg is null");
    	}
    	if ( attributes == null ) {
    		throw new IllegalArgumentException("$java.toStringMethod(..) : attributes arg is null");
    	}
		int indent = indentLevel ;
		lb.append(indent, "public String toString() { ");
		indent++;
    	if ( attributes.isEmpty() ) {
    		//--- No attributes
    		lb.append(indent, "return \"" + entity.getName() + " [no attribute]\" ;");
    	}
    	else {
    		//--- Build return concat with all the given attributes 
    		buildToStringMethodBody( entity, attributes, indent, lb );
    	}
		indent--;
		lb.append(indent, "}");
		return lb.toString();
	}
	
    /**
     * Builds the body of the "toString" method using the given LinesBuilder
     * @param entity
     * @param attributes
     * @param indentationLevel
     * @param lb
     */
    private void buildToStringMethodBody( EntityInContext entity, List<AttributeInContext> attributes, int indentationLevel, LinesBuilder lb) 
    {    	
    	if ( null == attributes ) return ;
    	int count = 0 ;
    	// first lines
    	lb.append(indentationLevel, "String separator = \"|\";");
		lb.append(indentationLevel, "StringBuilder sb = new StringBuilder();"); 
		lb.append(indentationLevel, "sb.append(\"" + entity.getName() + "[\");");  // append the class name, example : sb.append("Employee[")
    	for ( AttributeInContext attribute : attributes ) {
    		if ( isUsableInToString( attribute ) ) {
    			String startOfLine = "";
                if ( count > 0 ) {
                	startOfLine = "sb.append(separator)" ; // not the first one => append separator before
                }
                else {
                	startOfLine = "sb" ; // first one => no separator before
                }
    			lb.append(indentationLevel, startOfLine + ".append(\"" + attribute.getName() + "=\").append(" + attribute.getName() + ");"); 
    			// example: sb.append("firstName=").append(firstName) 
    			count++ ;
    		}
    		else {
    			lb.append(indentationLevel, "// attribute '" + attribute.getName() + "' (type " + attribute.getType() + ") not usable in toString() " );
    		}
    	}
    	// last line
    	lb.append(indentationLevel, "sb.append(\"]\");" ); 
		lb.append(indentationLevel, "return sb.toString();" );
    }

    /**
     * Returns true if the given type is usable in a 'toString' method
     * @param attribute
     * @return
     */
    private boolean isUsableInToString( AttributeInContext attribute ) {
    	return ! (   
    		   attribute.isBinaryType()   // neutral type is 'binary' => Java 'byte[]'
    		|| attribute.isLongText()     // neutral type is 'string' + @LongText => Java 'String' but 'long string'
    		//--- check other special types (not supposed to happen)
    		|| isJavaArrayType(attribute) 
    		|| isJavaSpecialType(attribute) 
    		) ;
    }
	
	//-------------------------------------------------------------------------------------
	// "validationAnnotations" METHOD GENERATION
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns the 'Bean Validation (JSR-303)' or 'Jakarta EE' annotations for the given attribute",
			"All annotations are grouped on a single line",
			"Annotation from packages 'javax.validation.constraints' or 'jakarta.validation.constraints' ",
			"For example: @NotNull, @NotBlank, @Size, @Min, @Max, etc",
			"Do not forget to import the package you want to use: ",
			"    import jakarta.validation.constraints.*; ",
			"or  import javax.validation.constraints.*;   "
			},
		example={ 
			"$java.validationAnnotations(4, $attribute)" },
		parameters = { 
			"leftMargin : the left margin size (number of blanks) ",
			"attribute : the attribute to be annotated "
			},
		since = "4.3.0"
	)
	public String validationAnnotations(int leftMargin, AttributeInContext attribute) {
		JavaValidationAnnotations annotations = new JavaValidationAnnotations(attribute);
		return annotations.getValidationAnnotations(leftMargin );
    }
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Same as 'validationAnnotations' but with multiline result (one line for each annotation)"
			},
		example={ 
			"$java.validationAnnotationsMultiline(4, $attribute)" },
		parameters = { 
			"leftMargin : the left margin size (number of blanks) ",
			"attribute : the attribute to be annotated "
			},
		since = "4.3.0"
	)
	public String validationAnnotationsMultiline(int leftMargin, AttributeInContext attribute) {
		JavaValidationAnnotations annotations = new JavaValidationAnnotations(attribute);
		return annotations.getValidationAnnotationsMultiline(leftMargin );
    }
	//-------------------------------------------------------------------------------------
	@VelocityMethod(
		text={	
			"Returns TRUE if the attribute has at least one validation annotation ",
			"Useful to avoid a blank line if no annotation"
			},
		example={ 
			"#if( $java.hasValidationAnnotations($attribute) )",
			"$java.validationAnnotations(4, $attribute)",
			"#end" },
		parameters = { 
			"leftMargin : the left margin size (number of blanks) ",
			"attribute : the attribute to be annotated "
			},
		since = "4.3.0"
	)
	public boolean hasValidationAnnotations(AttributeInContext attribute) {
		JavaValidationAnnotations annotations = new JavaValidationAnnotations(attribute);
		return annotations.hasValidationAnnotations();
    }
}
