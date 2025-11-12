import java.util.*;

class pro{
    int id,at,bt,ct,rt,wt,tat;
    boolean added = false;

    pro(int id,int at, int bt){
        this.id = id;
        this.at = at;
        this.bt = bt;
        this.rt = bt;
    }
}

public class SJF{

    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);

        System.out.println("enter number of processes :");
        int n= sc.nextInt();

        List<pro>cpuList= new ArrayList<>();

        for(int i=0;i<n;i++){

            System.out.println("enter arrival time for process "+i+":");
            int at = sc.nextInt();
            System.out.println("enter burst time for process "+i+":");
            int bt = sc.nextInt();
            

            cpuList.add(new pro(i,at,bt));
        }

        cpuList.sort(Comparator.comparingInt(a->a.at));
        PriorityQueue<pro>cpuQueue = new PriorityQueue<>(Comparator.comparingInt(a->a.rt));
        
        int time =0;
        int complete = 0;
        float totaltat=0;
        float totalwt =0;

        while(complete<n){

            for(pro p:cpuList){
                if(p.at<=time && !p.added){
                    cpuQueue.add(p);
                      p.added = true;
                }
            }
            if(cpuQueue.isEmpty()){
                time++;
                continue;
            }

            pro cur = cpuQueue.poll();
            
            time++;
            cur.rt--;

            if(cur.rt==0){
            cur.ct = time;
            cur.tat = cur.ct - cur.at;
            cur.wt = cur.tat - cur.bt;
            if(cur.wt<0){
                cur.wt = 0;
            }
            totaltat+=cur.tat;
            totalwt+=cur.wt;
            complete++;}
            else{
                cpuQueue.add(cur);
                
            }
        }

        System.out.println("\tPID\tAT\tBT\tCT\tRT\tWT\tTAT");
        for(pro p:cpuList){
            System.out.println("\t"+p.id+"\t"+p.at+"\t"+p.bt+"\t"+p.ct+"\t"+p.rt+"\t"+p.wt+"\t"+p.tat);

        }
        System.out.println("Avg TAT :"+(totaltat/n));
        System.out.println("Avg WT :"+(totalwt/n));
    }
}
