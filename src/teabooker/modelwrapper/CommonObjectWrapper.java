package teabooker.modelwrapper;

//Common Result for insert,update
public class CommonObjectWrapper {
	private boolean status;
	public boolean isStatus() {
		return status;
	}
	public void setStatus(boolean status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	private String message;
}
