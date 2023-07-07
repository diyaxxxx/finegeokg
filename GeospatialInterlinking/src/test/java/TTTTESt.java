import com.ustb.common.Constant;
import com.ustb.fsgr.*;
import com.ustb.model.GeoDataSet;
import com.ustb.model.GeoNode;
import com.ustb.util.CsvReader;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Envelope;

import java.io.*;
import java.util.*;

/**
 * @author weib
 * @date 2022-03-30 17:31
 */
public class TTTTESt {

    @Test
    public void test(){
        String test = "\"POLYGON ((11854971.319200002 4649131.164800003,11854971.764199998 4649138.166500002,11854971.3228 4649131.164499998,11854971.319200002 4649131.164800003))\", 3, 57428,1,草地,14.0353383778,0.01266980276";
        String[] tets = test.split("\"");
//        String ttt = "\t19\t051\t00465214\t19051\tDavis\tDavis County\t06\tH1\tG4020\t\t36900\t\tA\t1300661881\t6904096\t+40.7480892\t-092.4103453";
//        String[] test2 = ttt.split("\t");
//        System.out.println(test2[5]);
        for (String tet : tets) {
            System.out.println(tet);
        }
        String[] tets2 = tets[2].split(",");
        for (String tet : tets2) {
            System.out.println(tet);
        }

//        for (String s : test2) {
//            System.out.println(s);
//        }
//
//        System.out.println(test2[16]);
//        System.out.println(test2[17]);
    }


    @Test
    public void test34(){
        for (int i = 0; i < 100; i++) {
            int c = 9;
            if ( (c = new Random().nextInt(9) + 1 )>= 9)
             System.out.println("大于9  "+c);


        }
    }

    @Test
    public void te1234st() throws IOException, InterruptedException {
        String file = Constant.DATASETDIR + "COUNTY.csv";
        GeoDataSet geoDataSet = CsvReader.readDataSet("\"", file);
        double ax = geoDataSet.getAverageX();
        double ay = geoDataSet.getAverageY();

        GeoNode g1 = geoDataSet.getGeoNodes().get(0);
        GeoNode g2 = geoDataSet.getGeoNodes().get(1);

        if (!DirectionValidator.positiveSeq(g1, g2)) {
            GeoNode t = g1;
            g1 = g2;
            g2 = t;
        }
        System.out.println(DirectionValidator.varifyRalation(g1, g2));
        GeoNode last = g1;
        for (GeoNode geoNode : geoDataSet.getGeoNodes()) {
            System.out.println(DirectionValidator.varifyRalation(last, geoNode));
            last = geoNode;
            double y = geoNode.getGeometry().getEnvelopeInternal().getHeight() / ay;
            double x = geoNode.getGeometry().getEnvelopeInternal().getWidth() / ax;
            Envelope envelope = geoNode.getGeometry().getEnvelopeInternal();
            int maxX = (int) Math.floor(envelope.getMaxX() / ax);
            int maxY = (int) Math.floor(envelope.getMaxY() / ay);
            int minX = (int) Math.floor(envelope.getMinX() / ax);
            int minY = (int) Math.floor(envelope.getMinY() / ay);
            System.out.print(x + "   " + y);
            System.out.println(" ---" + minX + " " + maxX + " " + minY + " " + maxY+ " ---- " + envelope.getMaxX() + " "+envelope.getMinX());
            Thread.sleep(1000);
        }
    }

    @Test
    public void t12341est(){
        double d1 = -4.141276228016359 / 26.36712672392632;
        System.out.println(d1);
        System.out.println(Math.atan(d1) / Math.PI * 180 );
    }

    @Test
    public void test11234() throws IOException {
        BufferedReader bfr = new BufferedReader(new FileReader("E:\\0-program\\dataSet\\EDGES.csv"));
        BufferedWriter bfw = new BufferedWriter(new FileWriter("E:\\0-program\\dataSet\\EDGES.csv.spl30w"));
        BufferedWriter bfw2 = new BufferedWriter(new FileWriter("E:\\0-program\\dataSet\\EDGES.csv.spl300w"));
        String line = bfr.readLine();
        int count = 0;
        while (line != null && count < 300000) {
            bfw.write(line);
            bfw.newLine();
            line = bfr.readLine();
            count++;
        }
        while (line != null && count < 3000000) {
            bfw2.write(line);
            bfw2.newLine();
            line = bfr.readLine();
            count++;
        }
        bfw.close();
        bfw2.close();
        bfr.close();

    }


    @Test
    public void t1241234est() throws IOException, ClassNotFoundException {
        String file = Constant.DATASETDIR + "AREAWATER.csv";
        String delimiter = "\"";
        List<GeoSpatialRelation> gsr = GrgCreator.getGraph(delimiter, file);
        FileWriter fw = new FileWriter("E:\\0-program\\dataSet\\lengths.txt");
        BufferedWriter bw = new BufferedWriter(fw);
        for (GeoSpatialRelation g : gsr) {
            bw.write("" + (g.getLength() * 10000));
            bw.newLine();
        }
        bw.flush();
        bw.close();
        fw.close();
    }

    @Test
    public void te1235142st() throws IOException {

        FileWriter fw = new FileWriter("E:\\0-program\\dataSet\\lengths2.txt");
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write("123");
        bw.flush();


    }


