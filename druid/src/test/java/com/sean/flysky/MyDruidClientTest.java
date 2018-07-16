package com.sean.flysky;

import com.fasterxml.jackson.databind.ObjectMapper;
import in.zapr.druid.druidry.Interval;
import in.zapr.druid.druidry.aggregator.CountAggregator;
import in.zapr.druid.druidry.aggregator.DruidAggregator;
import in.zapr.druid.druidry.dimension.DruidDimension;
import in.zapr.druid.druidry.dimension.SimpleDimension;
import in.zapr.druid.druidry.granularity.Granularity;
import in.zapr.druid.druidry.granularity.PredefinedGranularity;
import in.zapr.druid.druidry.granularity.SimpleGranularity;
import in.zapr.druid.druidry.query.aggregation.DruidTopNQuery;
import in.zapr.druid.druidry.topNMetric.SimpleMetric;
import in.zapr.druid.druidry.topNMetric.TopNMetric;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

public class MyDruidClientTest {
    private static Logger logger = LoggerFactory.getLogger(MyDruidClientTest.class);

    private MyDruidClient myDruidClient;
    @Before
    public void setUp() throws Exception {
        myDruidClient = new MyDruidClient("monitor4.cs.os.kkws.cn", 8082);
    }

    @Test
    public void query() throws Exception {
        DateTime startTime = new DateTime(2018, 6, 28, 0, 0, 0, DateTimeZone.UTC);
        DateTime endTime = new DateTime(2018, 6, 29, 0, 0, 1, DateTimeZone.UTC);
        Interval interval = new Interval(startTime, endTime);

        Granularity granularity = new SimpleGranularity(PredefinedGranularity.ALL);

        DruidAggregator aggregator1 = new CountAggregator("count");
        DruidDimension dimension = new SimpleDimension("tran_name");
        TopNMetric metric = new SimpleMetric("count");

        DruidTopNQuery query = DruidTopNQuery.builder()
                .dataSource("dataset-trans")
                .dimension(dimension)
                .threshold(10)
                .topNMetric(metric)
                .granularity(granularity)
                .aggregators(Collections.singletonList(aggregator1))
                .intervals(Collections.singletonList(interval))
                .build();

        ObjectMapper mapper = new ObjectMapper();
        String requiredJson = mapper.writeValueAsString(query);
        logger.debug("Json query: {}", requiredJson);

        String result = myDruidClient.query(query);
        logger.info("Query result: {}", result);
    }

}