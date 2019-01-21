package com.oauth2.util;

/**
 * 
 * @author Jimmy Dave.
 *
 */
public class DataNotFoundException extends Exception {
	
	private final String field;
	private final String code;
	private final Object value;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	   
    public DataNotFoundException(String field, Object value, String code)
    {
       super();       
       this.code =code;    
       this.field = field;   
       this.value = value;
    }
    
    public DataNotFoundException(String field, Object value)
    {
       super();
       this.code = null;
       this.field = field;   
       this.value = value;
    }

	public String getField() {
		return field;
	}

	public String getCode() {
		return code;
	}

	public Object getValue() {
		return value;
	}  	
	
}
