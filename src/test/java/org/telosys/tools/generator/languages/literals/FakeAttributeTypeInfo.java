package org.telosys.tools.generator.languages.literals;

import org.telosys.tools.generator.languages.types.AttributeTypeInfo;

/**
 * FAKE AttributeTypeInfo for tests
 * 
 * @author Laurent Guerin
 *
 */
public class FakeAttributeTypeInfo implements AttributeTypeInfo {
    private final String neutralType;
    private boolean notNull;
    private boolean primitiveTypeExpected;
    private boolean objectTypeExpected;
    private boolean unsignedTypeExpected;

    public FakeAttributeTypeInfo(String neutralType) {
        this.neutralType = neutralType;
        this.notNull = false;
        this.primitiveTypeExpected = false;
        this.objectTypeExpected = false;
        this.unsignedTypeExpected = false;
    }

    @Override
    public String getNeutralType() {
        return neutralType;
    }
    @Override
    public boolean isNotNull() {
        return notNull;
    }
    @Override
    public boolean isPrimitiveTypeExpected() {
        return primitiveTypeExpected;
    }
    @Override
    public boolean isObjectTypeExpected() {
        return objectTypeExpected;
    }
    @Override
    public boolean isUnsignedTypeExpected() {
        return unsignedTypeExpected;
    }

    public FakeAttributeTypeInfo notNull() {
        this.notNull = true;
        return this;
    }
    public FakeAttributeTypeInfo nullable() {
        this.notNull = false;
        return this;
    }

    public FakeAttributeTypeInfo primitiveTypeExpected() {
        this.primitiveTypeExpected = true;
        return this;
    }

    public FakeAttributeTypeInfo objectTypeExpected() {
        this.objectTypeExpected = true;
        return this;
    }

    public FakeAttributeTypeInfo unsignedTypeExpected() {
        this.unsignedTypeExpected = true;
        return this;
    }

}
