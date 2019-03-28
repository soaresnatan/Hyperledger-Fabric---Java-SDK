package api.conductor.com.chaincodes.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class ChaincodeFeatures {
	private String chaincodeName;
	private String method;
	private String[] args;
	
	public ChaincodeFeatures(String chaincString) {
		this.chaincodeName = chaincString;
	}
	
	public String[] getArgs() {
		return args;
	}
	
	public void setArgs(String[] arguments) {
		List<String> listArguments = new ArrayList<>(Arrays.asList(arguments));
		
		int listSize = listArguments.size();
		for(int index = 0; index < listSize; index++) {			
			if(listArguments.get(index) == null) {				
				listArguments.remove(index);
				index--;
				listSize--;				
			}
		}
			
		args = listArguments.toArray(new String[0]);
	}
	
	public String getMethod() {
		return method;
	}
	
	public void setMethod(String method) {
		this.method = method;
	}
}
