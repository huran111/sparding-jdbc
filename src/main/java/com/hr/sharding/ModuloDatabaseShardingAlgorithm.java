package com.hr.sharding;

import com.dangdang.ddframe.rdb.sharding.api.ShardingValue;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.SingleKeyDatabaseShardingAlgorithm;
import com.google.common.collect.Range;

import java.util.Collection;
import java.util.LinkedHashSet;

/**
 * @author:HuRan
 * @Description: 分库
 * @Date: Created in 13:02 2018/6/22
 * @Modified By:
 */
public class ModuloDatabaseShardingAlgorithm implements SingleKeyDatabaseShardingAlgorithm<Long> {
    /**
     * select * from t_order from t_order where order_id = 11
     * └── SELECT *  FROM t_order_1 WHERE order_id = 11
     * select * from t_order from t_order where order_id = 44
     * └── SELECT *  FROM t_order_0 WHERE order_id = 44
     */
    /**
     *
     * @param availableTargetNames  所有的库名 （ds_0,ds_1）
     * @param shardingValue  user_id  如果是偶数就算到ds_0数据库否则放到ds_1数据库
     * @return
     */
    @Override
    public String doEqualSharding(Collection<String> availableTargetNames, ShardingValue<Long> shardingValue) {
        for (String each : availableTargetNames) {
            System.out.println("分库........");
            System.out.println("availableTargetNames:"+availableTargetNames.toString());
            System.out.println("shardingValue:"+shardingValue.getValue());
            System.out.println("ColumnName:"+shardingValue.getColumnName());
            System.out.println("Type:"+shardingValue.getType());            if (each.endsWith(shardingValue.getValue() % 2 + "")) {
                return each;
            }
        }
        throw new IllegalArgumentException();
    }

    /**
     * select * from t_order from t_order where order_id in (11,44)
     * ├── SELECT *  FROM t_order_0 WHERE order_id IN (11,44)
     * └── SELECT *  FROM t_order_1 WHERE order_id IN (11,44)
     * select * from t_order from t_order where order_id in (11,13,15)
     * └── SELECT *  FROM t_order_1 WHERE order_id IN (11,13,15)
     * select * from t_order from t_order where order_id in (22,24,26)
     * └──SELECT *  FROM t_order_0 WHERE order_id IN (22,24,26)
     */
    @Override
    public Collection<String> doInSharding(Collection<String> tableNames, ShardingValue<Long> shardingValue) {
        Collection<String> result = new LinkedHashSet<>(tableNames.size());
        for (Long value : shardingValue.getValues()) {
            for (String tableName : tableNames) {
                if (tableName.endsWith(value % 2 + "")) {
                    result.add(tableName);
                }
            }
        }
        return result;
    }

    /**
     * select * from t_order from t_order where order_id between 10 and 20
     * ├── SELECT *  FROM t_order_0 WHERE order_id BETWEEN 10 AND 20
     * └── SELECT *  FROM t_order_1 WHERE order_id BETWEEN 10 AND 20
     */
    @Override
    public Collection<String> doBetweenSharding(Collection<String> tableNames, ShardingValue<Long> shardingValue) {
        Collection<String> result = new LinkedHashSet<>(tableNames.size());
        Range<Long> range = shardingValue.getValueRange();
        for (Long i = range.lowerEndpoint(); i <= range.upperEndpoint(); i++) {
            for (String each : tableNames) {
                if (each.endsWith(i % 2 + "")) {
                    result.add(each);
                }
            }
        }
        return result;
    }
}
