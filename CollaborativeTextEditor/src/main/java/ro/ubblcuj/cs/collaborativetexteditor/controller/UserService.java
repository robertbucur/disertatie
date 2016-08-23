package ro.ubblcuj.cs.collaborativetexteditor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import ro.ubblcuj.cs.collaborativetexteditor.model.CTXEFile;
import ro.ubblcuj.cs.collaborativetexteditor.model.CTXEFileChange;
import ro.ubblcuj.cs.collaborativetexteditor.model.CTXEFileVersion;
import ro.ubblcuj.cs.collaborativetexteditor.persistence.PersistenceUtils;
import ro.ubblcuj.cs.collaborativetexteditor.utils.FileService;
import ro.ubblcuj.cs.collaborativetexteditor.utils.Utils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Path("/service")
public class UserService {

    @POST
    @Path("/saveVersion")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response saveFileVersion(@FormParam("fileIdVersioning") Integer fileId,
                                    @FormParam("fileNameVersioning") String fileName,
                                    @FormParam("author") String author,
                                    @FormParam("fileContent") String fileContent) throws IOException {

        if(author == null) { author = "";}
        String fullFileName;
        int nextVersion = PersistenceUtils.getNextVersionNumber(fileId);
        String uniqueIdentifier = Utils.getUniqueIdentifier().substring(0,9);
        int p = fileName.lastIndexOf(".");
        if (p >= 0){
            fullFileName = Utils.SERVER_UPLOAD_LOCATION_FOLDER + fileName.substring(0, p) + nextVersion + fileName.substring(p);
        } else {
            fullFileName = Utils.SERVER_UPLOAD_LOCATION_FOLDER + fileName + nextVersion;
        }
        File file = new java.io.File(fullFileName);

        if (file.createNewFile()) {
            System.out.println("CTXEFileVersion is created!");
            FileOutputStream out = new FileOutputStream(file);
            out.write(fileContent.getBytes());
            out.close();
        } else {
            System.out.println("CTXEFileVersion already exists.");
        }

        CTXEFileVersion fileVersion = new CTXEFileVersion();
        fileVersion.setFileNameComposed(file.getName());
        fileVersion.setVersionNumber(nextVersion);
        fileVersion.setAuthor(author);
        fileVersion.setCreationDate(new Date().getTime());
        fileVersion.setFileId(fileId);
        fileVersion.setIdentifier(uniqueIdentifier);

        PersistenceUtils.saveFileVersionToDb(fileVersion);

        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(fileVersion);

        return getResponse(jsonInString);
    }

    @POST
    @Path("/change")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response addChange(@FormParam("fileId") Integer fileId,
                              @FormParam("fileVersionId") Integer fileVersionId,
                              @FormParam("charColumn") Integer charColumn,
                              @FormParam("charRow") Integer charRow,
                              @FormParam("charPosition") Integer charPosition,
                              @FormParam("charValue") String charValue,
                              @FormParam("author") String author,
                              @FormParam("lastUpdate") long lastUpdate) throws ParseException {

        Date now = new Date();
        CTXEFileChange fileChange = new CTXEFileChange();
        fileChange.setFileId(fileId);
        fileChange.setFileVersionId(fileVersionId);
        fileChange.setDatetime(now.getTime());
        fileChange.setCharPosition(charPosition);
        fileChange.setCharValue(charValue);
        fileChange.setAuthor(author);

        List<CTXEFileChange> fileChanges = PersistenceUtils.getAllChangesForFile(fileId, fileVersionId, lastUpdate, author);

        Utils.applyOperationalTransformationOnChange(fileChanges, fileChange);

        PersistenceUtils.insertFileChange(fileChange);

        return getResponse(null);
    }

    @POST
    @Path("/allChanges")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllFileChanges(@FormParam("fileId") Integer fileId,
                                      @FormParam("fileVersionId") Integer fileVersionId,
                                      @FormParam("lastUpdate") long lastUpdate,
                                      @FormParam("author") String author) throws IOException, ParseException {
        if (fileVersionId == null) {
            return getResponse(null);
        } else {
            List<CTXEFileChange> changes = PersistenceUtils.getAllChangesForFile(fileId, fileVersionId, lastUpdate, author);

            ObjectMapper mapper = new ObjectMapper();
            String jsonInString = mapper.writeValueAsString(changes);
            jsonInString = "{\"changes\":" + jsonInString + "}";

            return getResponse(jsonInString);
        }
    }

    // FUNCTIONAL
    @GET
    @Path("/allFiles")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllFiles() throws IOException {
        List<CTXEFile> files = PersistenceUtils.getAllFiles();

        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(files);

        return getResponse(jsonInString);
    }

    @POST
    @Path("/updateAllFiles")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUpdateAllFiles(@FormParam("lastUpdate") long lastUpdate) throws IOException {
        List<CTXEFile> files = PersistenceUtils.getUpdateAllFiles(lastUpdate);

        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(files);

        return getResponse(jsonInString);
    }

