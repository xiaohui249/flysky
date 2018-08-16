package com.sean.flysky.mysql.dbutil;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.*;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * 数据库操作类
 * Created by sean on 2016/6/24.
 */
public class DbHelper {

    private final static Logger logger = LoggerFactory.getLogger(DbHelper.class);

    private static QueryRunner queryRunner;

    /**
     * 新增数据数据
     * @param model
     * @return Integer 如果主键自增，返回值表示最新记录的ID，否则，表示插入数据条数
     */
    public static boolean insert(BaseModel model) {
        String[] paramFields = getParamFields(model);
        String sql = createInsertSql(model, paramFields);
        Object[] values = getPropValues(model, paramFields);
        queryRunner = new QueryRunner(ConnectionFactory.getDataSource());
        int count = 0;
        try {
            count = queryRunner.update(sql, values);
        } catch (SQLException e) {
            logger.error("insert fail! execute sql : {}", sql, e);
        }

        return count > 0 ? true : false;
    }

    /**
     * 批量插入
     * @param modelList
     * @return
     */
    public static int[] batchInsert(List<? extends BaseModel> modelList) {
        int[] ret = new int[0];
        if(modelList != null && modelList.size() > 0) {
            BaseModel model = modelList.get(0);
            String[] paramFields = getParamFields(model);
            String sql = createInsertSql(model, paramFields);
            int size = modelList.size();
            Object[][] values = new Object[size][];
            for(int i=0; i<size; i++) {
                values[i] = getPropValues(modelList.get(i), paramFields);
            }
            
            queryRunner = new QueryRunner(ConnectionFactory.getDataSource());
            try {
                long s = System.currentTimeMillis();
                ret = queryRunner.batch(sql, values);
                logger.debug("execute batchInsert {} records, cost: {}ms", size, System.currentTimeMillis()-s);
            } catch (SQLException e) {
                logger.error("batchInsert fail! execute sql : {}", sql, e);
                return new int[0];
            }
        }

        return ret;
    }

    /**
     * 创建插入语句sql
     * @param model 对象
     * @param paramFields 字段数组
     * @return
     */
    private static String createInsertSql(BaseModel model, String[] paramFields) {
        StringBuffer sb = new StringBuffer("INSERT INTO ").append(model.getTableName()).append("(");
        String fieldStr = arrayToStringWithoutBracket(paramFields);
        sb.append(fieldStr).append(") VALUES (");

        int paramNum = paramFields.length;

        for(int i=0; i<paramNum; i++) {
            sb.append("?,");
        }

        return sb.substring(0, sb.length() - 1) + ")" ;
    }

    /**
     * 获取sql中使用的参数字段
     * @param model 对象
     * @return
     */
    private static String[] getParamFields(BaseModel model) {
        String[] fields = model.getFields();
        String[] primaryKeys = model.getPrimaryKeys();

        String[] paramFields = ArrayUtils.removeElement(fields, "serialVersionUID");
        if(primaryKeys.length == 1 && model.getAutoIncrement()) {
            paramFields = ArrayUtils.removeElement(paramFields, primaryKeys[0]);
        }

        return paramFields;
    }

    /**
     * 获取对象相应字段的值
     * @param model 对象
     * @param fields 字段数组
     * @return
     */
    private static Object[] getPropValues(BaseModel model, String[] fields) {
        Class c = model.getClass();
        Method[] methods = c.getMethods();
        Map<String, Method> methodMap = new HashMap<>();
        for(Method method : methods) {
            methodMap.put(method.getName(), method);
        }
        methods = null;

        Method tmpMethod;
        String methodName;
        final Object[] emptyParam = new Object[]{};
        Object[] values = new Object[fields.length];
        for(int i=0; i<fields.length; i++) {
            methodName = "get" + fields[i].toUpperCase().charAt(0) + fields[i].substring(1);
            tmpMethod = methodMap.get(methodName);
            if(tmpMethod != null) {
                try {
                    values[i] = tmpMethod.invoke(model, emptyParam);
                } catch (Exception e) {
                    logger.error("fail! get value of property named {}", fields[i], e);
                    return null;
                }
            }
        }
        return values;
    }

    private static String arrayToStringWithoutBracket(String[] strings) {
        String stringWithBracket = Arrays.toString(strings);
        return stringWithBracket.substring(1, stringWithBracket.length() - 1);
    }

