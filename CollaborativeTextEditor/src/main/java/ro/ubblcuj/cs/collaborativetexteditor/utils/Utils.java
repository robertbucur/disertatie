package ro.ubblcuj.cs.collaborativetexteditor.utils;

import ro.ubblcuj.cs.collaborativetexteditor.model.CTXEFile;
import ro.ubblcuj.cs.collaborativetexteditor.model.CTXEFileChange;
import ro.ubblcuj.cs.collaborativetexteditor.model.CTXEFileVersion;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.List;

public class Utils {

    // Under jetty folder
    public static final String SERVER_UPLOAD_LOCATION_FOLDER = "../CTEFiles/";

    public static CTXEFileChange applyOperationalTransformationOnChange(List<CTXEFileChange> changes, CTXEFileChange change) {
        for(CTXEFileChange fileChange: changes) {
            boolean isBackspace = "\b".equals(fileChange.getCharValue());
            if(!isBackspace && fileChange.getCharPosition() <= change.getCharPosition()) {
                change.setCharPosition(change.getCharPosition()+1);
            } else if (isBackspace && fileChange.getCharPosition() <= change.getCharPosition()) {
                change.setCharPosition(change.getCharPosition()-1);
            }
        }
        return null;
    }

    public static CTXEFileVersion getFileVersionFromFile(CTXEFile file) {
        String uniqueIdentifier = Utils.getUniqueIdentifier().substring(0,9);
        CTXEFileVersion fileVersion = new CTXEFileVersion();
        fileVersion.setFileId(file.getId());
        fileVersion.setCreationDate(file.getLastModified());
        fileVersion.setAuthor(file.getLastEditor());
        fileVersion.setVersionNumber(0);
        fileVersion.setFileNameComposed(file.getName());
        fileVersion.setIdentifier(uniqueIdentifier);

        return fileVersion;
    }

    public static String getUniqueIdentifier() {
        SecureRandom random = new SecureRandom();

        return new BigInteger(130, random).toString(32);
    }
}
