package org.telosys.tools.test.velocity.context.doc;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class ClassForTypeTest {

	public void v() {
	}
	
	public String s() {
		return "" ;
	}
	
	public List<String> listString () {
		return null ;
	}
	
	public List<Number> listNumber () {
		return null ;
	}
	public List<?> listJoker () {
		return null ;
	}
	public List<? extends Object> listJoker2 () {
		return null ;
	}
	public List<? super Object> listJoker3 () {
		return null ;
	}
	public List<? super Number> listJoker4 () {
		return null ;
	}
	public List<? extends Serializable> listJoker5 () {
		return null ;
	}

	public Map<String,Number> mapStringNumber () {
		return null ;
	}
	public Map<String,?> mapStringJoker () {
		return null ;
	}
	public Map<String,? extends Object> mapStringJoker2 () {
		return null ;
	}
	public Map<String,? extends Number> mapStringJoker3 () {
		return null ;
	}
}
