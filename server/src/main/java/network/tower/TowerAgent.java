package network.tower;

import swim.api.SwimLane;
import swim.api.SwimResident;
import swim.api.agent.AbstractAgent;
import swim.api.downlink.EventDownlink;
import swim.api.lane.MapLane;
import swim.api.lane.ValueLane;
import swim.collections.HashTrieMap;
import swim.collections.HashTrieSet;
import swim.concurrent.TimerRef;
import swim.structure.Record;
import swim.structure.Value;
import swim.uri.Uri;


/* Web Agent for Network Towers */
public class TowerAgent extends AbstractAgent
{

	static final String NETWORK_HOST = "localhost:9001";
	static final Uri NETWORK_HOST_URI = Uri.parse(NETWORK_HOST);
	
	EventDownlink<Value> bandwidthLink;
	EventDownlink<Value> droppedPacketLink;
	EventDownlink<Value> connectedClientLink;
	EventDownlink<Value> infoLink;

	
	@SwimLane("tower/bandwidth")
	public ValueLane<Value> bandwidth;
	
	@SwimLane("tower/droppedPacket")
	public ValueLane<Value> droppedPacket;
	
	@SwimLane("tower/connectedClient")
	public ValueLane<Value> connectedClient;
	
	@SwimLane("tower/info")
	public ValueLane<Value> info = this.<Value>valueLane()
		.didSet((n,o) -> {
			System.out.println(nodeUri().toString() + n);
			command("/city/EastPaloAlto", "addTower", n.updatedSlot("id", getProp("id")));
		});
	
	@Override
	public void didStart() 
	{
		System.out.println(nodeUri() + " didStart");
		linkBandwidth();
		linkDroppedPacket();
		linkConnectedClient();
		linkInfo();
	}
	
	@Override
	public void willStop() 
	{
		System.out.println(nodeUri() + " willStop");
		unlinkBandwidth();
		unlinkDroppedPacket();
		unlinkConnectedClient();
		unlinkInfo();
	}

	void didSetRemoteBandwidth(Value newValue) 
	{
		bandwidth.set(newValue);
	}
	  
	public void linkBandwidth() 
	{
		if (bandwidthLink == null) 
		{
			bandwidthLink = downlink().hostUri(NETWORK_HOST_URI)
			.nodeUri(Uri.from(nodeUri().path())).laneUri("bandwidth")
			.onEvent(this::didSetRemoteBandwidth).keepSynced(true).open();
			
//			System.out.println(bandwidthLink);
//			System.out.println(bandwidthLink.hostUri());
//			System.out.println(bandwidthLink.nodeUri());
//			System.out.println(bandwidthLink.laneUri());
//			System.out.println("BANDWIDTH LINKED");
		}
	}

	public void unlinkBandwidth() 
	{
		if (bandwidthLink != null) 
		{
			bandwidthLink.close();
			bandwidthLink = null;
			
//			System.out.println("BANDWIDTH UNLINKED");
		}
	}
	
	void didSetRemoteDroppedPacket(Value newValue) 
	{
		droppedPacket.set(newValue);
	}
	  
	public void linkDroppedPacket() 
	{
		if (droppedPacketLink == null) 
		{
			droppedPacketLink = downlink().hostUri(NETWORK_HOST_URI)
			.nodeUri(Uri.from(nodeUri().path())).laneUri("droppedPacket")
			.onEvent(this::didSetRemoteBandwidth).keepSynced(true).open();

//			System.out.println("DROPPED PACKET LINKED");
		}
	}

	public void unlinkDroppedPacket() 
	{
		if (droppedPacketLink != null) 
		{
			droppedPacketLink.close();
			droppedPacketLink = null;
			
//			System.out.println("DROPPED PACKET UNLINKED");
		}
	}
	
	void didSetRemoteConnectedClient(Value newValue) 
	{
		connectedClient.set(newValue);
	}
	  
	public void linkConnectedClient() 
	{
		if (connectedClientLink == null) 
		{
			connectedClientLink = downlink().hostUri(NETWORK_HOST_URI)
			.nodeUri(Uri.from(nodeUri().path())).laneUri("connectedClient")
			.onEvent(this::didSetRemoteBandwidth).keepSynced(true).open();

//			System.out.println("CONNECTED CLIENT LINKED");
		}
	}

	public void unlinkConnectedClient() 
	{
		if (connectedClientLink != null) 
		{
			connectedClientLink.close();
			connectedClientLink = null;
			
//			System.out.println("CONNECTED CLIENT UNLINKED");
		}
	}
	
	void didSetRemoteInfo(Value newValue)
	{
		info.set(newValue);
	}
	
	public void linkInfo()
	{
		  if (infoLink == null)
		  {
		      infoLink = downlink().hostUri(NETWORK_HOST_URI)
		          .nodeUri(Uri.from(nodeUri().path())).laneUri("info")
		          .onEvent(this::didSetRemoteInfo).keepSynced(true).open();
		      
//		      System.out.println("INFO LINKED");
		  }
	}
	
	public void unlinkInfo()
	{
		if (infoLink != null) 
		{
		      infoLink.close();
		      infoLink = null;
		      
//		      System.out.println("INFO UNLINKED");
		}
	}
}
