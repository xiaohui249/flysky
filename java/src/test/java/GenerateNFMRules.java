import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * 根据IP和端口生成网卡过滤规则
 *
 * @author xiaoh
 * @create 2018-07-21 15:31
 **/
public class GenerateNFMRules {
    public static void main(String[] args) {
        try(BufferedReader reader = new BufferedReader(new FileReader("E:/ip-port.json"))) {
            String jsonStr = reader.readLine();
            String rule = "/opt/netronome/bin/rules -A -n \"%s\" -k -b --ipv4_da=%s --dport=%s --ipv4_proto=6 --host_dest_id=15 -a VIA_HOST -M";
            String ruleDrop = "/opt/netronome/bin/rules -A -n \"rule_default\" -p100  -b -a DROP -M";
            JSONObject json = JSON.parseObject(jsonStr);
            System.out.println("json size = " + json.size());
            int i = 1;
            for(String key : json.keySet()) {
                String[] ipAndPort = key.split(":");
                System.out.println(String.format(rule, "rule"+(i++), ipAndPort[0], ipAndPort[1]));
            }
            System.out.println(ruleDrop);
            System.out.println("All rule include drop-rule is: " + i);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
