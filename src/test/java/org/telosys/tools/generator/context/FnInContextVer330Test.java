package org.telosys.tools.generator.context;

import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class FnInContextVer330Test {
	
	@Test
	public void testToList() {
		FnInContext fn = new FnInContext(null, null);
		String[] array = { "A", "B", "C" };
		List<String> list = fn.toList(array);
		
		Assert.assertEquals(3, list.size() );
		Assert.assertEquals("A", list.get(0) );
	}

	@Test
	public void testJoin() {
		FnInContext fn = new FnInContext(null, null);
		String[] array = { "A", "B", "C" };
		List<String> list = Arrays.asList(array);

		Assert.assertEquals("ABC", fn.join(list, "") );
		Assert.assertEquals("ABC", fn.join(list, null) );
		Assert.assertEquals("A,B,C", fn.join(list, ",") );
		Assert.assertEquals("A, B, C", fn.join(list, ", ") );
		Assert.assertEquals("", fn.join(null, "") );
		Assert.assertEquals("", fn.join(null, null) );
	}

	@Test
	public void testJoinWithPrefixSuffix() {
		FnInContext fn = new FnInContext(null, null);
		String[] array = { "A", "B", "C" };
		List<String> list = Arrays.asList(array);

		Assert.assertEquals("ABC",   fn.joinWithPrefixSuffix(list, "",  "", "") );
		Assert.assertEquals("ABC",   fn.joinWithPrefixSuffix(list, null, null, null) );
		Assert.assertEquals("A,B,C", fn.joinWithPrefixSuffix(list, ",", "", "") );

		Assert.assertEquals("[A],[B],[C]", fn.joinWithPrefixSuffix(list, ",", "[", "]") );
		Assert.assertEquals("[A][B][C]",   fn.joinWithPrefixSuffix(list, "",  "[", "]") );
		Assert.assertEquals("A.class, B.class, C.class",   fn.joinWithPrefixSuffix(list, ", ", "", ".class") );
	
		Integer[] array2 = { 1, 2, 3 };
		List<Integer> list2 = Arrays.asList(array2);
		Assert.assertEquals("123",   fn.joinWithPrefixSuffix(list2, "",  "", "") );
	}

	@Test
	public void testJoinWithPrefixSuffixIntegerList() {
		FnInContext fn = new FnInContext(null, null);
		Integer[] array = { 1, 2, 3 };
		List<Integer> list = Arrays.asList(array);
		Assert.assertEquals("123",   fn.joinWithPrefixSuffix(list, "",  "", "") );
		Assert.assertEquals("[1],[2],[3]", fn.joinWithPrefixSuffix(list, ",", "[", "]") );
	}

}
