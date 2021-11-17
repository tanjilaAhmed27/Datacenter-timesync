/**
 * Created by roiya on 10/30/16.
 */

import jsc.distributions.Uniform;
import jsc.distributions.Lognormal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class timesync {

    public static double global_time = 0;
    public static List<Integer> counterList = new ArrayList<Integer>();//contain time counts for all TORs
    public static List<Integer> candidates = new ArrayList<Integer>();//contains minimum counter value TORs
    public static long num_suc_sync=0;
    public static long sync_delay_total = 0;
    public static long two_delay =0;
    public static long arr_time_check =0;
    public static long packet_counter =0;
    /*public static PoissonDistribution poisson = new PoissonDistribution(1000);*/


    public static void main(String args[]) {

        /*LogNormalDistribution lognormal = new LogNormalDistribution(20,15);
        long logn = (long) lognormal.sample();
        System.out.println("logarithmic values: "+ logn);
        return;*/
        initCounter();// initialize the counter with random numbers
        System.out.println("Done initialization " + Constants.limit );
        //printPacketList();
        long total_delay = 0;
        long total_sync_num = 0;
        for(long i=0; i< Constants.sim_rounds; i++) {
            initCounter();
            global_time = 0;
            sync_delay_total = 0;
            num_suc_sync = 0;
            runSimulation();
            total_delay = total_delay + sync_delay_total;// Total Delay Calculation per simulation
            total_sync_num = total_sync_num + num_suc_sync;//Total number of sync calculation
            System.out.println("Delay per sync"+(double) total_delay/total_sync_num);
        }
        double avg_delay_per_suc_sync = (double) total_delay/total_sync_num;
        double avg_delay_per_round = (double) total_delay/Constants.sim_rounds;
        System.out.println(packet_counter + " "+ arr_time_check +" avg delay per sync: " +  avg_delay_per_suc_sync + " total " + avg_delay_per_round + " num of TOR synced " + total_sync_num);
        System.out.println("Done initialization " + Constants.limit );
    }


    public static void runSimulation(){
        while (global_time<Constants.duration) {/*running the simulation for some specific duration*/
            int min = (int) findCandidates();
            //System.out.println(candidates + " min=" + min);
            //Adjust counter times
            global_time = global_time + min;
            for (int i = 0; i < counterList.size(); i++) {
                if (min == counterList.get(i)) {
                    counterList.set(i, Constants.re_sync);/*making the minimum candidate value=resync value*/
                } else {
                    counterList.set(i, counterList.get(i) - min);/*reduce other counter value by min*/
                }
            }
            //Sync all the candidates
            for(int i = 0; i< candidates.size(); i++){
                //System.out.println("---Syncing ToR # : " + candidates.get(i));
                syncTor(candidates.get(i));
                candidates.remove(i);
             //   global_time = global_time + two_delay;
            }
        }
    }
    public static long syncTor(int candidate_TOR){
        Packet syncMsg = askforNextPacket(candidate_TOR);//a
        Packet delayReq = askforNextPacket(candidate_TOR);//b
        num_suc_sync++;
        two_delay = (delayReq.getArrTime() + syncMsg.getArrTime());
        sync_delay_total = sync_delay_total + two_delay;//getArrTime gives the time after which the packet will arrive
        //   System.out.println("Total: " + sync_delay_total + " 1st: " + syncMsg.getArrTime() + " 2nd: " + delayReq.getArrTime() );
         /*   return 1;
        }
        else {
            num_failed_sync++;
            return -1;
        }*/
        return 1;
    }
    public static Packet askforNextPacket(int tor) {
            /*Random random = new Random();
            int arriv_time = random.nextInt(Constants.limit);//decided by the workload*/
            Uniform uniform = new Uniform (0, 14);
            int src = (int)uniform.random();
            int dest = (int)uniform.random();
          /* double ln= Math.log(Constants.limit)-0.5;
            LogNormalDistribution lognormal = new LogNormalDistribution(ln,1);
            int logn = (int) lognormal.sample();*/
            //ParetoDistribution pareto = new ParetoDistribution(0.5*Constants.limit,2);
            //int paret = (int) pareto.sample();
            //int arriv_time = paret % Constants.limit;*/
            /*public static PoissonDistribution poisson = new PoissonDistribution(1000);
            int pois = poisson.sample();*/
           int arriv_time = logn % Constants.limit;
            //arr_time_check += arriv_time;
            packet_counter +=1;
            //System.out.println("Arrival time " + arriv_time);

            return new Packet(arriv_time, 1, tor);
    }


    public static long findCandidates(){
        //find smallest value in counterList
        long min = Collections.min(counterList);
      //System.out.println("Minimum Element of the counterList is : " + min);
        getIndexOfMin(min);
        return min;
        //System.out.println(candidates);
    }
    public static void getIndexOfMin(long min){
        for (int i = 0; i < counterList.size(); i++) {
            if(counterList.get(i) == min ){
                candidates.add(i);
            }
        }
    }

    /*initializes counterList(with element #num_TOR) with random numbers*/
    public static void initCounter(){
        for(long i=0; i < Constants.num_TOR;i++){
            Random rand;
            rand = new Random();
            //LogNormalDistribution lognormal = new LogNormalDistribution(20,15);
            //long logn = (long) lognormal.sample();
            counterList.add(rand.nextInt(Constants.re_sync));
            //if(rand > Constants.re_sync)
            //{rand = Constants.re_sync;}
           // counterList.add(rand);
            //System.out.println("logarithmic values: "+ counterList);
        }
        //System.out.println("logarithmic values: "+ counterList);
    }

 /*   public static void createinTORPacketList() {
        for (int k = 0; k < Constants.num_TOR; k++) {
            for (int i = 0; i < Constants.duration; i++) {
                for (int j = 0; j < Constants.num_packet; j++) {
                    //Random random = new Random();
                    //int time = random.nextInt(Constants.duration);
                    LogNormalDistribution lognormal = new LogNormalDistribution();
                    int time = (int) lognormal.sample();
                    Packet PK = new Packet(time, 1,k);
                    insertPacketSortedInList(PK);
                    //System.out.println("Packet : " + PK.getArrTime()+ "  TOR : " +PK.getTOR());
                }
            }
        }
    }
    public static void insertPacketSortedInList(Packet PK){
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
    public static void createoutTORPacketList() {
        for (int k = 0; k < Constants.num_TOR; k++) {
        for (int i = 0; i < Constants.duration; i++) {
            for (int j = 0; j < Constants.num_packet; j++) {
                LogNormalDistribution lognormal = new LogNormalDistribution();
                int time = (int) lognormal.sample();
                Packet PK = new Packet(time, 2,k);
                insertPacketSortedOutList(PK);
                //System.out.println("Packet : " + PK.getArrTime()+ "  TOR : " +PK.getTOR());
            }
        }
    }
    }
    public static void insertPacketSortedOutList(Packet PK){
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
    public static void printPacketList(){
        int pos=0;
        for(pos=0; pos< inTORpacketList.size(); pos++){
            System.out.println(inTORpacketList.get(pos).myPrint());
        }
        for(pos=0; pos< outTORpacketList.size(); pos++){
            System.out.println(outTORpacketList.get(pos).myPrint());
        }
    }*/
    /*   while (counter>0){
            counter= counter-1;
        }
        if (counter=0){
            for (int k=1;k<180;k++){
                controller_side();
                TOR_side();
            }
            counter=100;
        }
    }
    public static void controller_side(){

        while(framer_dat(k)=1 && deframer_dat(k)=1){
            if (framer_pad = delay_req)
                dframer_pad = delay_resp;
            sync(k)= 1;
            else dframer_pad = sync_msg;
        }
    }


    public static void TOR_side(){
        while(framer_dat(k)=1 && deframer_dat(k)=1){
            if (dframer_pad = sync_msg)
                framer_pad = delay_req;



        }

    }*/
}

