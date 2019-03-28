package api.conductor.com.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChaincodeDTO {
	private String name;
	private String version;
	private String endorsement;
	private String path;
	
}
