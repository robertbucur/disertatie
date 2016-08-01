package ro.ubblcuj.cs.collaborativetexteditor.model;

/**
 * Created by robytoxic on 24/07/2016.
 */
public class CTXEFileChange {
    private long datetime;
    private Integer id;
    private Integer fileId;
    private Integer fileVersionId;
    private Integer charPosition;
    private String charValue;
    private String author;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public Integer getFileVersionId() {
        return fileVersionId;
    }

    public void setFileVersionId(Integer fileVersionId) {
        this.fileVersionId = fileVersionId;
    }

    public long getDatetime() {
        return datetime;
    }

    public void setDatetime(long timestamp) {
        this.datetime = timestamp;
    }

    public Integer getCharPosition() {
        return charPosition;
    }

    public void setCharPosition(Integer charPosition) {
        this.charPosition = charPosition;
    }

    public String getCharValue() {
        return charValue;
    }

    public void setCharValue(String charValue) {
        this.charValue = charValue;
    }

}
