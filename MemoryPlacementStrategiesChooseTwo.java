import java.util.Arrays;
import java.util.Scanner;

public class MemoryPlacementStrategiesChooseTwo {

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

    // Helper to map 1-based menu int -> Strategy
    static Strategy strategyFromChoice(int choice) {
        if (choice < 1 || choice > Strategy.values().length) return null;
        return Strategy.values()[choice - 1];
    }

    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
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
            sc.nextLine(); // consume leftover newline
            System.out.println();

            // Display menu and ask for two choices
            System.out.println("Choose any TWO strategies to perform (enter two numbers separated by space):");
            for (int i = 0; i < Strategy.values().length; i++) {
                System.out.printf("  %d. %s Fit%n", i + 1, Strategy.values()[i].name());
            }

            int choiceA = -1, choiceB = -1;
            while (true) {
                System.out.print("Enter two distinct choices (e.g. \"1 3\"): ");
                String line = sc.nextLine().trim();
                if (line.isEmpty()) continue;
                String[] parts = line.split("[,\\s]+");
                if (parts.length != 2) {
                    System.out.println("Please enter exactly two choices.");
                    continue;
                }
                try {
                    choiceA = Integer.parseInt(parts[0]);
                    choiceB = Integer.parseInt(parts[1]);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid numbers. Try again.");
                    continue;
                }
                if (choiceA == choiceB) {
                    System.out.println("Choices must be distinct. Try again.");
                    continue;
                }
                if (strategyFromChoice(choiceA) == null || strategyFromChoice(choiceB) == null) {
                    System.out.println("Choices must be between 1 and " + Strategy.values().length + ". Try again.");
                    continue;
                }
                break;
            }

            // Run only the two selected strategies
            Strategy[] toRun = { strategyFromChoice(choiceA), strategyFromChoice(choiceB) };
            for (Strategy s : toRun) {
                int[] allocation = allocate(blocks, processes, s);
                System.out.println(s.name() + " Fit Allocation:");
                printAllocation(processes, allocation);
            }
        }
    }
}

