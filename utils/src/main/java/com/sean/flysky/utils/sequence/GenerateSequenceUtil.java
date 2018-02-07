package com.sean.flysky.utils.sequence;

import java.text.*;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: huixiao200068
 * Date: 13-6-20
 * Time: 下午5:04
 * 根据时间生成唯一ID
 */
public class GenerateSequenceUtil {
    /** .log */
    private static final Logger logger = Logger.getLogger(GenerateSequenceUtil.class.getName());

    /** The FieldPosition. */
    private static final FieldPosition HELPER_POSITION = new FieldPosition(0);

    /** This Format for format the data to special format. */
    private final static Format dateFormat = new SimpleDateFormat("MMddHHmmssS");

    /** This Format for format the number to special format. */
    private final static NumberFormat numberFormat = new DecimalFormat("0000");

    /** This int is the com.sean.flysky.utils.sequence number ,the default value is 0. */
    private static int seq = 0;

    private static final int MAX = 9999;

    /**
     * 时间格式生成序列，速度较快
     * @return String
     */
    public static synchronized String generateSequenceNo() {

        Calendar rightNow = Calendar.getInstance();

        StringBuffer sb = new StringBuffer();

        dateFormat.format(rightNow.getTime(), sb, HELPER_POSITION);

        numberFormat.format(seq, sb, HELPER_POSITION);

        if (seq == MAX) {
            seq = 0;
        } else {
            seq++;
        }

//        logger.info("THE SQUENCE IS :" + sb.toString());

        return sb.toString();
    }

    private static long tmpID = 0;
    private static boolean tmpIDlocked = false;

    /**
     * 生成19位的唯一ID
     * @return
     */
    public static long getUniqueId() {
        long ltime = 0;
        while (true) {
            if (tmpIDlocked == false) {
                tmpIDlocked = true;
                ltime = Long.valueOf(new SimpleDateFormat("yyMMddhhmmssSSS")
                        .format(new Date()).toString()) * 10000;
                if (tmpID < ltime) {
                    tmpID = ltime;
                } else {    //如果1ms之内，出现重复的ltime，则使用临时变量tmpID来区别
                    tmpID = tmpID + 1;
                    ltime = tmpID;
                }
                tmpIDlocked = false;
                return ltime;
            }
        }
    }
}
