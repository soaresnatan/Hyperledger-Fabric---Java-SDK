package api.conductor.com.responses;

import java.nio.charset.StandardCharsets;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
public class ResponseSdk extends Response{
	private String status;
	private String messsage;
	private String eventPayload;
	private String ProposalResponse;
}