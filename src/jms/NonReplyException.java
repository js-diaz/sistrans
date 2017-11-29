package jms;

public class NonReplyException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public NonReplyException(String message){
		super(message);
	}

}