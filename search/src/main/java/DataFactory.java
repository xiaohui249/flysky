import java.util.ArrayList;
import java.util.List;

/**
 * Created by bbgds on 14-3-6.
 */
public class DataFactory {
    public static DataFactory dataFactory = new DataFactory();

    private DataFactory(){}

    public DataFactory getInstance(){
        return dataFactory;
    }

    public static List<String> getInitJsonData(){
        List<String> list = new ArrayList<String>();
        String data1 = JsonUtil.obj2JsonData(new Medicine(1,"银花 感冒 颗粒","功能主治：银花感冒颗粒 ，头痛,清热，解表，利咽。", 0));
        String data2 = JsonUtil.obj2JsonData(new Medicine(2,"感冒 止咳糖浆","功能主治：感冒止咳糖浆,解表清热，止咳化痰。", 1));
        String data3 = JsonUtil.obj2JsonData(new Medicine(3,"感冒灵颗粒","功能主治：解热镇痛。头痛 ,清热。", 2));
        String data4 = JsonUtil.obj2JsonData(new Medicine(4,"感冒 灵胶囊","功能主治：银花感冒颗粒 ，头痛,清热，解表，利咽。", 3));
        String data5 = JsonUtil.obj2JsonData(new Medicine(5,"仁和 感冒 颗粒","功能主治：疏风清热，宣肺止咳,解表清热，止咳化痰。", 4));
        String data6 = JsonUtil.obj2JsonData(new Medicine(6, "苹果 笔记本电脑","非常好用", 1));
        String data7 = JsonUtil.obj2JsonData(new Medicine(7, "苹果 笔记本","非常好用", 1));
        String data8 = JsonUtil.obj2JsonData(new Medicine(8, "苹果 电脑","非常好用", 1));
        list.add(data1);
        list.add(data2);
        list.add(data3);
        list.add(data4);
        list.add(data5);
        list.add(data6);
        list.add(data7);
        list.add(data8);
        return list;
    }
}
