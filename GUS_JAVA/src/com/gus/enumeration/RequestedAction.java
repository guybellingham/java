package com.gus.enumeration;

/**
 * An enumeration of <code>action=</code> request parameter values. 
 * Use these instead of 'magic' strings. You can use enums in the <code>switch</code> 
 * case statement now as follows: 
 * <pre>
 * RequestedAction requestedAction = getRequestedAction();
 * try {
 *     switch(requestedAction) {
 *         case ADD:
 *             return handleAdd(.....);
 *         case EDIT:
 *             doEdit(....);
 *             break;
 *         default: 
 *             throw new InvalidInputException("Invalid action '" + requestedAction.getValue() + "'!"); 
 *     }
 * } catch (ItemNotFoundException e) {
 *     //Throw a new Exception or handle the error here
 * }
 * </pre> 
 * <b>IMPORTANT!</b>  The value string inside this enumeriation MUST be the same sequence of characters as 
 * the <code>name</code> you give the enum if you want to use: 
 * <pre>
 * RequestedAction requestedAction = RequestedAction.valueOf(myParamString.toUpperCase().trim());
 * </pre> 
 * Otherwise use the <code>fromString(String param)</code> method {@link #fromString(String)}.
 * @author Guy
 *
 */
public enum RequestedAction {
	ADD("add"),
	ASSIGN("assign"),
	BACKWARD("backward"),
	CANCEL("cancel"),
	CHECK("check"),
	CROP("crop"),
	DEFAULT("default"),
	DELETE("default"),
	EDIT("edit"),
	EDITCALENDAR("editcalendar"),
	EMAIL("email"),
	EXECUTE("execute"),
	FORWARD("forward"),
	INITIALIZE("initialize"),
	PAGE("page"),
	PAGEBACKWARD("pagebackward"),
	PAGEFORWARD("pageforward"),
	SELECT("select"),
	UPLOAD("upload");
	
	/**
	 * String 'value' that the <code>action</code> query parameter can have. 
	 */
	private String value;

	RequestedAction(String value) {
		setValue(value);
	}
	public String getValue() {
		return value;
	}

	private void setValue(String value) {
		this.value = value;
	}
	/**
	 * Will look for the RequestedAction that has a <code>value</code> String 
	 * equal to the given <code>param</code> (ignoring case and whitespace).
	 * @param param
	 * @return
	 */
	public static RequestedAction fromString(String param) {
		param = param.trim();   	//remove leading and trailing whitespace
		RequestedAction[] enums = RequestedAction.values();
		for (int i = 0; i < enums.length; i++) {
			if(enums[i].getValue().equalsIgnoreCase(param)) {
				return enums[i];
			}
		}
		return DEFAULT;
	}
}
