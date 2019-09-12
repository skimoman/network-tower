package network.tower;

import java.util.Iterator;
import swim.api.SwimLane;
import swim.api.SwimResident;
import swim.api.agent.AbstractAgent;
import swim.api.downlink.MapDownlink;
import swim.api.lane.CommandLane;
import swim.api.lane.JoinValueLane;
import swim.api.lane.ListLane;
import swim.api.lane.MapLane;
import swim.api.lane.ValueLane;
import swim.recon.Recon;
import swim.structure.Record;
import swim.structure.Value;
import swim.uri.Uri;

public class CityAgent extends AbstractAgent 
{
	
	static final String NETWORK_HOST = "localhost:9001";
	static final Uri NETWORK_HOST_URI = Uri.parse(NETWORK_HOST);
	static final Uri TOWER_INFO = Uri.parse("tower/info");

	@SwimLane("tower/info")
	MapLane<String, Value> info = this.<String, Value>mapLane()
		.didUpdate((key, newValue, oldValue) -> 
		{
			logMessage(key + " changed to " + newValue + " from " + oldValue);
		})
		.didRemove((key, oldValue) -> 
		{
			logMessage("removed <" + key  + "," + oldValue + ">");
		});
	
	@SwimLane("addTower")
	CommandLane<Value> publish = this.<Value>commandLane()
		.onCommand(msg -> 
		{
			System.out.println(msg);
			this.info.put(msg.get("id").stringValue(), msg);
		});
	
	public void didStart()
	{
		System.out.println(nodeUri() + " didStart");
	}

	public void willStop()
	{
		System.out.println(nodeUri() + " willStop");

	}
	
	private void logMessage(Object msg) 
	{
		System.out.println(nodeUri() + ": " + msg);
	}	
	
}
