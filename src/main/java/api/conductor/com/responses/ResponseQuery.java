package api.conductor.com.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseQuery extends Response{
	private ResponsePeer responsePeer;
	private Object responseQuery;	
}
