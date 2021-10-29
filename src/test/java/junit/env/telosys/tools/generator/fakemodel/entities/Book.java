package junit.env.telosys.tools.generator.fakemodel.entities;

import org.telosys.tools.generic.model.Attribute;
import org.telosys.tools.generic.model.ForeignKey;
import org.telosys.tools.generic.model.types.NeutralType;

import junit.env.telosys.tools.generator.fakemodel.FakeAttribute;
import junit.env.telosys.tools.generator.fakemodel.FakeEntity;
import junit.env.telosys.tools.generator.fakemodel.FakeForeignKey;
import junit.env.telosys.tools.generator.fakemodel.FakeForeignKeyColumn;

public class Book extends FakeEntity {

	public static final String ENTITY_NAME = "Book";

	public Book() {
		super(ENTITY_NAME, ""); // no table name
		storeAttribute(idAttribute());
		storeAttribute(titleAttribute());
		storeAttribute(authorIdAttribute());
		storeForeignKey(authorFK());
	}

	private ForeignKey authorFK() {
		FakeForeignKey fk = new FakeForeignKey("FK_BOOK_AUTHOR", "BOOK", "AUTHOR");
		fk.addColumn(new FakeForeignKeyColumn("AUTHOR_ID", "ID", 1)) ;
		return fk ;
	}

	
	private Attribute idAttribute() {
		FakeAttribute attribute = new FakeAttribute("id", NeutralType.INTEGER, true);
		return attribute ;
	}

	private Attribute titleAttribute() {
		FakeAttribute attribute = new FakeAttribute("title", NeutralType.STRING, false);
//		attribute.setDatabaseName("FIRST_NAME");
//		attribute.setDatabaseType("VARCHAR");
//		attribute.setDatabaseDefaultValue("default-firts-name");
		return attribute ;
	}

	private Attribute authorIdAttribute() {
		FakeAttribute attribute = new FakeAttribute("authorId", NeutralType.INTEGER, false);
		return attribute ;
	}
}
