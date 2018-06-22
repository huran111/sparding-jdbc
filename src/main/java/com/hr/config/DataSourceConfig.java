package com.hr.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.dangdang.ddframe.rdb.sharding.api.ShardingDataSourceFactory;
import com.dangdang.ddframe.rdb.sharding.api.rule.DataSourceRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.ShardingRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.TableRule;
import com.dangdang.ddframe.rdb.sharding.api.strategy.database.DatabaseShardingStrategy;
import com.dangdang.ddframe.rdb.sharding.id.generator.IdGenerator;
import com.dangdang.ddframe.rdb.sharding.id.generator.self.CommonSelfIdGenerator;
import com.hr.sharding.ModuloDatabaseShardingAlgorithm;
//import com.hr.sharding.ModuloTableShardingAlgorithm;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

import com.mysql.jdbc.Driver;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author:HuRan
 * @Description:
 * @Date: Created in 11:43 2018/6/22
 * @Modified By:
 */
@Configuration
public class DataSourceConfig {
    @Bean
    public IdGenerator getIdGenerator() {
        return new CommonSelfIdGenerator();

    }

    @Bean
    public DataSource getDataSource() {
        return buildDataSource();
    }

    private DataSource buildDataSource() {
        //设置分库映射
        Map<String, DataSource> dataSourceMap = new HashMap<>(2);
        //添加两个数据库 ds_0,ds_1到map里面
        dataSourceMap.put("db_0", createDateSource("db_0"));
        dataSourceMap.put("db_1", createDateSource("db_1"));
        //默认db为ds_0,也就是为哪些没有配置分库分表策略指定的默认库
        DataSourceRule dataSourceRule = new DataSourceRule(dataSourceMap, "db_0");
        //设置分表映射，将t_order_0和t_order_1两个实际不同的表映射到t_order逻辑表
        TableRule tableRule = TableRule.builder("t_order")
                .actualTables(Arrays.asList("t_order_0"))
                .dataSourceRule(dataSourceRule).build();
        //具体分表策略，按什么规则来分
        ShardingRule shardingRule = ShardingRule.builder()
                .dataSourceRule(dataSourceRule)
                .tableRules(Arrays.asList(tableRule))
                .databaseShardingStrategy(new DatabaseShardingStrategy("user_id", new ModuloDatabaseShardingAlgorithm()))
 /*               .tableShardingStrategy(new TableShardingStrategy("order_id", new ModuloTableShardingAlgorithm()))*/.build();

        DataSource dataSource = ShardingDataSourceFactory.createDataSource(shardingRule);
        return dataSource;
    }

    private static DataSource createDateSource(final String dataSourceName) {
        //使用druidL连接数据库
        DruidDataSource result = new DruidDataSource();
        result.setDriverClassName(Driver.class.getName());
        result.setUrl(String.format("jdbc:mysql://182.254.228.51:3306/%s", dataSourceName));
        result.setUsername("root");
        result.setPassword("root");
        return result;
    }
}
