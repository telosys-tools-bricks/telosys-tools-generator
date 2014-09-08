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
package org.telosys.tools.generator.directive;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;
/*
 * NB : each user directive must be defines in the Velocity properties
 * Example : 
 * userdirective=com.example.MyDirective1, com.example.MyDirective2
 * 
 * In this project it's defined in the Generator class 
 * 
 */
/**
 * Velocity directive
 * 
 * @author Laurent Guerin
 *
 */
public abstract class AbstractLineDirective extends Directive {

	private final String directiveName ;
	private final int    argumentsCount ;
	
	/**
	 * Constructor
	 * @param directiveName
	 * @param argumentsCount
	 */
	public AbstractLineDirective(String directiveName, int argumentsCount) {
		super();
		this.directiveName = directiveName;
		this.argumentsCount = argumentsCount ;
	}

	@Override
	// returns the name of the directive that will be used in templates.
	public String getName() {
		return this.directiveName ; // for "#error" in the template
	}

	@Override
	// returns BLOCK or LINE constants which determine a directive type.
	public int getType() {
		return LINE; // LINE or BLOCK
	}

	protected void checkArgumentsCount(Node node) {
		if ( node.jjtGetNumChildren() != this.argumentsCount ) {
			//throw new RuntimeException("#" + directiveName + " : " + argumentsCount + " argument(s) expected");
			String message = argumentsCount + " argument(s) expected" ;
			throw new DirectiveException( message, this.getName(), node.getTemplateName(), node.getLine() );
		}
	}

	protected String getArgumentAsString( int index, Node node, InternalContextAdapter context) {
		Object o = node.jjtGetChild(index).value(context);
		if ( o instanceof String ) {
			return String.valueOf(o);
		}
		else {
			//throw new RuntimeException("#" + directiveName + " : invalid argument #" + index + "( String expected )");
			String message = "invalid argument #" + index + "( String expected )" ;
			throw new DirectiveException( message, this.getName(), node.getTemplateName(), node.getLine() );
		}
	}

	protected boolean getArgumentAsBoolean( int index, Node node, InternalContextAdapter context) {
		Object o = node.jjtGetChild(index).value(context);
		if ( o instanceof Boolean ) {
			return (Boolean) o;
		}
		else {
			//throw new RuntimeException("#" + directiveName + " : invalid argument #" + index + "( Boolean expected )");
			String message = "invalid argument #" + index + "( Boolean expected )" ;
			throw new DirectiveException( message, this.getName(), node.getTemplateName(), node.getLine() );
		}
	}
	
	/*
	 * EXEMPLE 
	 *        Node childNode = node.jjtGetChild(0);
        if ( childNode.getType() !=  ParserTreeConstants.JJTSTRINGLITERAL &&
             childNode.getType() !=  ParserTreeConstants.JJTREFERENCE )
        {
           throw new TemplateInitException(
                   "#" + getName() + "()  argument must be a string literal or reference", 
                   context.getCurrentTemplateName(),
                   childNode.getColumn(),
                   childNode.getLine());
        }
	 */
}
