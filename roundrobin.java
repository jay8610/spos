import java.util.*;

class pro {
    int id, at, bt, ct, rt, wt, tat;
    boolean added = false;

    pro(int id, int at, int bt){
        this.id = id;
        this.at = at;
        this.bt = bt;
        this.rt = bt;
    }
}

public class roundrobin {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        List<pro> cpuList = new ArrayList<>();

        for(int i = 0; i < n; i++){
            System.out.print("Enter arrival time for process " + i + ": ");
            int at = sc.nextInt();
            System.out.print("Enter burst time for process " + i + ": ");
            int bt = sc.nextInt();
            cpuList.add(new pro(i, at, bt));
        }

        System.out.print("Enter Quantum Time: ");
        int qt = sc.nextInt(); // ✅ Custom quantum entered by user

        cpuList.sort(Comparator.comparingInt(a -> a.at));
        Queue<pro> cpuQueue = new LinkedList<>();

        int time = 0, complete = 0;
        float totaltat = 0, totalwt = 0;

        while(complete < n){

            for(pro p : cpuList){
                if(p.at <= time && p.rt > 0 && !p.added){
                    cpuQueue.add(p);
                    p.added = true;
                }
            }

            if(cpuQueue.isEmpty()){
                time++;
                continue;
            }

            pro cur = cpuQueue.poll();
            int run = Math.min(cur.rt, qt);

            // Execute CPU time unit by unit
            for(int i = 0; i < run; i++){
                time++;
                cur.rt--;

                for(pro p : cpuList){
                    if(p.at <= time && p.rt > 0 && !p.added){
                        cpuQueue.add(p);
                        p.added = true;
                    }
                }

                if(cur.rt == 0) break;
            }

            if(cur.rt == 0){
                cur.ct = time;
                cur.tat = cur.ct - cur.at;
                cur.wt = cur.tat - cur.bt;
                if(cur.wt < 0) cur.wt = 0;

                totaltat += cur.tat;
                totalwt += cur.wt;
                complete++;
            } else {
                cpuQueue.add(cur);
            }
        }

        System.out.println("\n\tPID\tAT\tBT\tCT\tWT\tTAT");
        for(pro p : cpuList){
            System.out.println("\t" + p.id + "\t" + p.at + "\t" + p.bt + "\t" +
                    p.ct + "\t" + p.wt + "\t" + p.tat);
        }

        System.out.println("Avg TAT : " + (totaltat / n));
        System.out.println("Avg WT  : " + (totalwt / n));
    }
}

