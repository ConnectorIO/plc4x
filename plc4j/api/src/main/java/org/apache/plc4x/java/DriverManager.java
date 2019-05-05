package org.apache.plc4x.java;

import org.apache.plc4x.java.api.PlcConnection;
import org.apache.plc4x.java.api.authentication.PlcAuthentication;
import org.apache.plc4x.java.api.exceptions.PlcConnectionException;

public interface DriverManager {

    PlcConnection getConnection(String url) throws PlcConnectionException;
    PlcConnection getConnection(String url, PlcAuthentication authentication) throws PlcConnectionException;

}
