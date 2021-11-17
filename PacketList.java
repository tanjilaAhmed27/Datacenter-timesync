import java.util.ArrayList;
import java.util.List;

/**
 * Created by roiya on 11/28/16.
 */
public class PacketList {
    public List<Packet> inTORpacketList = new ArrayList<>();
    public List<Packet> outTORpacketList = new ArrayList<>();

    public PacketList(){

    }

    public void insertPacketSortedInList(Packet PK){

        if(inTORpacketList.isEmpty()) {
            inTORpacketList.add(PK);
            return;
        }
        Packet oldPK;
        int pos=0;
        for(pos=0; pos< inTORpacketList.size(); pos++){
            oldPK = inTORpacketList.get(pos);
            if(oldPK.getArrTime() >= PK.getArrTime()) {
                break;
            }
        }
        if(pos >= inTORpacketList.size()){
            inTORpacketList.add(PK);

        } else {
            inTORpacketList.add(pos, PK);
        }
    }

    public void insertPacketSortedOutList(Packet PK){
        if(outTORpacketList.isEmpty()) {
            outTORpacketList.add(PK);
            return;
        }
        Packet oldPK;
        int pos=0;
        for(pos=0; pos< outTORpacketList.size(); pos++){
            oldPK = outTORpacketList.get(pos);
            if(oldPK.getArrTime() >= PK.getArrTime()) {
                break;
            }
        }
        if(pos >= outTORpacketList.size()){
            outTORpacketList.add(PK);

        } else {
            outTORpacketList.add(pos, PK);
        }
    }
    public void printPacketList(){
        int pos=0;
        for(pos=0; pos< inTORpacketList.size(); pos++){
            System.out.println(inTORpacketList.get(pos).myPrint());
        }
        for(pos=0; pos< outTORpacketList.size(); pos++){
            System.out.println(outTORpacketList.get(pos).myPrint());
        }
    }
}
