package com.example.joboishi.Api;

import com.google.gson.annotations.SerializedName;

public class CountryApiResponse {
    @SerializedName("name")
    private NameData name;
    @SerializedName("region")
    private String region;
    @SerializedName("status")

    private String status;

    public CountryApiResponse(NameData name, String region, String status) {
        this.name = name;
        this.region = region;
        this.status = status;
    }

    public NameData getName() {
        return name;
    }

    public void setName(NameData name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public static class NameData {

        @SerializedName("common")
        private String common;
        @SerializedName("official")
        private String official;

        public NameData(String common, String official) {
            this.common = common;
            this.official = official;
        }

        public String getCommon() {
            return common;
        }

        public void setCommon(String common) {
            this.common = common;
        }

        public String getOfficial() {
            return official;
        }

        public void setOfficial(String official) {
            this.official = official;
        }
    }
}
