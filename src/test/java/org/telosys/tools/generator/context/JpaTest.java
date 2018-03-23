package org.telosys.tools.generator.context;

import junit.env.telosys.tools.generator.fakemodel.AttributeInFakeModel;
import junit.env.telosys.tools.generator.fakemodel.EntityInFakeModel;
import org.junit.Test;
import org.telosys.tools.dsl.generic.model.GenericLink;
import org.telosys.tools.dsl.generic.model.GenericModel;
import org.telosys.tools.generator.GeneratorException;
import org.telosys.tools.generic.model.Cardinality;
import org.telosys.tools.generic.model.CascadeOptions;
import org.telosys.tools.generic.model.Entity;
import org.telosys.tools.generic.model.JoinColumn;
import org.telosys.tools.generic.model.types.NeutralType;
import org.telosys.tools.repository.model.JoinColumnInDbModel;
import org.telosys.tools.repository.model.JoinTableInDbModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JpaTest {

    @Test
    public void multipleJoinColumnsDoNotHaveInsertableUpdateableAttributesWhenNotAlreadyUsed() throws GeneratorException {
        Jpa jpa = new Jpa();
        Entity entity = stubEntity();

        GenericModel genericModel = new GenericModel();
        genericModel.getEntities().add(stubEntity());

        ModelInContext modelInContext = new ModelInContext(genericModel, "whatever", new EnvInContext());
        EntityInContext entityInContext = new EntityInContext(entity, "table1", modelInContext, new EnvInContext());
        modelInContext.getAllEntites().add(entityInContext);

        LinkInContext linkInContext = stubLinkInContext(modelInContext, entityInContext);

        String annotation = jpa.linkAnnotations(5, linkInContext, new ArrayList<AttributeInContext>());
        assertNotNull(annotation);
        String toBe = "     @ManyToOne\n" +
                "     @JoinColumns( { \n" +
                "         @JoinColumn(name=\"join1\", referencedColumnName=\"ref1\"),\n" +
                "         @JoinColumn(name=\"join2\", referencedColumnName=\"ref2\") } )";

        assertEquals(toBe, annotation);
    }

    @Test
    public void multipleJoinColumnsHaveSameInsertableUpdateableAttributesWhenAnyColumnIsAlreadyUsed() throws GeneratorException {
        Jpa jpa = new Jpa();
        Entity entity = stubEntity();

        GenericModel genericModel = new GenericModel();
        genericModel.getEntities().add(stubEntity());

        ModelInContext modelInContext = new ModelInContext(genericModel, "whatever", new EnvInContext());
        EntityInContext entityInContext = new EntityInContext(entity, "table1", modelInContext, new EnvInContext());
        modelInContext.getAllEntites().add(entityInContext);

        LinkInContext linkInContext = stubLinkInContext(modelInContext, entityInContext);
        AttributeInContext attributeInContext = stubAttributeInContext(modelInContext, entityInContext);

        String annotation = jpa.linkAnnotations(5, linkInContext, Arrays.asList(attributeInContext));
        assertNotNull(annotation);
        String toBe = "     @ManyToOne\n" +
                "     @JoinColumns( { \n" +
                "         @JoinColumn(name=\"join1\", referencedColumnName=\"ref1\", insertable=false, updatable=false),\n" +
                "         @JoinColumn(name=\"join2\", referencedColumnName=\"ref2\", insertable=false, updatable=false) } )";

        assertEquals(toBe, annotation);
    }

    private AttributeInContext stubAttributeInContext(ModelInContext modelInContext, EntityInContext entityInContext) {
        List<AttributeInContext> mappedFields = new ArrayList<>();
        AttributeInFakeModel attribute = new AttributeInFakeModel("attribute1", "huh");
        attribute.setDatabaseName("join1");

        AttributeInContext attributeInContext = new AttributeInContext(entityInContext, attribute, modelInContext, new EnvInContext());
        mappedFields.add(attributeInContext);
        return attributeInContext;
    }

    private LinkInContext stubLinkInContext(ModelInContext modelInContext, EntityInContext entityInContext) {
        GenericLink link = new GenericLink();
        link.setOwningSide(true);
        link.setTargetTableName("table1");
        link.setCardinality(Cardinality.MANY_TO_ONE);
        link.setCascadeOptions(new CascadeOptions());

        JoinTableInDbModel joinTable = new JoinTableInDbModel();

        JoinColumnInDbModel joinColumn1 = new JoinColumnInDbModel();
        joinColumn1.setName("join1");
        joinColumn1.setReferencedColumnName("ref1");

        JoinColumnInDbModel joinColumn2 = new JoinColumnInDbModel();
        joinColumn2.setName("join2");
        joinColumn2.setReferencedColumnName("ref2");

        joinTable.setJoinColumns(Arrays.asList(joinColumn1, joinColumn2));
        link.setJoinTable(joinTable);

        link.setJoinColumns(Arrays.<JoinColumn>asList(joinColumn1, joinColumn2));
        return new LinkInContext(entityInContext, link, modelInContext);
    }

    private Entity stubEntity() {
        EntityInFakeModel entity = new EntityInFakeModel();
        entity.setDatabaseTable("table1");
        entity.setClassName("WHATEVER");
        entity.storeAttribute(buildIdAttribute1());
        return entity ;
    }

    private AttributeInFakeModel buildIdAttribute1() {
        AttributeInFakeModel a = new AttributeInFakeModel("id", NeutralType.INTEGER);
        a.setDatabaseName("ID");
        a.setKeyElement(true);
        a.setNotNull(true);
        return a ;
    }
}
