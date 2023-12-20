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

import static org.apache.plc4x.java.opcua.context.SecureChannel.getX509Certificates;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.KeyStore;
import java.security.cert.X509Certificate;
import java.util.Objects;
import org.apache.plc4x.java.opcua.security.MessageSecurity;
import org.apache.plc4x.java.opcua.security.SecurityPolicy;
import org.apache.plc4x.java.spi.configuration.Configuration;
import org.apache.plc4x.java.spi.configuration.annotations.ComplexConfigurationParameter;
import org.apache.plc4x.java.spi.configuration.annotations.ConfigurationParameter;
import org.apache.plc4x.java.spi.configuration.annotations.defaults.BooleanDefaultValue;

public class OpcuaConfiguration implements Configuration {

    public static final long DEFAULT_CHANNEL_LIFETIME = 3600000;

    public static final long DEFAULT_SESSION_TIMEOUT = 120000;
    public static final long DEFAULT_NEGOTIATION_TIMEOUT = 60000;

    public static final long DEFAULT_REQUEST_TIMEOUT = 30000;

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

    @ConfigurationParameter("keyStoreType")
    private String keyStoreType = KeyStore.getDefaultType();

    @ConfigurationParameter("keyStorePassword")
    private String keyStorePassword;

    @ConfigurationParameter("trustStoreFile")
    private String trustStoreFile;

    @ConfigurationParameter("trustStoreType")
    private String trustStoreType = KeyStore.getDefaultType();

    @ConfigurationParameter("trustStorePassword")
    private String trustStorePassword;

    @ConfigurationParameter("serverCertificateFile")
    private String serverCertificateFile;

    private X509Certificate serverCertificate;

    @ConfigurationParameter("channelLifetime")
    private long channelLifetime = DEFAULT_CHANNEL_LIFETIME;

    @ConfigurationParameter("sessionTimeout")
    private long sessionTimeout = DEFAULT_SESSION_TIMEOUT;

    @ConfigurationParameter("negotiationTimeout")
    private long negotiationTimeout = DEFAULT_NEGOTIATION_TIMEOUT;

    @ConfigurationParameter("requestTimeout")
    private long requestTimeout = DEFAULT_REQUEST_TIMEOUT;

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

    public SecurityPolicy getSecurityPolicy() {
        return securityPolicy;
    }

    public MessageSecurity getMessageSecurity() {
        return messageSecurity;
    }

    public String getKeyStoreFile() {
        return keyStoreFile;
    }

    public String getKeyStoreType() {
        return keyStoreType;
    }

    public char[] getKeyStorePassword() {
        return keyStorePassword == null ? null : keyStorePassword.toCharArray();
    }

    public String getTrustStoreFile() {
        return trustStoreFile;
    }

    public String getTrustStoreType() {
        return trustStoreType;
    }

    public char[] getTrustStorePassword() {
        return trustStorePassword == null ? null : trustStorePassword.toCharArray();
    }

    public Limits getEncodingLimits() {
        return limits;
    }

    public X509Certificate getServerCertificate() {
        if (serverCertificate == null && serverCertificateFile != null) {
          try {
              byte[] certificate = Files.readAllBytes(new File(serverCertificateFile).toPath());
              serverCertificate = Objects.requireNonNull(getX509Certificates(certificate))[0];
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        }
        return serverCertificate;
    }

    public void setServerCertificate(X509Certificate serverCertificate) {
        this.serverCertificate = serverCertificate;
    }

    public long getChannelLifetime() {
        return channelLifetime;
    }

    public long getSessionTimeout() {
        return sessionTimeout;
    }

    public long getRequestTimeout() {
        return requestTimeout;
    }

    public long getNegotiationTimeout() {
        return negotiationTimeout;
    }

    @Override
    public String toString() {
        return "OpcuaConfiguration" +
            "{ protocolCode='" + protocolCode + '\'' +
            ", transportCode='" + transportCode + '\'' +
            ", transportConfig='" + transportConfig + '\'' +
            ", discovery=" + discovery +
            ", username='" + username + '\'' +
            ", password='" + password + '\'' +
            ", securityPolicy=" + securityPolicy +
            ", messageSecurity=" + messageSecurity +
            ", keyStoreFile='" + keyStoreFile + '\'' +
            ", keyStoreType='" + keyStoreType + '\'' +
            ", keyStorePassword='" + keyStorePassword + '\'' +
            ", trustStoreFile='" + trustStoreFile + '\'' +
            ", trustStoreType='" + trustStoreType + '\'' +
            ", trustStorePassword='" + trustStorePassword + '\'' +
            ", serverCertificateFile='" + serverCertificateFile + '\'' +
            ", serverCertificate=" + serverCertificate +
            ", channelLifetime=" + channelLifetime +
            ", sessionTimeout=" + sessionTimeout +
            ", negotiationTimeout=" + negotiationTimeout +
            ", requestTimeout=" + requestTimeout +
            ", limits=" + limits +
            ", encodingLimits=" + getEncodingLimits() +
            '}';
    }
}

