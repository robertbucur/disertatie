package ro.ubblcuj.cs.collaborativetexteditor.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * Created by robytoxic on 24/07/2016.
 */
public class CTXEFileChange {
    private Integer id;
    private Integer fileId;
    private Integer fileVersionId;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss.SSS")
    private Date datetime;
    private Integer charColumn;
    private Integer charRow;
    private Integer charPosition;
    private String charValue;

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

    public Date getDatetime() {
        return datetime;
    }

    public void setDatetime(Date timestamp) {
        this.datetime = timestamp;
    }

    public Integer getCharColumn() {
        return charColumn;
    }

    public void setCharColumn(Integer charColumn) {
        this.charColumn = charColumn;
    }

    public Integer getCharRow() {
        return charRow;
    }

    public void setCharRow(Integer charRow) {
        this.charRow = charRow;
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
