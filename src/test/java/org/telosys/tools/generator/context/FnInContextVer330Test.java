package org.telosys.tools.generator.context;

import java.util.ArrayList;
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

	@Test
	public void testReplaceInListString() {
		FnInContext fn = new FnInContext(null, null);
		List<String> list = new ArrayList<>();
		list.add("a");
		list.add("b");
		list.add("c");
		list.add("b");
		
		fn.replaceInList(list, "b", "BB");
		int i = 0 ;
		Assert.assertEquals("a",  list.get(i++) );
		Assert.assertEquals("BB",  list.get(i++) );
		Assert.assertEquals("c",  list.get(i++) );
		Assert.assertEquals("BB",  list.get(i++) );
	}

	@Test
	public void testReplaceInListInteger() {
		FnInContext fn = new FnInContext(null, null);
		List<Integer> list = new ArrayList<>();
		list.add(Integer.valueOf(1));
		list.add(Integer.valueOf(99));
		list.add(Integer.valueOf(333));
		list.add(Integer.valueOf(99));
		list.add(Integer.valueOf(888));
		
		fn.replaceInList(list, Integer.valueOf(99), Integer.valueOf(77));
		int i = 0 ;
		Assert.assertEquals(Integer.valueOf(1),  list.get(i++) );
		Assert.assertEquals(Integer.valueOf(77),  list.get(i++) );
		Assert.assertEquals(Integer.valueOf(333),  list.get(i++) );
		Assert.assertEquals(Integer.valueOf(77),  list.get(i++) );
	}

	@Test
	public void testReplaceInListAny() {
		FnInContext fn = new FnInContext(null, null);
		List<Object> list = new ArrayList<>();
		list.add(Integer.valueOf(1));
		list.add("aa");
		list.add(Boolean.valueOf(true));
		list.add("bbb");
		list.add(Integer.valueOf(888));
		
		fn.replaceInList(list, "aa", Integer.valueOf(77));
		int i = 0 ;
		Assert.assertEquals(Integer.valueOf(1),  list.get(i++) );
		Assert.assertEquals(Integer.valueOf(77),  list.get(i++) );
		Assert.assertEquals(Boolean.valueOf(true),  list.get(i++) );
		Assert.assertEquals("bbb",  list.get(i++) );
	}

	@Test
	public void testTrimAllString() {
		FnInContext fn = new FnInContext(null, null);
		List<Object> list = new ArrayList<>();
		list.add("a");
		list.add("b  ");
		list.add("  c c ");
		list.add("  d");
		
		fn.trimAll(list);
		int i = 0 ;
		Assert.assertEquals("a",  list.get(i++) );
		Assert.assertEquals("b",  list.get(i++) );
		Assert.assertEquals("c c",  list.get(i++) );
		Assert.assertEquals("d",  list.get(i++) );
	}

	@Test
	public void testTrimAllAny() {
		FnInContext fn = new FnInContext(null, null);
		List<Object> list = new ArrayList<>();
		list.add(Integer.valueOf(1));
		list.add(" a");
		list.add(Boolean.valueOf(true));
		list.add(" b b ");
		list.add(Integer.valueOf(888));
		
		fn.trimAll(list);
		int i = 0 ;
		Assert.assertEquals(1,  list.get(i++) );
		Assert.assertEquals("a",  list.get(i++) );
		Assert.assertEquals(Boolean.valueOf(true),  list.get(i++) );
		Assert.assertEquals("b b",  list.get(i++) );
	}

}
