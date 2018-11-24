/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.apache.plc4x.java.scraper;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.pool2.KeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import org.apache.plc4x.java.PlcDriverManager;
import org.apache.plc4x.java.api.PlcConnection;
import org.apache.plc4x.java.api.exceptions.PlcConnectionException;
import org.apache.plc4x.java.api.types.PlcResponseCode;
import org.apache.plc4x.java.base.messages.items.DefaultIntegerFieldItem;
import org.apache.plc4x.java.mock.MockDevice;
import org.apache.plc4x.java.mock.PlcMockConnection;
import org.apache.plc4x.java.utils.connectionpool.PoolKey;
import org.apache.plc4x.java.utils.connectionpool.PooledPlcConnectionFactory;
import org.apache.plc4x.java.utils.connectionpool.PooledPlcDriverManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScraperTest {

    @Mock
    MockDevice mockDevice;

    public static final String CONN_STRING_TIM = "s7://10.10.64.22/0/1";
    public static final String FIELD_STRING_TIM = "%DB225:DBW0:INT";

    public static final String CONN_STRING_CH = "s7://10.10.64.20/0/1";
    public static final String FIELD_STRING_CH = "%DB3:DBD32:DINT";

    @Test
    void real_stuff() throws InterruptedException {
        PlcDriverManager driverManager = new PooledPlcDriverManager(pooledPlcConnectionFactory -> {
            GenericKeyedObjectPoolConfig<PlcConnection> config = new GenericKeyedObjectPoolConfig<>();
            config.setMaxWaitMillis(-1);
            config.setMaxTotal(3);
            config.setMinIdlePerKey(0);
            config.setBlockWhenExhausted(true);
            config.setTestOnBorrow(true);
            config.setTestOnReturn(true);
            return new GenericKeyedObjectPool<>(pooledPlcConnectionFactory, config);
        });

        Scraper scraper = new Scraper(driverManager, Arrays.asList(
            new Scraper.ScrapeJob("job1",
                10,
                Collections.singletonMap("tim", CONN_STRING_TIM),
                Collections.singletonMap("distance", FIELD_STRING_TIM)
            ),
            new Scraper.ScrapeJob("job2",
                10,
                Collections.singletonMap("chris", CONN_STRING_CH),
                Collections.singletonMap("counter", FIELD_STRING_CH)
            )
        ));

        Thread.sleep(300_000);
    }

    @Test
    void scraper_schedulesJob() throws InterruptedException, PlcConnectionException {
        PlcDriverManager driverManager = new PlcDriverManager();
        PlcMockConnection connection = (PlcMockConnection) driverManager.getConnection("mock:m1");
        connection.setDevice(mockDevice);

        when(mockDevice.read(any())).thenReturn(Pair.of(PlcResponseCode.OK, new DefaultIntegerFieldItem(1)));

        Scraper scraper = new Scraper(driverManager, Collections.singletonList(
            new Scraper.ScrapeJob("job1",
                10,
                Collections.singletonMap("m1", "mock:m1"),
                Collections.singletonMap("field1", "qry1")
            )
        ));

        Thread.sleep(5_000);
    }
}