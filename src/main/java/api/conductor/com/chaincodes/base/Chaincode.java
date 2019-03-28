package api.conductor.com.chaincodes.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Chaincode {
	private String name;
	private String version;
}