    @Test
    public void ttttttt() throws IOException, ClassNotFoundException {
        File file = new File(Constant.DATASETDIR + "osm100w.csv.objs");
        List<GeoSpatialRelation> result = new ArrayList<>();
        if (file.exists()) {
            System.out.println("反序列化！");
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            result = (List<GeoSpatialRelation>) ois.readObject();
            ois.close();
        }
        HashMap<Integer, Integer> map = new HashMap<>();
        for (GeoSpatialRelation obj : result) {
            if (map.get(obj.getGsrGeoType()) == null) {
                map.put(obj.getGsrGeoType(), 1);
                continue;
            }
            int i = map.get(obj.getGsrGeoType());
            map.put(obj.getGsrGeoType(), ++i);
        }
        for (Integer key : map.keySet()) {
            System.out.println(key + " "  + map.get(key));
        }
    }

    @Test
    public void te123454st() throws IOException {
        GeoDataSet geoDataSet = CsvReader.readDataSet("\"", "D:\\桌面\\lll.csv");
        System.out.println(geoDataSet.getGeoNodes().get(0).getGeometry().getGeometryType());
//        System.out.println(geoDataSet);
    }

    @Test
    public void tes235265t(){
        Random r = new Random(1);
        int i = 100;
        while (i > 0) {
            System.out.println(r.nextInt()%180);
            i--;
        }
    }

    @Test
    public void tttestasdf() throws IOException {
        String filePath = "C:\\Users\\wwei\\Desktop\\test.txt";
        Writer w = new FileWriter(filePath);
        w.write("123");
        w.flush();
        w.close();
    }


    /**
     *
     * @param startYear
     * @param startMonth
     * @param startDay
     * @param endYear
     * @param endMonth
     * @param endDay
     * @param num：每行显示的日历个数
     * 算法主要思想：输入两个日期，将两个日期之间的日期按照正常日历格式，一个月一个月的输出，存入一个字符串中，将字符串分割为n（
     *           两个日期之间的月数）个字符数组中，再将每个字符数组按行分割，将分割得到的结果存入一个二维数组中即可。
     */
    public static void outprint(int startYear, int startMonth, int startDay, int endYear, int endMonth, int endDay,
                                int num) {
        Calendar start = new GregorianCalendar(startYear, startMonth - 1, startDay);// 月份从0开始，按照习惯，应减1
        Calendar end = new GregorianCalendar(endYear, endMonth - 1, endDay);
        if(start.compareTo(end) > 0){
            System.err.println("后面的日期必须大于前面的日期!");
            return;
        }
        int startWeekDay = start.get(Calendar.DAY_OF_WEEK);// 开始日是星期几
        int index = 0;// 索引位置，用以判断是不是周六，周六需换行
        int thisMonth = startMonth;// 记录第几月
        StringBuffer sb = new StringBuffer();
        sb.append(startYear + "年" + thisMonth + "月\t\t\t\t\t\t\n日\t一\t二\t三\t四\t五\t六\t\n");
        for (int i = 0; i < startWeekDay - 1; i++) {// 边界处理，最开始空多少空格
            sb.append("\t");
        }
        while (start.compareTo(end) <= 0) {// 包含末尾那天
            sb.append(start.get(Calendar.DATE) + "\t");
            if ((index++ + startWeekDay) % 7 == 0) {// 周六换行，索引增1
                sb.append("\n");
            }
            start.add(Calendar.DAY_OF_MONTH, 1);// 日期加1
            if (start.get(Calendar.DATE) == 1 && start.compareTo(end) <= 0) {// 跨月处理
                if (thisMonth == 12) {// 跨年处理
                    thisMonth = 0;
                    startYear++;
                }
                for (int i = 0; i < 8 - start.get(Calendar.DAY_OF_WEEK); i++) {// 每月月末后面添加的空格数
                    sb.append("\t");
                }
                sb.append("\n\n" + startYear + "年" + ++thisMonth + "月\t\t\t\t\t\t\n日\t一\t二\t三\t四\t五\t六\t\n");
                for (int j = 0; j < start.get(Calendar.DAY_OF_WEEK) - 1; j++) {// 每月结束换行后第二月开始要空的天数
                    sb.append("\t");
                }
            }
        }
        String[] arr = sb.toString().split("\n\n");// 将获取的日历切割，一个月为一个单位
        String[][] array = new String[arr.length][8];// 保存结果
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < 8; j++) {
                array[i][j] = "\t\t\t\t\t\t\t";// 初始化结果数组，每行7个空格
            }
        }
        for (int i = 0; i < arr.length; i++) {
            String[] a = arr[i].split("\n");// 每个月的日历按行切割
            for (int j = 0; j < a.length; j++) {
                array[i][j] = a[j];// 将每行结果存入结果，其中i标示第几个数组（月），j表示第几行
            }
        }
        for (int i = 0; i < array.length; i += num) {
            for (int j = 0; j < array[i].length; j++) {
                for (int k = 0; k < num && i + k < array.length; k++) {// 每行显示多个日历
                    System.out.print(array[i + k][j] + "\t");// 每行每个日历之间用空格隔开
                }
                System.out.println();// 每行显示结束换行
            }
            System.out.println();
        }
    }



    @Test
    public void tes234253t(){
        System.out.println("请输入两个日期和每行显示的日历个数（示例：2016 2017 3）：");
        Scanner scanner=new Scanner(System.in);
        String in = scanner.nextLine();
        try{
            String [] inArr=in.split(" ");
            int startYear=Integer.parseInt(inArr[0]);
//            int startYear=2012;
            int startMonth=1;
            int startDay=1;
            int endYear=Integer.parseInt(inArr[1]);
            int endMonth=12;
            int endDay=31;
            int num=Integer.parseInt(inArr[2]);
            outprint(startYear, startMonth, startDay, endYear, endMonth, endDay, num);
        }catch(Exception e){
            System.err.println("输入格式错误！");
        }finally {
            scanner.close();
        }

    }

}
