package com.wallethub.parser.model;

import java.util.Date;

public class Log {

    private String now;
    private String ip;
    private String request;
    private int status;
    private String user;

    public Log() {
    }

    public Log(String now, String ip, String request, int status, String user) {
        this.now = now;
        this.ip = ip;
        this.request = request;
        this.status = status;
        this.user = user;
    }

    public String getNow() {
        return now;
    }

    public void setNow(String now) {
        this.now = now;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Log{" +
                "now=" + now +
                ", ip='" + ip + '\'' +
                ", request='" + request + '\'' +
                ", status=" + status +
                ", user='" + user + '\'' +
                '}';
    }
}