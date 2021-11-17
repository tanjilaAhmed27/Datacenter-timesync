/**
 * Created by roiya on 11/18/16.
 */
public final class Constants {
    public static final double duration = 1000000000000.0;//nanoseconds(12)
   // public static final int num_packet = 89;//0.1=1250, 1400 byte 0.1: 89
    public static final double load = 0.2;
    public static final long link_bandwidth = 10;//bp ns
    public static final int packet_size = 1400;//Bytes
    public static final int limit = (int) (8 * packet_size / (load * link_bandwidth));
    //public static final int limit = (int) ((load * link_bandwidth)/(8 * packet_size));
    public static final int num_TOR=80;
    public static final int re_sync = 1000000000;//(9)
    public static final long sim_rounds = 10;

}
