package api.conductor.com.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponsePeer extends Response  {
	private String peer;
	private String status;
	private String message;
	private String id;
	
	public ResponsePeer(String status, String message) {
		this.status = status;
		this.message = message;
	}
}