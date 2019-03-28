package api.conductor.com.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccountDTO {
	private String number;
	private String balance;
	private String owner;
}
