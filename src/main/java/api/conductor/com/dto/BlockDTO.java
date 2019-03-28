package api.conductor.com.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlockDTO {
	private String channel;
	private String peer;
	private long numberBlock;
	private int numberTransactions;	
}
