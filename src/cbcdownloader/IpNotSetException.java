package cbcdownloader;

public class IpNotSetException extends CommunicationException {
	private static final long serialVersionUID = 5152351952302468131L;

	public IpNotSetException() {
	}

	public IpNotSetException(String message) {
		super(message);
	}
}
