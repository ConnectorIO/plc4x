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

package org.apache.plc4x.java.opcua.discovery;

import java.util.concurrent.CompletableFuture;
import org.apache.plc4x.java.api.messages.PlcDiscoveryItem;
import org.apache.plc4x.java.api.messages.PlcDiscoveryItemHandler;
import org.apache.plc4x.java.api.messages.PlcDiscoveryRequest.Builder;
import org.apache.plc4x.java.api.messages.PlcDiscoveryResponse;
import org.apache.plc4x.java.opcua.OpcuaPlcDriver;

class OpcuaDiscovererTest {

    public static void main(String[] args) throws Exception {
        Builder builder = new OpcuaPlcDriver().discoveryRequestBuilder();
        CompletableFuture<? extends PlcDiscoveryResponse> future = builder.addQuery("uri", "opcua:tcp://opcuademo.sterfive.com:26543")
            .build().executeWithHandler(new PlcDiscoveryItemHandler() {
                @Override
                public void handle(PlcDiscoveryItem discoveryItem) {
                    System.out.println(discoveryItem.getConnectionUrl());
                }
            });

        future.get();
    }

}