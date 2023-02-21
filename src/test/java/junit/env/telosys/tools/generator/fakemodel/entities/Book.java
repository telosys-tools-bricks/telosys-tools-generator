package junit.env.telosys.tools.generator.fakemodel.entities;

import org.telosys.tools.dsl.model.DslModelAttribute;
import org.telosys.tools.dsl.model.DslModelEntity;
import org.telosys.tools.dsl.model.DslModelForeignKeyAttribute;
import org.telosys.tools.generic.model.Attribute;
import org.telosys.tools.generic.model.types.NeutralType;

public class Book extends DslModelEntity {

	public static final String ENTITY_NAME = "Book";

	public Book() {
		super(ENTITY_NAME); 
		this.addAttribute(idAttribute());
		this.addAttribute(titleAttribute());
		this.addAttribute(authorIdAttribute());
		Builder.foreignKey(this, "FK_BOOK_AUTHOR", "Book", "Author", 
				new DslModelForeignKeyAttribute(1, "authorId", "id"));
	}

	private Attribute idAttribute() {
		DslModelAttribute attribute = new DslModelAttribute("id", NeutralType.INTEGER);
		attribute.setKeyElement(true);
		return attribute ;
	}

	private Attribute titleAttribute() {
		DslModelAttribute attribute = new DslModelAttribute("title", NeutralType.STRING);
		return attribute ;
	}

	private Attribute authorIdAttribute() {
		DslModelAttribute attribute = new DslModelAttribute("authorId", NeutralType.INTEGER);
		return attribute ;
	}

}