/*  public static Packet findOutPacketList(int tor_num){
        for(int pos=global_pos_out; pos <outTORpacketList.size();pos++){
            Packet pk = outTORpacketList.get(pos);
            if(pk.getArrTime()>=global_time && pk.getTOR()== tor_num){
                return pk;
            }
        }
        return new Packet(-1,0,0);
    }

    public static Packet findInPacketList(int tor_num){
        for(int pos=global_pos_in; pos <inTORpacketList.size();pos++){
            Packet pk = inTORpacketList.get(pos);
            if(pk.getArrTime()>=global_time && pk.getTOR()== tor_num){
                return pk;
            }
        }
        return new Packet(-1,0,0);
    }*/

  /*  public static void syncCandidates(){
        for(int i =global_pos_in; i< inTORpacketList.size();i++){
            if(inTORpacketList.get(i).getArrTime()>=global_time) {
                global_pos_in = i;
                break;
            }
        }
        for(int i =global_pos_out; i< outTORpacketList.size();i++){
            if(outTORpacketList.get(i).getArrTime()>=global_time) {
                global_pos_out = i;
                break;
            }
        }


        for(int i=0; i < candidates.size();i++){
            syncTor(candidates.get(i));
        }

    }*/
 /*   private int[] framer_dat = new int[]{101110} ;
    private int[] dframer_dat = new int[]{101110} ;
    private int[] framer_pad = new int[]{101110} ;
    private int[] dframer_pad = new int[]{101110} ;
    private int[] delay_req = new int[]{100001} ;
    private int[] delay_resp = new int[]{111000} ;
    private int[] sync_msg = new int[]{111000} ;
    private boolean sync;*/
