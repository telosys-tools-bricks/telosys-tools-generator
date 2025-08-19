package org.telosys.tools.generator.context.tools;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PluralizerTest {

	@Test
	public void test1() {
		Pluralizer pluralizer = new Pluralizer(); 
		
		// Add "s" 
		assertEquals("cars", pluralizer.pluralize("car") );
		assertEquals("dogs", pluralizer.pluralize("dog") );
		
		// Add "es" 
		assertEquals("boxes", pluralizer.pluralize("box") );
		assertEquals("Boxes", pluralizer.pluralize("Box") );

		assertEquals("buses", pluralizer.pluralize("bus") );
		assertEquals("Buses", pluralizer.pluralize("Bus") );
		
		assertEquals("dishes", pluralizer.pluralize("dish") );
		assertEquals("Dishes", pluralizer.pluralize("Dish") );
		
		assertEquals("topazes", pluralizer.pluralize("topaz") );
		assertEquals("Topazes", pluralizer.pluralize("Topaz") );

		assertEquals("heroes", pluralizer.pluralize("hero") );
		assertEquals("Heroes", pluralizer.pluralize("Hero") );
		
		// Replace "y" by "ies"
		assertEquals("armies", pluralizer.pluralize("army") );
		assertEquals("Armies", pluralizer.pluralize("Army") );
		
		// invariants
		assertEquals("fish", pluralizer.pluralize("fish") );
		assertEquals("Fish", pluralizer.pluralize("Fish") );
		
		// irregulars
		assertEquals("children", pluralizer.pluralize("child") );
		assertEquals("Children", pluralizer.pluralize("Child") );
		assertEquals("women", pluralizer.pluralize("woman") );
		assertEquals("Women", pluralizer.pluralize("Woman") );
		assertEquals("mice", pluralizer.pluralize("mouse") ); // plural word shorter than original
		assertEquals("Mice", pluralizer.pluralize("Mouse") );
		
		// Test with multi uppercase and special char
		assertEquals("bigboxes", pluralizer.pluralize("bigbox") );
		assertEquals("BigBoxes", pluralizer.pluralize("BigBox") );
		assertEquals("big_boxes", pluralizer.pluralize("big_box") );
		assertEquals("BigArmies", pluralizer.pluralize("BigArmy") );
		assertEquals("Big-armies", pluralizer.pluralize("Big-army") );
		
		// pluralize("BigChild") cannot work because it's irregural and unknown => return "BigChilds"
		
	}	

}
