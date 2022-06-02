package com.as.exer;

import com.as.jdbcutils.JDBCUtils;
import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * @ClassName BatchTest
 * @Description TODO
 * @Author xpower
 * @Date 2022/6/2 9:12
 * @Version 1.0
 */
public class BatchTest {
    @Test
    public void testInsertMore() {
        //4 基础上优化 最后提交数据
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        long start = System.currentTimeMillis();
        try {
            connection = JDBCUtils.getConnection();
            String sql = "insert into goods(name) values(?)";
            preparedStatement = connection.prepareStatement(sql);
            connection.setAutoCommit(false);
            for (int i = 1; i <= 100000000; i++) {
                preparedStatement.setObject(1, "name_" + i);

                preparedStatement.addBatch();

                if (i % 500 == 0) {
                    preparedStatement.executeBatch();
                    preparedStatement.clearBatch();
                }
            }
            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            long end = System.currentTimeMillis();
            System.out.println(end - start);
            JDBCUtils.closeResource(connection, preparedStatement);
        }
    }
}
