package ro.ubblcuj.cs.collaborativetexteditor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ro.ubblcuj.cs.collaborativetexteditor.model.CTXEFile;
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