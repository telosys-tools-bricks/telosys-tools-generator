package junit.env.telosys.tools.generator.fakemodel;

import org.telosys.tools.commons.StrUtil;
import org.telosys.tools.generic.model.ForeignKeyAttribute;

public class FakeForeignKeyAttribute implements ForeignKeyAttribute  {

	private final int    ordinal ;

	private final String originAttributeName ;
	
	private final String referencedAttributeName ;

	/**
	 * Constructor
	 * @param ordinal
	 * @param originAttributeName
	 * @param referencedAttributeName
	 */
	public FakeForeignKeyAttribute(int ordinal, String originAttributeName, String referencedAttributeName) {
		super();
		if (ordinal < 0 ) {
			throw new IllegalArgumentException("invalid FK attribute : ordinal < 0");
		}
		if (StrUtil.nullOrVoid(originAttributeName) ) {
			throw new IllegalArgumentException("invalid FK attribute : origin attribute is null or empty");
		}
		if (StrUtil.nullOrVoid(referencedAttributeName) ) {
			throw new IllegalArgumentException("invalid FK attribute : referenced attribute is null or empty");
		}
		this.ordinal = ordinal;
		this.originAttributeName = originAttributeName;
		this.referencedAttributeName = referencedAttributeName;
	}

	@Override
	public int getOrdinal() {
		return ordinal;
	}

	@Override
	public String getOriginAttributeName() {
		return originAttributeName;
	}

	@Override
	public String getReferencedAttributeName() {
		return referencedAttributeName;
	}

}
