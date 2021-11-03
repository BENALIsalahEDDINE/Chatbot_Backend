package ecomerce.chatbot.model;

public class TextMessage {
	private String firstname ;
	private String text ;
	
	
	
	public TextMessage(String firstname, String text) {
		super();
		this.firstname = firstname;
		this.text = text;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	
}
