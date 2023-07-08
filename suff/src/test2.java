import com.sun.javafx.collections.MappingChange;
import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.util.Pair;


import java.io.*;
import java.util.*;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class test2 {//有filter版本，最终版本，先用filter.java生成filter文件
    //数据图
    static int d_V;
    static int d_E;
    static Graph dataGraph ;
    static Node[] dataGraphNodes ;//开辟数组空间
    //查询图
    static int q_V = 5;
    static int q_E = 6;
    static Graph queryGraph;
    static Node[] queryGraphNodes;//开辟数组空间

    static ArrayList<Integer> q = new ArrayList<>();//查询次序
    static int count = 0;//记录查询结果的个数；
    static Map<Pair<Integer, Integer>, Boolean> dataGraphEdges;

    static MyBloomFilter dataFilter3 = new MyBloomFilter();
    static MyBloomFilter dataFilter4 = new MyBloomFilter();
    static MyBloomFilter queryFilter3 = new MyBloomFilter();
    static MyBloomFilter queryFilter4 = new MyBloomFilter();
    //深度优先查找、回溯
    //（查询结果结点数组，当前查询列表的下标，当前数据图被查询的结点的下标）

    public static void Enumerate(ArrayList<Integer> f, Boolean[] visited, int n, int d_s){
        if(n == q_V){//判断边是否符合条件
            Boolean flag = true;
            ArrayList<Integer> querylist = new ArrayList<>();
            ArrayList<Integer> datalist = new ArrayList<>();
            for(int j = 0; j < q_V; j++){
                querylist = queryGraph.adj(j);
                for(int i = 0; i < querylist.size(); i++){
                    //System.out.println(f.get(j)+" "+f.get(querylist.get(i)));
                    Pair<Integer,Integer> d_pair1 = new Pair<Integer,Integer>(f.get(j),f.get(querylist.get(i)));
                    Pair<Integer,Integer> d_pair2 = new Pair<Integer,Integer>(f.get(querylist.get(i)),f.get(j));

                    if(dataGraphEdges.containsKey(new Pair<>(f.get(j),f.get(querylist.get(i))))
                            || dataGraphEdges.containsKey(new Pair<>(f.get(querylist.get(i)),f.get(j))))
                    {}else{
                        flag = false;
                    }
                }
            }
            if(flag){
                System.out.println("符合条件："+f);
                count += 1;
            }else{
                //System.out.print(f);System.out.println("边 不符合条件");
            }
            return;
        }
        int[] C = new int[d_V];//记录和当前查询点label相同的数据图中点的id
        int k = 0;
        if(d_s == -1){
            System.out.println("查询图的点："+q.get(n));
            boolean q3, q4, d3, d4;
            // System.out.println("q.get(n):"+q.get(n)+"   J:"+j);
            q3 = queryFilter3.contains(q.get(n));q4 = queryFilter4.contains(q.get(n));
            ArrayList<Integer> cha = new ArrayList<>();
            System.out.println("q3 q4:"+q3+" "+q4);
            for(int j = 0; j < d_V; j++){
                ///here加上Filter
                if(queryGraphNodes[q.get(n)].getLabel() == dataGraphNodes[j].getLabel()){
                    d3 = dataFilter3.contains(j);d4 = dataFilter4.contains(j);
                    //System.out.println(j+":"+d3+" "+d4);
                    //System.out.println("测试："+q3+" "+d3+" "+q4+" "+d4);
                    if(!q3 && !q4){
                        C[k] = j;
                        k++;
                        cha.add(j);
                        //System.out.println(j);
                    } else if (!q3 && q4) {
                        if(d4){
                            C[k] = j;
                            cha.add(j);
                            //System.out.println(j);
                        }

                    } else if (q3 && !q4) {
                        if(d3){
                            C[k] = j;
                            k++;
                            cha.add(j);
                            //System.out.println(j);
                        }

                    } else if (q3 && q4) {
                        if(d3 && d4){
                            C[k] = j;
                            k++;
                            cha.add(j);
                            //System.out.println(j);
                        }
                    }
                }

            }
            System.out.println("Filter过滤结果："+cha);
            System.out.println(cha.size());
        }else{
            ArrayList<Integer> d_near = new ArrayList<>();//邻接表
            d_near = dataGraph.adj(d_s);
//            System.out.println("d_s:"+d_s);
//            System.out.println("d_s邻接表："+d_near);
//            System.out.println("q.get(n):"+q.get(n));
            for(int j = 0; j < d_near.size(); j++){
                //System.out.println(queryGraphNodes[q.get(n)].getLabel()+"对比"+dataGraphNodes[d_near.get(j)].getLabel());
                if(queryGraphNodes[q.get(n)].getLabel() == dataGraphNodes[d_near.get(j)].getLabel()){
                    C[k] = d_near.get(j);
                    k++;
                }
            }

        }
        for(int j = 0; j < k; j++){
            Boolean flag = false;//标志，查询f数组中是否已经存在C[j]结点
            for(int i = 0; i < f.size(); i++){
                if(C[j] == f.get(i)) flag = true;
            }
            if(flag == false){//不存在的时候进行深度搜索
                f.add(C[j]);
                visited[C[j]] = true;
                Enumerate(f,visited,n+1, C[j]);
                visited[C[j]] = false;
                f.remove(f.size() - 1);//删除最后一个元素
            }
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        //数据图 数据 从文件中读入
        //Thread.sleep(20000);
        String dataEdge = "C:\\Users\\14097\\Desktop\\postgra\\ustb\\suff\\dataset\\osm1w.graph";
        dataGraphEdges = new HashMap<Pair<Integer,Integer>,Boolean>();
        try(Scanner sc = new Scanner(new FileReader(dataEdge))){
            String firstLine = sc.nextLine();
            String[] first = firstLine.split("\\s+");
            d_V = Integer.parseInt(first[1]);
            d_E = Integer.parseInt(first[2]);
            dataGraph = new Graph(d_V);
            dataGraphNodes = new Node[d_V];
            // System.out.println(d_V+" "+d_E);
            for(int i = 0; i < d_V; i++){
                String v_line = sc.nextLine();
                String[] str = v_line.split("\\s+");
                int id = Integer.parseInt(str[1]);
                int label = Integer.parseInt(str[2]);
                int degree = Integer.parseInt(str[3]);
                dataGraphNodes[i] = new Node();
                dataGraphNodes[i].setNode(id,label,degree);
                // System.out.println(id+" "+label+" "+degree);
            }
            for(int i = 0; i < d_E; i++){
                String e_line = sc.nextLine();
                String[] str = e_line.split("\\s+");
                int s_id = Integer.parseInt(str[1]);
                int e_id = Integer.parseInt(str[2]);
                dataGraph.addEdge(s_id, e_id);
                dataGraphEdges.put(new Pair<>(s_id, e_id), true);dataGraphEdges.put(new Pair<>(e_id, s_id), true);
                //System.out.println(s_id+" "+e_id+" ");
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        String queryEdge = "C:\\Users\\14097\\Desktop\\postgra\\ustb\\suff\\query1.txt";
        try(Scanner sc = new Scanner(new FileReader(queryEdge))){
            String firstLine = sc.nextLine();
            String[] first = firstLine.split("\\s+");
            q_V = Integer.parseInt(first[1]);
            q_E = Integer.parseInt(first[2]);
            queryGraph = new Graph(q_V);
            queryGraphNodes = new Node[q_V];
            for(int i = 0; i < q_V; i++){
                String v_line = sc.nextLine();
                String[] str = v_line.split("\\s+");
                int id = Integer.parseInt(str[1]);
                int label = Integer.parseInt(str[2]);
                int degree = Integer.parseInt(str[3]);
                queryGraphNodes[i] = new Node();
                queryGraphNodes[i].setNode(id,label,degree);
                // System.out.println(id+" "+label+" "+degree);
            }
            for(int i = 0; i < q_E; i++){
                String e_line = sc.nextLine();
                String[] str = e_line.split("\\s+");
                int s_id = Integer.parseInt(str[1]);
                int e_id = Integer.parseInt(str[2]);
                queryGraph.addEdge(s_id, e_id);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        long start,end;
        start = System.currentTimeMillis();

        DepthFirstSearch bs = new DepthFirstSearch(queryGraph,0);//就从0开始深度遍历！！
        q = bs.getList();
        System.out.println("结点查询次序："+q);
        //读取路径
        ArrayList<Integer> fR3 = new ArrayList<>();
        String filterData3 = "C:\\Users\\14097\\Desktop\\postgra\\ustb\\suff\\filter\\osm1w3.txt";
        try(Scanner sc = new Scanner(new FileReader(filterData3))){
            while(sc.hasNext()){
                String s = sc.nextLine();
                fR3.add(Integer.parseInt(s));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Collections.sort(fR3);
        for(int i = 0; i < fR3.size(); i++){
            dataFilter3.add(fR3.get(i));
        }
        System.out.println("fR3:"+fR3);
        System.out.println("size3:"+fR3.size());
        System.out.println(dataFilter3.contains(5));

        //GetFilter getDataFilter4 = new GetFilter(4);
        //getDataFilter4.dfs(dataGraph);
        ArrayList<Integer> fR4 = new ArrayList<>();
        String filterData4 = "C:\\Users\\14097\\Desktop\\postgra\\ustb\\suff\\filter\\osm1w4.txt";
        try(Scanner sc = new Scanner(new FileReader(filterData4))){
            while(sc.hasNext()){
                String s = sc.nextLine();
                fR4.add(Integer.parseInt(s));
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Collections.sort(fR4);
        for(int i = 0; i < fR4.size(); i++){
            dataFilter4.add(fR4.get(i));

            //System.out.println(fR4.get(i)+":"+dataFilter4.contains(fR4.get(i)));
        }
        System.out.println("fR4:"+fR4);
        System.out.println("size4:"+fR4.size());
        System.out.println(dataFilter4.contains(12));

        GetFilter getQueryFilter3 = new GetFilter(3);
        getQueryFilter3.dfs(queryGraph);
        ArrayList<Integer> q_R3 = new ArrayList<>();
        q_R3 = getQueryFilter3.getFilterResult();
        Collections.sort(q_R3);
        for(int i = 0; i < q_R3.size(); i++){
            queryFilter3.add(q_R3.get(i));
        }
        System.out.println("q_R3:"+q_R3);
        System.out.println("size3:"+q_R3.size());
        System.out.println(queryFilter3.contains(0));

        GetFilter getQueryFilter4 = new GetFilter(4);
        getQueryFilter4.dfs(queryGraph);
        ArrayList<Integer> q_R4 = new ArrayList<>();
        q_R4 = getQueryFilter4.getFilterResult();
        Collections.sort(q_R4);
        for(int i = 0; i < q_R4.size(); i++){
            queryFilter4.add(q_R4.get(i));
        }
        System.out.println("q_R4:"+q_R4);
        System.out.println("size4:"+q_R4.size());
        System.out.println(queryFilter4.contains(0));


        //Examination.start();
        //long stratMem = r.totalMemory() ;
        long middle = System.currentTimeMillis();
        ArrayList<Integer> f = new ArrayList<>();
        //int[] f = new int[q_V];//数目跟查询图结点数一致
        Boolean[] visited = new Boolean[d_V];//记录结点是否被查询过
        for(int i = 0; i < d_V; i++){
            visited[i] = false;
        }
        Enumerate(f, visited,0, -1);//应该有一个查询图的查询次序队列
        System.out.println("count:"+count);

        end = System.currentTimeMillis();
        //Examination.end();
        //long endMem = r.freeMemory();
        System.out.println("start time:" + start+ "; end time:" + end+ "; Run Time:" + (end - start) + "(ms)");
        System.out.println(" Run Time:" + (end - middle) + "(ms)");
        //System.out.println("内存消耗："+String.valueOf((stratMem - endMem) / 1024/1024)+"MB");

    }
}