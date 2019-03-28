package api.conductor.com.responses;

import java.util.ArrayList;
import java.util.List;

import api.conductor.com.dto.ChannelDTO;
import lombok.Data;

@Data
public class ResponseChannel extends Response {
	private String peer;
	private List<ChannelDTO> channel;
	
	public ResponseChannel(String peer) {
		this.peer = peer;
		channel = new ArrayList<>();		
	}
	
	public void addChannel(String name) {
		channel.add(new ChannelDTO(name));
	}
	
}
