package network.tower;

import swim.api.ref.SwimRef;
import swim.structure.Record;

/**
 * Simple wrapper around some {@code SwimRef}, e.g. a {@code SwimClient} handle,
 * that pushes data to the Swim server running at {@code hostUri}.
 */
class RandomData
{

  private final SwimRef ref;
  private final String hostUri;

  RandomData(SwimRef ref, String hostUri) 
  {
    this.ref = ref;
    this.hostUri = hostUri;
  }

  void sendCommands() throws InterruptedException 
  {
    int indicator = 0;
    while (true) {
      int bandwidth = (int) (Math.random() * 10 - 5) + 30;
      int droppedPackets = (int) (Math.random() * 20 - 10) + 60;
      int connectedClients = (int) (Math.random() * 30 - 15) + 90;
      if ((indicator / 25) % 2 == 0) {
        bandwidth *= 2;
        droppedPackets *= 2;
        connectedClients *= 2;
      }
      // msg's Recon serialization will take the following form:
      //   "{foo:$foo,bar:$bar,baz:$baz}"
      final Record msg = Record.create(3)
          .slot("bandwidth", bandwidth)
          .slot("droppedPackets", droppedPackets)
          .slot("connectedClients", connectedClients);

      // Push msg to the
      //   *CommandLane* addressable by "publish" OF the
      //   *Web Agent* addressable by "/unit/master" RUNNING ON the
      //   *(Swim) server* addressable by hostUri
      this.ref.command(this.hostUri, "tower/info", "publish", msg);
      indicator = (indicator + 1) % 1000;

      // Throttle events to four every three seconds
      Thread.sleep(750);
    }
  }
}