package com.example.chen.tset.Data;

/**
 * Created by Administrator on 2016/10/25 0025.
 */
public class VersionsUpdate {
    private String downloadPath;
    private String updateLog;
    private String version;

    public VersionsUpdate(String downloadPath, String updateLog, String version) {
        this.downloadPath = downloadPath;
        this.updateLog = updateLog;
        this.version = version;
    }

    public String getDownloadPath() {
        return downloadPath;
    }

    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }

    public String getUpdateLog() {
        return updateLog;
    }

    public void setUpdateLog(String updateLog) {
        this.updateLog = updateLog;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return "VersionsUpdate{" +
                "downloadPath='" + downloadPath + '\'' +
                ", updateLog='" + updateLog + '\'' +
                ", version='" + version + '\'' +
                '}';
    }
}
