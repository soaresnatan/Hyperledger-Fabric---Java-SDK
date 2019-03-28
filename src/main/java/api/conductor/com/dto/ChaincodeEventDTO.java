package api.conductor.com.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChaincodeEventDTO {
	private String peer;
	private String event;
	private String txId;
	private String response;
}
