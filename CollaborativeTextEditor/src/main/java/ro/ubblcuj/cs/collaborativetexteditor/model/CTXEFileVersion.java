package ro.ubblcuj.cs.collaborativetexteditor.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * Created by robytoxic on 12/07/2016.
 */
public class CTXEFileVersion {
    private Integer id;
    private Integer fileId;
    private Integer versionNumber;
    private long creationDate;
    private String author;
    private String fileNameComposed;

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

    public Integer getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(Integer versionNumber) {
        this.versionNumber = versionNumber;
    }

    public long getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(long creationDate) {
        this.creationDate = creationDate;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getFileNameComposed() {
        return fileNameComposed;
    }

    public void setFileNameComposed(String fileNameComposed) {
        this.fileNameComposed = fileNameComposed;
    }
}
