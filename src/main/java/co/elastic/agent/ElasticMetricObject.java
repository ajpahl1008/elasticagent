package co.elastic.agent;

public class ElasticMetricObject {
    private String measuredClass;
    private long duration;

    public String getMeasuredClass() {
        return measuredClass;
    }

    public void setMeasuredClass(String measuredClass) {
        this.measuredClass = measuredClass;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

}
