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

import java.io.IOException;
import java.io.Writer;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
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
public class ErrorDirective extends AbstractLineDirective {

	public ErrorDirective() {
		super("error", 1);
	}

	@Override
	// InternalContextAdapter contains everything Velocity needs to know about the template in order to render it
	// Writer is our template writer where we are going to write the result.
	// Node object contains information about our directive (its parameters and properties) 
	// Return : True if the directive rendered successfully.
	public boolean render(InternalContextAdapter context, Writer writer, Node node)
			throws IOException, ResourceNotFoundException, ParseErrorException,
			MethodInvocationException {
		
		checkArgumentsCount(node);
		
		// The error message ( 1rst argument of the directive )
		String errorMessage = getArgumentAsString(0, node, context);
		
		throw new DirectiveException( errorMessage, this.getName(), node.getTemplateName(), node.getLine() );
	}
	
}
