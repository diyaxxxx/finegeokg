public class Node {
    private int id;
    private int label;//标签，结点类型
    private int degree;//结点的总度，出度入度的和

    public Node(){}

    public void setNode(int id, int label, int degree){
        //初始化
        this.id = id;
        this.label = label;
        this.degree = degree;
       // System.out.println("创建成功 "+"id:"+id+"  label:"+label+"  degree:"+degree+"\t");
    }

    public int getId(){ return id;}
    public int getLabel(){ return label;}
    public int getDegree(){ return degree;}

}
