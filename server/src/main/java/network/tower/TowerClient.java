//package network.tower;
//
//import swim.api.downlink.ValueDownlink;
//import swim.client.ClientRuntime;
//import swim.structure.Text;
//import swim.structure.Value;
//
//class TowerClient
//{
//
//	public static void main(String[] args) throws InterruptedException
//	{
//		ClientRuntime swimClient = new ClientRuntime();
//		swimClient.start();
//		final String hostUri = "warp://localhost:9001";
//		final String nodeUri = "/tower/1";
//		
//		swimClient.command(hostUri, nodeUri, "WAKEUP", Value.absent());
//		final ValueDownlink<Value> link = swimClient.downlinkValue()
//				.hostUri(hostUri).nodeUri(nodeUri).laneUri("bandwidth")
//				.didSet((newValue, oldValue) -> 
//		{
//			System.out.println("link watched bandwidth change to "
//					+ newValue + " from " + oldValue);
//		})
//		.open();
//		
//	    swimClient.command(hostUri, nodeUri, "droppedPacket", Text.from("Hello from command, world!"));
//		link.set(Text.from("Link test"));
//		//swimClient.stop();
//	}
//}
