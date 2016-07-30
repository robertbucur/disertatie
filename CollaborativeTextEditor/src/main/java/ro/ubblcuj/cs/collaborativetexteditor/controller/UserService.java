package ro.ubblcuj.cs.collaborativetexteditor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ro.ubblcuj.cs.collaborativetexteditor.model.CTXEFile;
import ro.ubblcuj.cs.collaborativetexteditor.model.CTXEFileChange;
import ro.ubblcuj.cs.collaborativetexteditor.model.CTXEFileVersion;
import ro.ubblcuj.cs.collaborativetexteditor.persistence.HibernateUtil;
import ro.ubblcuj.cs.collaborativetexteditor.utils.FileService;
import ro.ubblcuj.cs.collaborativetexteditor.utils.Utils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.Date;
import java.util.List;

@Path("/service")
public class UserService {

    @GET
    @Path("/newFile/{fileName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createFile(@PathParam("fileName") String fileName) {
        java.io.File file = new java.io.File(fileName + ".txt");

        try {
            if (file.createNewFile()) {
                System.out.println("CTXEFile is created!");
            } else {
                System.out.println("CTXEFile already exists.");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return getResponse(null);
    }

    @POST
    @Path("/saveVersion")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response saveFileVersion(@FormParam("fileIdVersioning") Integer fileId,
                                    @FormParam("fileNameVersioning") String fileName) {


//        int nextVersion = HibernateUtil.getNextVersionNumber(fileId);
//        java.io.File file = new java.io.File(fileName + nextVersion + ".txt");
//
//        try {
//            if (file.createNewFile()) {
//                System.out.println("CTXEFileVersion is created!");
//            } else {
//                System.out.println("CTXEFileVersion already exists.");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        return getResponse(fileName + fileId);
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
                              @FormParam("charValue") String charValue) {

        CTXEFileChange fileChange = new CTXEFileChange();
        fileChange.setFileId(fileId);
        fileChange.setFileVersionId(fileVersionId);
        fileChange.setDatetime(new Date());
        fileChange.setCharColumn(charColumn);
        fileChange.setCharRow(charRow);
        fileChange.setCharPosition(charPosition);
        fileChange.setCharValue(charValue);

        HibernateUtil.insertFileChange(fileChange);

        return getResponse(null);
    }

    // FUNCTIONAL
    @GET
    @Path("/allFiles")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllFiles() throws IOException {
        SessionFactory sessionFactory = HibernateUtil.configureSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        List<CTXEFile> files = HibernateUtil.getAllFiles();

        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(files);

        return getResponse(jsonInString);
    }

    @GET
    @Path("/allVersions/{fileId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllFileVersions(@PathParam("fileId") Integer fileId) throws IOException {
        SessionFactory sessionFactory = HibernateUtil.configureSessionFactory();
        Session session = sessionFactory.openSession();
        session.beginTransaction();

        List<CTXEFileVersion> fileVersions = HibernateUtil.getAllVersionsForFile(fileId);

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
        file.setLastModified(new Date());
        file.setName(contentDispositionHeader.getFileName());

        // save the file to the server and DB
        FileService.saveFileToServer(fileInputStream, filePath);
        HibernateUtil.saveFileToDb(file);

        return Response.status(Response.Status.OK).build();
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