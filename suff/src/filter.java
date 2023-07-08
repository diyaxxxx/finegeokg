import javafx.util.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class filter {//filter文件生成
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
    //深度优先查找、回溯
    //（查询结果结点数组，当前查询列表的下标，当前数据图被查询的结点的下标）
    public static void main(String[] args) throws IOException, InterruptedException {
        //数据图 数据 从文件中读入
        Thread.sleep(20000);
        long start,end;
        start = System.currentTimeMillis();
        //源数据文件
        String dataEdge = "C:\\Users\\14097\\Desktop\\postgra\\ustb\\suff\\dataset\\osm10w.graph";
       // dataGraphEdges = new HashMap<Pair<Integer,Integer>,Boolean>();
        try(Scanner sc = new Scanner(new FileReader(dataEdge))){
            String firstLine = sc.nextLine();
            String[] first = firstLine.split("\\s+");
            d_V = Integer.parseInt(first[1]);
            d_E = Integer.parseInt(first[2]);
            dataGraph = new Graph(d_V);
            dataGraphNodes = new Node[d_V];
            System.out.println("d_V:"+d_V+"d_E:"+d_E);
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
               // dataGraphEdges.put(new Pair<>(s_id, e_id), true);dataGraphEdges.put(new Pair<>(e_id, s_id), true);
                //System.out.println(s_id+" "+e_id+" ");
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        GetFilter getDataFilter = new GetFilter(4);
        getDataFilter.dfs(dataGraph);
        //getDataFilter.writeTo("f1.bin");
        ArrayList<Integer>  filterResult = new ArrayList<>();
        filterResult = getDataFilter.getFilterResult();
        BufferedWriter bw = new BufferedWriter(new FileWriter("filter/osm10w4.txt"));
        for(int i = 0; i < filterResult.size(); i++){
            String s = Integer.toString(filterResult.get(i));
            bw.write(s);
            bw.newLine();
            bw.flush();
        }
        bw.close();
        //读取路径
//        String filterData = "C:\\Users\\14097\\Desktop\\postgra\\ustb\\suff\\file\\osm1k3.txt";
//        try(Scanner sc = new Scanner(new FileReader(filterData))){
//            while(sc.hasNext()){
//                String s = sc.nextLine();
//                fR.add(Integer.parseInt(s));
//            }
//
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//        System.out.println("dude:"+fR);
//        MyBloomFilter mbf = new MyBloomFilter();
//        for(int i = 0; i < fR.size(); i++){
//            mbf.add(fR.get(i));
//        }

        //System.out.println("hh"+mbf.contains(8));

        //DepthFirstSearch bs = new DepthFirstSearch(dataGraph,0);//就从0开始深度遍历！！


       // ArrayList<Integer> f = new ArrayList<>();
        //int[] f = new int[q_V];//数目跟查询图结点数一致
        //Boolean[] visited = new Boolean[d_V];//记录结点是否被查询过
       // for(int i = 0; i < d_V; i++){
        //    visited[i] = false;
       // }

        //System.out.println(count);

        end = System.currentTimeMillis();
        System.out.println("start time:" + start+ "; end time:" + end+ "; Run Time:" + (end - start) + "(ms)");


    }
}
