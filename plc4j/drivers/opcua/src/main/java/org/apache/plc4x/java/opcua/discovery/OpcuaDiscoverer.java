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

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import org.apache.plc4x.java.api.PlcConnection;
import org.apache.plc4x.java.api.messages.PlcDiscoveryItemHandler;
import org.apache.plc4x.java.api.messages.PlcDiscoveryRequest;
import org.apache.plc4x.java.api.messages.PlcDiscoveryResponse;
import org.apache.plc4x.java.api.metadata.OptionMetadata;
import org.apache.plc4x.java.opcua.OpcuaPlcDriver;
import org.apache.plc4x.java.spi.messages.DefaultPlcDiscoveryRequest;
import org.apache.plc4x.java.spi.messages.PlcDiscoverer;

public class OpcuaDiscoverer implements PlcDiscoverer {

    @Override
    public CompletableFuture<PlcDiscoveryResponse> discover(PlcDiscoveryRequest discoveryRequest) {
        return discoverWithHandler(discoveryRequest, result -> {});
    }

    @Override
    public CompletableFuture<PlcDiscoveryResponse> discoverWithHandler(PlcDiscoveryRequest discoveryRequest, PlcDiscoveryItemHandler handler) {
        if (!(discoveryRequest instanceof DefaultPlcDiscoveryRequest)) {
            return CompletableFuture.failedFuture(new UnsupportedOperationException());
        }

        DefaultPlcDiscoveryRequest request = (DefaultPlcDiscoveryRequest) discoveryRequest;
        CompletableFuture<PlcDiscoveryResponse> future = new CompletableFuture<>();
        try {
            OpcuaDiscoveryDriver driver = new OpcuaDiscoveryDriver();
            PlcConnection uri = driver.getConnection(request.getQueries().get("uri"));

            Optional<OptionMetadata> optionMetadata = new OpcuaPlcDriver().getMetadata()
                .getProtocolConfigurationOptionMetadata();
            optionMetadata.ifPresent(m -> {
                m.getOptions().stream().forEach(opt -> {
                    System.out.println(opt.getKey() + " " + opt.getType());
                });
            });
        } catch (Exception e) {
            future.completeExceptionally(e);
        }
        return future;
    }
}