    @POST
    @Path("/updateAllFileVersions")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUpdateAllFileVersions(@FormParam("lastUpdate") long lastUpdate,
                                             @FormParam("fileId") Integer fileId) throws IOException {
        List<CTXEFileVersion> fileVersions = PersistenceUtils.getUpdateAllVersionsForFile(lastUpdate, fileId);

        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(fileVersions);

        return getResponse(jsonInString);
    }

    @GET
    @Path("/allVersions/{fileId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllFileVersions(@PathParam("fileId") Integer fileId) throws IOException {
        List<CTXEFileVersion> fileVersions = PersistenceUtils.getAllVersionsForFile(fileId);

        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(fileVersions);

        return getResponse(jsonInString);
    }

    // FUNCTIONAL
    @POST
    @Path("/uploadFile")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveFileToServer(
            @FormDataParam("fileUploaded") InputStream fileInputStream,
            @FormDataParam("fileUploaded") FormDataContentDisposition contentDispositionHeader,
            @FormDataParam("usernameToBeSent") String username) {

        String filePath = Utils.SERVER_UPLOAD_LOCATION_FOLDER + contentDispositionHeader.getFileName();
        CTXEFile file = new CTXEFile();
        file.setLastEditor(username);
        file.setLastModified(new Date().getTime());
        file.setName(contentDispositionHeader.getFileName());

        // save the file to the server and DB
        FileService.saveFileToServer(fileInputStream, filePath);
        PersistenceUtils.saveFileToDb(file);

        CTXEFileVersion fileVersion = Utils.getFileVersionFromFile(file);
        PersistenceUtils.saveFileVersionToDb(fileVersion);

        return Response.status(Response.Status.OK).build();
    }

    @POST
    @Path("/newFile")
    @Produces(MediaType.APPLICATION_JSON)
    public Response saveNewFile(@FormParam("fileName") String fileName,
                              @FormParam("author") String author) throws IOException, ParseException {

        if(!fileName.contains(".txt")) {
            fileName = fileName + ".txt";
        }
        String filePath = Utils.SERVER_UPLOAD_LOCATION_FOLDER + fileName;
        CTXEFile file = new CTXEFile();
        file.setLastEditor(author);
        file.setLastModified(new Date().getTime());
        file.setName(fileName);

        // save the file to the server and DB
        FileService.createNewFileToServer(filePath);
        PersistenceUtils.saveFileToDb(file);

        CTXEFileVersion fileVersion = Utils.getFileVersionFromFile(file);
        PersistenceUtils.saveFileVersionToDb(fileVersion);

        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(fileVersion);

        return getResponse(jsonInString);
    }

    @POST
    @Path("/deleteFileVersion")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteFileVersion(@FormParam("fileVersionName") String fileName) throws IOException, ParseException {

        String filePath = Utils.SERVER_UPLOAD_LOCATION_FOLDER + fileName;

        // save the file to the server and DB
        FileService.deleteFileFromServer(filePath);

        PersistenceUtils.deleteFileVersionFromDb(fileName);

        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(fileName);

        return getResponse(jsonInString);
    }

    @GET
    @Path("/getFile/{fileName}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response getFile(@PathParam("fileName") String fileName) {
        File file = new File(Utils.SERVER_UPLOAD_LOCATION_FOLDER + fileName);
        return Response.ok(file, MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"" ) //optional
                .build();
    }

    @GET
    @Path("/details/{identifier}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getDetailsBasedOnIdentifier(@PathParam("identifier") String identifier) throws IOException {
        CTXEFileVersion details = PersistenceUtils.getIdentifierDetails(identifier);
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(details);

        return getResponse(jsonInString);
    }

    @GET
    @Path("/download/{fileName}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadFile(@PathParam("fileName") final String fileName) {
        File file = new File(Utils.SERVER_UPLOAD_LOCATION_FOLDER + fileName);
        return Response.ok(file, MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"" ) //optional
                .build();
    }

    private Response getResponse(String json) {
        return Response
                .status(200)
                .entity(json)
                .build();
    }
}









//            RandomAccessFile r = new RandomAccessFile(file, "rw");
//            RandomAccessFile rtemp = new RandomAccessFile(new java.io.CTXEFile(file + "~"), "rw");
//            long fileSize = r.length();
//            FileChannel sourceChannel = r.getChannel();
//            FileChannel targetChannel = rtemp.getChannel();
//            long offset = 15;
//            sourceChannel.transferTo(offset, (fileSize - offset), targetChannel);
//            sourceChannel.truncate(offset);
//            r.seek(offset);
//            r.write("x".getBytes());
//            long newOffset = r.getFilePointer();
//            targetChannel.position(0L);
//            sourceChannel.transferFrom(targetChannel, newOffset, (fileSize - offset));
//            sourceChannel.close();
//            targetChannel.close();