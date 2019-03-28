package api.conductor.com.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TransferDTO {
	private String accountPayer;
	private String accountReceiver;
	private String amount;
}
