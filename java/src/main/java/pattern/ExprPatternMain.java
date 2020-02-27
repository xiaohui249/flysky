package pattern;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExprPatternMain {

    final static Pattern p = Pattern.compile("([\\+|\\-])?[0-9]+(\\.[0-9]+)?");

    public static void main(String[] args) {
        String result = "";

        Scanner sc = new Scanner(System.in);
        String content = sc.nextLine().trim();

        if(content.length() > 0) {

            Matcher matcher = p.matcher(content);

            while (matcher.find()) {
//            System.out.println(matcher.start() + "," + matcher.end() + "," + matcher.group());
                String matchStr = matcher.group();
                if (matchStr != null && matchStr.length() >= result.length()) {
                    result = matchStr;
                }
            }
        }

        System.out.println(result);
    }
}