    public static int update(String sql) {
        queryRunner = new QueryRunner(ConnectionFactory.getDataSource());
        try {
            return queryRunner.update(sql);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public static int update(String sql, Object... params) {
        queryRunner = new QueryRunner(ConnectionFactory.getDataSource());
        try {
            return queryRunner.update(sql, params);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    /**
     * 获取数据库驱动类名
     * @return
     */
    public static String getDriveName() {
        try {
            return ConnectionFactory.getConnection().getMetaData().getDriverName();
        } catch (SQLException e) {
            logger.error("Unable to get driveName!!!", e);
            return null;
        }
    }

    public static boolean isMysqlDB() {
        String driverName = getDriveName().toLowerCase();
        logger.debug("DriverName = " + driverName);
        return driverName.indexOf("mysql") > -1 ? true : false;
    }

    public static boolean isOracleBD() {
        String driverName = getDriveName().toLowerCase();;
        logger.debug("DriverName = " + driverName);
        return driverName.indexOf("oracle") > -1 ? true : false;
    }

    /**
     * 执行查询，将某一行的结果保存到Bean中，
     *
     * @param beanClass 类名
     * @param sql sql语句
     * @return 查询结果
     */
    public static <T> T findBean(Class<T> beanClass, String sql, Object... params) {
        queryRunner = new QueryRunner(ConnectionFactory.getDataSource());
        try {
            return (T) queryRunner.query(sql, isPrimitive(beanClass) ? gScaleHandler : new BeanHandler(beanClass), params);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public static <T> T findBean(Class<T> beanClass, String sql) {
        queryRunner = new QueryRunner(ConnectionFactory.getDataSource());
        try {
            return (T) queryRunner.query(sql, isPrimitive(beanClass) ? gScaleHandler : new BeanHandler(beanClass));
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    /**
     * 执行查询，将每行的结果保存到Bean中，然后将所有Bean保存到List中
     *
     * @param beanClass 类名
     * @param sql sql语句
     * @return 查询结果
     */
    public static <T> List<T> findList(Class<T> beanClass, String sql, Object... params) {
        queryRunner = new QueryRunner(ConnectionFactory.getDataSource());
        try {
            return (List<T>) queryRunner.query(sql, isPrimitive(beanClass) ? gColumnListHandler : new BeanListHandler(beanClass), params);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public static <T> List<T> findList(Class<T> beanClass, String sql) {
        queryRunner = new QueryRunner(ConnectionFactory.getDataSource());
        try {
            return (List<T>) queryRunner.query(sql, isPrimitive(beanClass) ? gColumnListHandler : new BeanListHandler(beanClass));
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public static List<Map<String, Object>> findList(String sql, Object... params) {
        queryRunner = new QueryRunner(ConnectionFactory.getDataSource());
        try {
            return queryRunner.query(sql, new MapListHandler(), params);
        }catch (SQLException e) {
            throw new DBException(e);
        }
    }

    public static <T> T query(String sql, ResultSetHandler<T> rsh, Object... params) {
        queryRunner = new QueryRunner(ConnectionFactory.getDataSource());
        try {
            return queryRunner.query(sql, rsh, params);
        } catch (SQLException e) {
            throw new DBException(e);
        }
    }

    private final static ColumnListHandler gColumnListHandler = new ColumnListHandler() {
        @Override
        protected Object handleRow(ResultSet rs) throws SQLException {
            Object obj = super.handleRow(rs);
            if (obj instanceof BigInteger)
                return ((BigInteger) obj).longValue();
            return obj;
        }
    };

    private final static ScalarHandler gScaleHandler = new ScalarHandler() {
        @Override
        public Object handle(ResultSet rs) throws SQLException {
            Object obj = super.handle(rs);
            if (obj instanceof BigInteger)
                return ((BigInteger) obj).longValue();
            return obj;
        }
    };

    private final static List<Class<?>> PrimitiveClasses = new ArrayList<Class<?>>() {
        {
            add(Long.class);
            add(Integer.class);
            add(String.class);
            add(Date.class);
            add(java.sql.Date.class);
            add(java.sql.Timestamp.class);
        }
    };

    private final static boolean isPrimitive(Class<?> cls) {
        return cls.isPrimitive() || PrimitiveClasses.contains(cls);
    }

}
