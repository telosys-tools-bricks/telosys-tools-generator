package junit.env.telosys.tools.generator.fakemodel;

import org.telosys.tools.generic.model.ForeignKeyColumn;

public class FakeForeignKeyColumn implements ForeignKeyColumn{

	private final String columnName ;
	private final String referencedColumnName ;
	private final int    sequence ;
	
	public FakeForeignKeyColumn(String columnName, String referencedColumnName, int sequence) {
		super();
		this.columnName = columnName;
		this.referencedColumnName = referencedColumnName;
		this.sequence = sequence;
	}

	@Override
	public String getColumnName() {
		return columnName;
	}

	@Override
	public String getReferencedColumnName() {
		return referencedColumnName;
	}

	@Override
	public int getSequence() {
		return sequence;
	}

}
