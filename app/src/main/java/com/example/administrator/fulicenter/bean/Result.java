package com.example.administrator.fulicenter.bean;

/**
 * Created by Winston on 2016/10/13.
 */

public class Result {

    /**
     * muserName : a952702
     * muserNick : 彭鹏
     * mavatarId : 74
     * mavatarPath : user_avatar
     * mavatarSuffix : .jpg
     * mavatarType : 0
     * mavatarLastUpdateTime : 1476285149669
     */

    private String muserName;
    private String muserNick;
    private int mavatarId;
    private String mavatarPath;
    private String mavatarSuffix;
    private int mavatarType;
    private String mavatarLastUpdateTime;

    public String getMuserName() {
        return muserName;
    }

    public void setMuserName(String muserName) {
        this.muserName = muserName;
    }

    public String getMuserNick() {
        return muserNick;
    }

    public void setMuserNick(String muserNick) {
        this.muserNick = muserNick;
    }

    public int getMavatarId() {
        return mavatarId;
    }

    public void setMavatarId(int mavatarId) {
        this.mavatarId = mavatarId;
    }

    public String getMavatarPath() {
        return mavatarPath;
    }

    public void setMavatarPath(String mavatarPath) {
        this.mavatarPath = mavatarPath;
    }

    public String getMavatarSuffix() {
        return mavatarSuffix;
    }

    public void setMavatarSuffix(String mavatarSuffix) {
        this.mavatarSuffix = mavatarSuffix;
    }

    public int getMavatarType() {
        return mavatarType;
    }

    public void setMavatarType(int mavatarType) {
        this.mavatarType = mavatarType;
    }

    public String getMavatarLastUpdateTime() {
        return mavatarLastUpdateTime;
    }

    public void setMavatarLastUpdateTime(String mavatarLastUpdateTime) {
        this.mavatarLastUpdateTime = mavatarLastUpdateTime;
    }

    public Result() {
    }

    @Override
    public String toString() {
        return "Result{" +
                "muserName='" + muserName + '\'' +
                ", muserNick='" + muserNick + '\'' +
                ", mavatarId=" + mavatarId +
                ", mavatarPath='" + mavatarPath + '\'' +
                ", mavatarSuffix='" + mavatarSuffix + '\'' +
                ", mavatarType=" + mavatarType +
                ", mavatarLastUpdateTime='" + mavatarLastUpdateTime + '\'' +
                '}';
    }
}
