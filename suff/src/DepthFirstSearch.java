import java.util.ArrayList;
import java.util.LinkedList;

public class DepthFirstSearch {
    //索引代表顶点，值表示当前顶点是否已经被搜索
    private boolean[] marked;

    //
    private ArrayList<Integer> q ;
    public DepthFirstSearch(Graph g,int s){
        //初始化marked数组
        this.marked = new boolean[g.V()];
        this.q = new ArrayList<>();//查询次序
        dfs(g,s);
    }

    //广度优先搜索
    private LinkedList<Integer> queue = new LinkedList<>();
    public void bfs(Graph g, int s) {
        //把v顶点标识为已搜索
        queue.add(s);
        //marked[s] = true;
        while (!queue.isEmpty()) {
            int k = queue.removeFirst();
            if (marked[k] == true) {
                continue;
            }
            //System.out.print(k + "-");
            q.add(k);
            //System.out.println(q[i]);
            marked[k] = true;
            for (Integer w : g.adj(k)) {
                if (!marked[w]) {
                    queue.add(w);
                }
            }
        }
    }
    //深度优先搜索
    public void dfs(Graph g, int s){
        if(!marked[s]){
            q.add(s);
            marked[s] = true;
            for(int i = 0; i < g.adj(s).size();i++) {
                if(marked[g.adj(s).get(i)] == false){
                    dfs(g,g.adj(s).get(i));
                }
            }
        }
    }

    //判断w顶点是否被搜索过
    public boolean marked(int w){
        return marked[w];
    }
    public ArrayList<Integer> getList(){
        //System.out.println(q);
        return q;
    }
}
