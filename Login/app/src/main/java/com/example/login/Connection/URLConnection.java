//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.example.login.Connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ContentHandlerFactory;
import java.net.FileNameMap;
import java.net.URL;
import java.security.Permission;
import java.util.List;
import java.util.Map;

public abstract class URLConnection {
    protected boolean allowUserInteraction;
    protected boolean connected = false;
    protected boolean doInput = true;
    protected boolean doOutput = false;
    protected long ifModifiedSince = 0L;
    protected URL url;
    protected boolean useCaches;

    protected URLConnection(URL url) {
        throw new RuntimeException("Stub!");
    }

    public static synchronized FileNameMap getFileNameMap() {
        throw new RuntimeException("Stub!");
    }

    public static void setFileNameMap(FileNameMap map) {
        throw new RuntimeException("Stub!");
    }

    public abstract void connect() throws IOException;

    public void setConnectTimeout(int timeout) {
        throw new RuntimeException("Stub!");
    }

    public int getConnectTimeout() {
        throw new RuntimeException("Stub!");
    }

    public void setReadTimeout(int timeout) {
        throw new RuntimeException("Stub!");
    }

    public int getReadTimeout() {
        throw new RuntimeException("Stub!");
    }

    public URL getURL() {
        throw new RuntimeException("Stub!");
    }

    public int getContentLength() {
        throw new RuntimeException("Stub!");
    }

    public long getContentLengthLong() {
        throw new RuntimeException("Stub!");
    }

    public String getContentType() {
        throw new RuntimeException("Stub!");
    }

    public String getContentEncoding() {
        throw new RuntimeException("Stub!");
    }

    public long getExpiration() {
        throw new RuntimeException("Stub!");
    }

    public long getDate() {
        throw new RuntimeException("Stub!");
    }

    public long getLastModified() {
        throw new RuntimeException("Stub!");
    }

    public String getHeaderField(String name) {
        throw new RuntimeException("Stub!");
    }

    public Map<String, List<String>> getHeaderFields() {
        throw new RuntimeException("Stub!");
    }

    public int getHeaderFieldInt(String name, int Default) {
        throw new RuntimeException("Stub!");
    }

    public long getHeaderFieldLong(String name, long Default) {
        throw new RuntimeException("Stub!");
    }

    public long getHeaderFieldDate(String name, long Default) {
        throw new RuntimeException("Stub!");
    }

    public String getHeaderFieldKey(int n) {
        throw new RuntimeException("Stub!");
    }

    public String getHeaderField(int n) {
        throw new RuntimeException("Stub!");
    }

    public Object getContent() throws IOException {
        throw new RuntimeException("Stub!");
    }

    public Object getContent(Class[] classes) throws IOException {
        throw new RuntimeException("Stub!");
    }

    public Permission getPermission() throws IOException {
        throw new RuntimeException("Stub!");
    }

    public InputStream getInputStream() throws IOException {
        throw new RuntimeException("Stub!");
    }

    public OutputStream getOutputStream() throws IOException {
        throw new RuntimeException("Stub!");
    }

    public String toString() {
        throw new RuntimeException("Stub!");
    }

    public void setDoInput(boolean doinput) {
        throw new RuntimeException("Stub!");
    }

    public boolean getDoInput() {
        throw new RuntimeException("Stub!");
    }

    public void setDoOutput(boolean dooutput) {
        throw new RuntimeException("Stub!");
    }

    public boolean getDoOutput() {
        throw new RuntimeException("Stub!");
    }

    public void setAllowUserInteraction(boolean allowuserinteraction) {
        throw new RuntimeException("Stub!");
    }

    public boolean getAllowUserInteraction() {
        throw new RuntimeException("Stub!");
    }

    public static void setDefaultAllowUserInteraction(boolean defaultallowuserinteraction) {
        throw new RuntimeException("Stub!");
    }

    public static boolean getDefaultAllowUserInteraction() {
        throw new RuntimeException("Stub!");
    }

    public void setUseCaches(boolean usecaches) {
        throw new RuntimeException("Stub!");
    }

    public boolean getUseCaches() {
        throw new RuntimeException("Stub!");
    }

    public void setIfModifiedSince(long ifmodifiedsince) {
        throw new RuntimeException("Stub!");
    }

    public long getIfModifiedSince() {
        throw new RuntimeException("Stub!");
    }

    public boolean getDefaultUseCaches() {
        throw new RuntimeException("Stub!");
    }

    public void setDefaultUseCaches(boolean defaultusecaches) {
        throw new RuntimeException("Stub!");
    }

    public void setRequestProperty(String key, String value) {
        throw new RuntimeException("Stub!");
    }

    public void addRequestProperty(String key, String value) {
        throw new RuntimeException("Stub!");
    }

    public String getRequestProperty(String key) {
        throw new RuntimeException("Stub!");
    }

    public Map<String, List<String>> getRequestProperties() {
        throw new RuntimeException("Stub!");
    }

    /** @deprecated */
    @Deprecated
    public static void setDefaultRequestProperty(String key, String value) {
        throw new RuntimeException("Stub!");
    }

    /** @deprecated */
    @Deprecated
    public static String getDefaultRequestProperty(String key) {
        throw new RuntimeException("Stub!");
    }

    public static synchronized void setContentHandlerFactory(ContentHandlerFactory fac) {
        throw new RuntimeException("Stub!");
    }

    public static String guessContentTypeFromName(String fname) {
        throw new RuntimeException("Stub!");
    }

    public static String guessContentTypeFromStream(InputStream is) throws IOException {
        throw new RuntimeException("Stub!");
    }
}
