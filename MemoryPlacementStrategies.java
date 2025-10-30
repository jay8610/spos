import java.util.Arrays;
import java.util.Scanner;

public class MemoryPlacementStrategies {

    enum Strategy { FIRST, BEST, WORST, NEXT }

    // Generic allocator: returns allocation[] where allocation[i] = index of block (or -1)
    static int[] allocate(int[] blocksOriginal, int[] processes, Strategy strategy) {
        int m = blocksOriginal.length;
        int n = processes.length;
        int[] blocks = blocksOriginal.clone(); // work on copy so caller's array is safe
        int[] allocation = new int[n];
        Arrays.fill(allocation, -1);

        int nextStart = 0; // used only by NEXT strategy

        for (int i = 0; i < n; i++) {
            int chosen = -1;

            if (strategy == Strategy.FIRST) {
                for (int j = 0; j < m; j++) {
                    if (blocks[j] >= processes[i]) { chosen = j; break; }
                }
            } else if (strategy == Strategy.BEST) {
                for (int j = 0; j < m; j++) {
                    if (blocks[j] >= processes[i] && (chosen == -1 || blocks[j] < blocks[chosen])) {
                        chosen = j;
                    }
                }
            } else if (strategy == Strategy.WORST) {
                for (int j = 0; j < m; j++) {
                    if (blocks[j] >= processes[i] && (chosen == -1 || blocks[j] > blocks[chosen])) {
                        chosen = j;
                    }
                }
            } else if (strategy == Strategy.NEXT) {
                int count = 0;
                int j = nextStart;
                while (count < m) {
                    if (blocks[j] >= processes[i]) { chosen = j; break; }
                    j = (j + 1) % m;
                    count++;
                }
                if (chosen != -1) nextStart = (chosen + 1) % m;
            }

            if (chosen != -1) {
                allocation[i] = chosen;
                blocks[chosen] -= processes[i];
            }
        }

        return allocation;
    }

    static void printAllocation(int[] processes, int[] allocation) {
        System.out.println("Process No.\tProcess Size\tBlock No.");
        for (int i = 0; i < processes.length; i++) {
            System.out.printf(" %d\t\t%d\t\t%s%n", i + 1, processes[i],
                              allocation[i] == -1 ? "Not Allocated" : String.valueOf(allocation[i] + 1));
        }
        System.out.println();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of memory blocks: ");
        int m = sc.nextInt();
        int[] blocks = new int[m];
        for (int i = 0; i < m; i++) {
            System.out.printf("Enter size of block %d: ", i + 1);
            blocks[i] = sc.nextInt();
        }

        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();
        int[] processes = new int[n];
        for (int i = 0; i < n; i++) {
            System.out.printf("Enter size of process %d: ", i + 1);
            processes[i] = sc.nextInt();
        }
        System.out.println();

        for (Strategy s : Strategy.values()) {
            int[] allocation = allocate(blocks, processes, s);
            System.out.println(s.name() + " Fit Allocation:");
            printAllocation(processes, allocation);
        }

        sc.close();
    }
}