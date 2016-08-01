package ro.ubblcuj.cs.collaborativetexteditor.utils;

import ro.ubblcuj.cs.collaborativetexteditor.model.CTXEFileChange;

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
}
