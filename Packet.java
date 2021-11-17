/**
 * Created by roiya on 11/18/16.
 */
public class Packet {
    private int arrTime;// How long are we waiting to see the next packet?
    private int type;
    private int TOR;

    public int getArrTime() {
        return arrTime;
    }
    public int getTOR(){
        return TOR;
    }

    public void setArrTime(int arrTime) {
        this.arrTime = arrTime;
    }

    public Packet(int time, int typ, int tor){
        arrTime = time;
        type = typ;
        TOR = tor;
    }

    public String myPrint(){
        return "Time " + arrTime + " type " + type + "  TOR " + TOR;
    }
}
