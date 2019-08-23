import java.io.*;
import java.util.StringTokenizer;



public class JobQueue {
    private int numWorkers;
    private int[] jobs;
    private int[] workerTree;
    long[] runningTimes;

    private int[] assignedWorker;
    private long[] startTime;

    private FastScanner in;
    private PrintWriter out;

    public static void main(String[] args) throws IOException {
        new JobQueue().solve();
    }

    private void readData() throws IOException {
        numWorkers = in.nextInt();
        int m = in.nextInt();
        jobs = new int[m];
        for (int i = 0; i < m; ++i) {
            jobs[i] = in.nextInt();
        }
    }

    private void writeResponse() {
        for (int i = 0; i < jobs.length; ++i) {
            out.println(assignedWorker[i] + " " + startTime[i]);
        }
    }

    private void assignJobs() {
        // TODO: replace this code with a faster algorithm.
        assignedWorker = new int[jobs.length];
        startTime = new long[jobs.length];
        long[] nextFreeTime = new long[numWorkers];
        for (int i = 0; i < jobs.length; i++) {
            int duration = jobs[i];
            int bestWorker = 0;
            for (int j = 0; j < numWorkers; ++j) {
                if (nextFreeTime[j] < nextFreeTime[bestWorker])
                    bestWorker = j;
            }
            assignedWorker[i] = bestWorker;
            startTime[i] = nextFreeTime[bestWorker];
            nextFreeTime[bestWorker] += duration;
        }
    }
    private void buildTree(){
    	workerTree[0]=0;
    	
    	for (int i=0; i<workerTree.length/2; i++){
    		int l=leftChild(i);
    		int r=rightChild(i);
    		
    		if (i<workerTree.length){
    			workerTree[l]=l;
    		}
    		if (r<workerTree.length){
    			workerTree[r]=r;
    		}
    	}
    }
    
    private int parent(int i){
    	return i/2;
    }
    private int leftChild(int i){
    	return 2*i+1;
    }
    private int rightChild(int i){ 
    	return 2*(i+1);
    }
    
    private void assignJobsFast() {
        // TODO: replace this code with a faster algorithm.
        assignedWorker = new int[jobs.length];
        startTime = new long[jobs.length];
        runningTimes = new long[numWorkers];
        workerTree=new int[numWorkers];
        buildTree();
        
        for (int i=0; i<jobs.length; i++){
        	assignedWorker[i]=workerTree[0];
        	startTime[i]=runningTimes[workerTree[0]];
        	runningTimes[workerTree[0]]+=jobs[i];
        	siftDown(0);
        }
        
    }
    
    private void siftDown(int i){

        int n=runningTimes.length;
    	int minIndex=i;
        
        int l=leftChild(i);
        if(l<n && (runningTimes[workerTree[l]]<runningTimes[workerTree[minIndex]] ||(runningTimes[workerTree[l]]==runningTimes[workerTree[minIndex]] && workerTree[l]<workerTree[minIndex]))){
      	  minIndex=l;
        }
        
        int r=rightChild(i);
        if(r<n && (runningTimes[workerTree[r]]<runningTimes[workerTree[minIndex]] ||(runningTimes[workerTree[r]]==runningTimes[workerTree[minIndex]] && workerTree[r]<workerTree[minIndex]))){
      	  minIndex=r;
        }
        
        if (i!=minIndex){
            int tmp = workerTree[i];
            workerTree[i] = workerTree[minIndex];
            workerTree[minIndex] = tmp;
            siftDown(minIndex);
        }
    }

    public void solve() throws IOException {
        in = new FastScanner();
        out = new PrintWriter(new BufferedOutputStream(System.out));
        readData();
        assignJobsFast();
        writeResponse();
        out.close();
    }

    static class FastScanner {
        private BufferedReader reader;
        private StringTokenizer tokenizer;

        public FastScanner() {
            reader = new BufferedReader(new InputStreamReader(System.in));
            tokenizer = null;
        }

        public String next() throws IOException {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                tokenizer = new StringTokenizer(reader.readLine());
            }
            return tokenizer.nextToken();
        }

        public int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }
}
