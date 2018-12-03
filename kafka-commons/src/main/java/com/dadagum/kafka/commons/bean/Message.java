package com.dadagum.kafka.commons.bean;

public class Message {

    private Integer id;

    private String uuid;

    private Double value;

    private String runAt;

    private Boolean original;

    private Boolean anomaly;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getRunAt() {
        return runAt;
    }

    public void setRunAt(String runAt) {
        this.runAt = runAt;
    }

    public Boolean getOriginal() {
        return original;
    }

    public void setOriginal(Boolean original) {
        this.original = original;
    }

    public Boolean getAnomaly() {
        return anomaly;
    }

    public void setAnomaly(Boolean anomaly) {
        this.anomaly = anomaly;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", value=" + value +
                ", runAt='" + runAt + '\'' +
                ", original=" + original +
                ", anomaly=" + anomaly +
                '}';
    }
}
