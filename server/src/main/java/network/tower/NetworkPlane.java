package network.tower;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import swim.api.SwimAgent;
import swim.api.SwimRoute;
import swim.api.agent.AgentRoute;
import swim.api.plane.AbstractPlane;
import swim.fabric.Fabric;
import swim.kernel.Kernel;
import swim.server.ServerLoader;
import swim.structure.Record;
import swim.structure.Value;

public class NetworkPlane extends AbstractPlane
{
	private static List<Double> lngs = new ArrayList<>();
	private static List<Double> lats = new ArrayList<>();
	
	@SwimAgent("city")
	@SwimRoute("/city/:id")
	AgentRoute<CityAgent> cityAgent;
	
	@SwimAgent("tower")
	@SwimRoute("/tower/:id")
	AgentRoute<TowerAgent> towerAgent;

	public static void main(String[] args) throws InterruptedException, IOException
	{
		final Kernel kernel = ServerLoader.loadServer();
		final Fabric fabric = (Fabric) kernel.getSpace("network");
		
		kernel.start();
		System.out.println("Running Network server...");
		kernel.run();
//		fabric.command("/tower/1", "wake", Value.absent());
//		fabric.command("/tower/2", "wake", Value.absent());	
		
		/*
		 * Reads file, obtains coordinates, and writes them into a String ArrayList
		 * One for Latitudes, and one for longitudes
		 */
		BufferedReader br = new BufferedReader(new FileReader("SiteCoords"));
		try 
		{
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();
		    List<String> lngString = new ArrayList<>();
		    List<String> latString = new ArrayList<>();

		    for (int i = 0; i < 18; i++) 
		    {
		        sb.append(line);
		        sb.append(System.lineSeparator());
		        if(i % 2 == 0) 
		        {
		        	lngString.add(line);
		        }
		        else 
		        {
		        	latString.add(line);
		        }
		        line = br.readLine();
		    }
		    
//		    String everything = sb.toString();
//		    System.out.println("Latitudes: " + latitudeString);
//		    System.out.println("Longitudes: " + longitudeString);


		    /*
		     * Converts String Arraylists to Double ArrayLists
		     */
		    for (int i = 0; i < lngString.size(); i++)
		    { 
		        lngs.add(Double.parseDouble(lngString.get(i))); 
		    }
		    		    
		    for (int i = 0; i < latString.size(); i++)
		    { 
		    	lats.add(Double.parseDouble(latString.get(i))); 
		    }
		    System.out.println("Longitudes: " + lngs);
		    System.out.println("Latitudes: " + lats);

		    /*
		     * Pushes Latitudes, and Longitudes through command lanes
		     */
		    for(int i = 0; i < lngs.size(); i++)
			{
				final Record coord = Record.create(2)
						.slot("lng", lngs.get(i))
						.slot("lat", lats.get(i));
				fabric.command("/tower/" + i, "tower/info", coord);
//				fabric.command("/tower/EastPaloAlto/" + i, "info", coord);
//				System.out.println(coord);
			}
		}
		
		finally 
		{
		    br.close();
		}
		
//		final RandomData rd = new RandomData(fabric, "warp://localhost:9001");
//	    rd.sendCommands();
		
//		Thread.sleep(3000);
//		kernel.stop();
//		System.out.println("Server closed.");
	}
}