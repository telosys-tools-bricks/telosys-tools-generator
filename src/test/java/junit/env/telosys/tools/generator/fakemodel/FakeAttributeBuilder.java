package junit.env.telosys.tools.generator.fakemodel;

import java.util.LinkedList;
import java.util.List;

import org.telosys.tools.dsl.model.DslModelAttribute;
import org.telosys.tools.generator.context.AttributeInContext;
import org.telosys.tools.generator.context.EntityInContext;
import org.telosys.tools.generator.context.EnvInContext;
import org.telosys.tools.generic.model.Attribute;

public class FakeAttributeBuilder {

	private FakeAttributeBuilder() {
	}

	public static List<AttributeInContext> buildAttributes() {
		return buildAttributes(new EnvInContext());
	}
	
	public static List<AttributeInContext> buildAttributes(EnvInContext envInContext) {
		EntityInContext entityInContext = FakeEntityBuilder.buildEntityInContext("Foo", "FOO");
		List<AttributeInContext> attributes = new LinkedList<>();
		attributes.add( buildAttributeNotNull(entityInContext, "id", "int", envInContext) );
		attributes.add( buildAttribute(entityInContext, "name",      "string", envInContext ) );
		attributes.add( buildAttribute(entityInContext, "flag",      "boolean", envInContext ) );
		attributes.add( buildAttribute(entityInContext, "birthDate", "date", envInContext ) );
		attributes.add( buildAttribute(entityInContext, "id2",       "uuid", envInContext ) );
		return attributes;
	}
	
	public static AttributeInContext buildAttribute(String entityName, String attributeName, String attributeType, EnvInContext envInContext) {
		return buildAttribute(FakeEntityBuilder.buildEntityInContext(entityName), attributeName, attributeType, envInContext);
		
	}
	public static AttributeInContext buildAttribute(EntityInContext entityInContext, String name, String type, EnvInContext envInContext) {
		Attribute attribute = new DslModelAttribute(name, type);
		return new AttributeInContext(entityInContext, 
				attribute, 
				null, 
				envInContext ) ;
	}
	public static AttributeInContext buildAttributeNotNull(EntityInContext entityInContext, String name, String type, EnvInContext envInContext) {
		DslModelAttribute attribute = new DslModelAttribute(name, type);
		attribute.setNotNull(true);
		return new AttributeInContext(entityInContext, 
				attribute, 
				null, 
				envInContext ) ;
	}
	
}
