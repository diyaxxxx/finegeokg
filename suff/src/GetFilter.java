import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class GetFilter {
    //索引代表顶点，值表示当前顶点是否已经被搜索
    private boolean[] marked;
    private int num;
    private ArrayList<Integer> q ;
    ArrayList<Integer> filterResult = new ArrayList<>();
    MyBloomFilter mbf = new MyBloomFilter();
    public GetFilter(int num){
        this.num = num;
        System.out.println("num:"+num);
    }

    //深度优先搜索
    public void dfs(Graph g) throws IOException {//g:查找的图，n:环的结点数
        for(int i = 0; i < g.V(); i++){
            //System.out.println("此刻遍历结点id是："+i);
            boolean[] visited = new boolean[g.V()];
            ArrayList<Integer> parent = new ArrayList<>();//此时应parent为空
            for(int j = 0; j < g.V(); j++){
                visited[j] = false;
            }
            if(!visited[i] && !filterResult.contains(i)){
            //if(!visited[i]){
                //System.out.println("第"+i+"个");
                System.out.println("--开始查"+i+"--");
                visited[i] = true;
                parent.add(i);
                cycleDFS(g, i, visited, parent, 0);
            }
        }
        Collections.sort(filterResult);
        System.out.println(filterResult);
        System.out.println("filterSize:"+filterResult.size());
    }
    public ArrayList<Integer> getFilterResult(){
        return filterResult;
    }
    public void cycleDFS(Graph g, int u, boolean[] visited, ArrayList<Integer> parent, int n){
        n++;
        if(n > num){
            //parent.clear();
            return;
        }
        //parent.add(u);
        //System.out.println("..."+parent+"n:"+n);
        for(int i = 0; i <g.adj(u).size(); i++ ){
//            if(parent.size() == 3 && parent.get(0) == 561 && parent.get(1) == 566 && parent.get(2) ==567){
//                System.out.println("z:"+parent);
//                System.out.print(g.adj(u).get(i));
//                System.out.println(" "+visited[g.adj(u).get(i)]);
//                System.out.println("n:"+n);
//                System.out.println(parent.get(0));
//                System.out.println(parent.get(0) == g.adj(u).get(i));
//                System.out.println(n == num);
//
//            }
            if(!visited[g.adj(u).get(i)]){//没有访问过
                visited[g.adj(u).get(i)] = true;
                parent.add(g.adj(u).get(i));
//                if(parent.size() == 3 && parent.get(0) == 561 && parent.get(1) == 566 && parent.get(2) ==567){
//                    System.out.println(parent);
//                    System.out.println(g.adj(567));
//                }
                cycleDFS(g, g.adj(u).get(i), visited, parent, n);
                parent.remove(parent.size()-1);
                visited[g.adj(u).get(i)] = false;
            } else if (parent.get(0).equals(g.adj(u).get(i)) && n == num) {//排除已经visited过的父节点
                    //System.out.println("成功的："+parent);
                    for(int j = 0; j < parent.size(); j++){
                        if(!filterResult.contains(parent.get(j))){
                            filterResult.add(parent.get(j));
                        }
                    }

            }
        }
    }

    //判断w顶点是否被搜索过
    public MyBloomFilter getMbf(){
        return mbf;
    }

}
