/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.plc4x.java.opcua.config;

import java.security.cert.X509Certificate;
import java.time.Duration;
import org.apache.plc4x.java.opcua.context.SecureChannel;
import org.apache.plc4x.java.opcua.readwrite.PascalByteString;
import org.apache.plc4x.java.opcua.security.MessageSecurity;
import org.apache.plc4x.java.opcua.security.SecurityPolicy;
import org.apache.plc4x.java.spi.configuration.Configuration;
import org.apache.plc4x.java.spi.configuration.annotations.ComplexConfigurationParameter;
import org.apache.plc4x.java.spi.configuration.annotations.ConfigurationParameter;
import org.apache.plc4x.java.spi.configuration.annotations.defaults.BooleanDefaultValue;

public class OpcuaConfiguration implements Configuration {

    public static final long DEFAULT_CONNECTION_LIFETIME = 36000000;

    public static final long DEFAULT_REQUEST_TIMEOUT = 5 * 60 * 1000L;

    public static final long DEFAULT_SESSION_TIMEOUT = 120000L;

    @ConfigurationParameter("protocolCode")
    private String protocolCode;

    @ConfigurationParameter("transportCode")
    private String transportCode;

    @ConfigurationParameter("transportConfig")
    private String transportConfig;

    @ConfigurationParameter("discovery")
    @BooleanDefaultValue(true)
    private boolean discovery;

    @ConfigurationParameter("username")
    private String username;

    @ConfigurationParameter("password")
    private String password;

    @ConfigurationParameter("securityPolicy")
    private SecurityPolicy securityPolicy = SecurityPolicy.NONE;

    @ConfigurationParameter("messageSecurity")
    private MessageSecurity messageSecurity = MessageSecurity.SIGN_ENCRYPT;

    @ConfigurationParameter("keyStoreFile")
    private String keyStoreFile;

    @ConfigurationParameter("certDirectory")
    private String certDirectory;

    @ConfigurationParameter("keyStorePassword")
    private String keyStorePassword;
    private X509Certificate serverCertificate;

    @ConfigurationParameter("channelLifetime")
    private long channelLifetime = DEFAULT_CONNECTION_LIFETIME;

    @ConfigurationParameter("requestTimeout")
    private long requestTimeout = DEFAULT_REQUEST_TIMEOUT;

    @ConfigurationParameter("sessionTimeout")
    private long sessionTimeout = DEFAULT_SESSION_TIMEOUT;

    @ConfigurationParameter("openChannelTimeout")
    private long openChannelTimeout = DEFAULT_REQUEST_TIMEOUT;

    @ComplexConfigurationParameter(prefix = "encoding", defaultOverrides = {}, requiredOverrides = {})
    private Limits limits = new Limits();

    public String getProtocolCode() {
        return protocolCode;
    }

    public String getTransportCode() {
        return transportCode;
    }

    public String getTransportConfig() {
        return transportConfig;
    }

    public boolean isDiscovery() {
        return discovery;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getCertDirectory() {
        return certDirectory;
    }

    public SecurityPolicy getSecurityPolicy() {
        return securityPolicy;
    }

    public MessageSecurity getMessageSecurity() {
        return messageSecurity;
    }

    public String getKeyStoreFile() {
        return keyStoreFile;
    }

    public String getKeyStorePassword() {
        return keyStorePassword;
    }

    public Limits getEncodingLimits() {
        return limits;
    }

    public X509Certificate getServerCertificate() {
        return serverCertificate;
    }

    public void setServerCertificate(X509Certificate serverCertificate) {
        this.serverCertificate = serverCertificate;
    }

    public long getChannelLifetime() {
        return channelLifetime;
    }

    public long getRequestTimeout() {
        return requestTimeout;
    }

    public long getOpenChannelTimeout() {
        return openChannelTimeout;
    }

    @Override
    public String toString() {
        return "OpcuaConfiguration{" +
            "discovery=" + discovery +
            ", username='" + username + '\'' +
            ", password='" + (password != null ? "******" : null) + '\'' +
            ", securityPolicy='" + securityPolicy + '\'' +
            ", keyStoreFile='" + keyStoreFile + '\'' +
            ", certDirectory='" + certDirectory + '\'' +
            ", keyStorePassword='" + (keyStorePassword != null ? "******" : null) + '\'' +
            ", limits=" + limits +
            '}';
    }
}

