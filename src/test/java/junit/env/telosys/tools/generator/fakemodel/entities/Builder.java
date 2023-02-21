package junit.env.telosys.tools.generator.fakemodel.entities;

import org.telosys.tools.dsl.model.DslModelEntity;
import org.telosys.tools.dsl.model.DslModelForeignKey;
import org.telosys.tools.dsl.model.DslModelForeignKeyAttribute;
import org.telosys.tools.dsl.model.DslModelLink;
import org.telosys.tools.dsl.tags.Tag;
import org.telosys.tools.dsl.tags.TagError;
import org.telosys.tools.dsl.tags.Tags;
import org.telosys.tools.generic.model.TagContainer;

public class Builder {

	private Builder() {
	}

	private static void addTag(TagContainer tagContainer, String tagName, String tagValue) {
		Tag tag;
		if ( tagValue != null ) {
			tag = new Tag(tagName, tagValue);
		} else {
			tag = new Tag(tagName);
		}
		try {
			((Tags)tagContainer).addTag(tag);
		} catch (TagError e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void tag(DslModelEntity entity, String tagName, String tagValue) {
		addTag(entity.getTagContainer(), tagName, tagValue);
	}

	public static void tag(DslModelEntity entity, String tagName) {
		addTag(entity.getTagContainer(), tagName, null);
	}

	public static void tag(DslModelLink link, String tagName, String tagValue) {
		addTag(link.getTagContainer(), tagName, tagValue);
	}

	public static void tag(DslModelLink link, String tagName) {
		addTag(link.getTagContainer(), tagName, null);
	}

	public static void foreignKey(DslModelEntity entity, String fkName, String originEntityName, String referencedEntityName, DslModelForeignKeyAttribute... fkAttributes) {
		DslModelForeignKey fk = new DslModelForeignKey(fkName, originEntityName, referencedEntityName);
		for ( DslModelForeignKeyAttribute a : fkAttributes ) {
			fk.addAttribute(a);
		}
		entity.addForeignKey(fk);
	}
}
