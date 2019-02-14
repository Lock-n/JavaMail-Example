package main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.*;
 
public class Main{
 
 private static final String email_id = "lucas.da.silva00101@gmail.com";
 private static final String password = "12345senha";
 
 public static void main(String[] args) {
 
 
  //set properties 
  Properties properties = new Properties();
  //You can use imap or imaps , *s -Secured
  properties.put("mail.store.protocol", "imaps");
  //Host Address of Your Mail
  properties.put("mail.imaps.host", "imap.gmail.com");
  //Port number of your Mail Host
  properties.put("mail.imaps.port", "993");
 
  //properties.put("mail.imaps.timeout", "10000");
 
  try {
 
   //create a session  
   Session session = Session.getDefaultInstance(properties, null);
   //SET the store for IMAPS
   Store store = session.getStore("imaps");
 
   System.out.println("Connection initiated......");
   //Trying to connect IMAP server
   store.connect(email_id, password);
   System.out.println("Connection is ready :)");
 
 
   //Get inbox folder 
   Folder inbox = store.getFolder("inbox");
   //SET readonly format (*You can set read and write)
   inbox.open(Folder.READ_ONLY);
 
 
   //Display email Details 
 
   //Inbox email count
   int messageCount = inbox.getMessageCount();
   System.out.println("Total Messages in INBOX:- " + messageCount);
   
   Message mensage = inbox.getMessage(messageCount);
 
   //Print Last 10 email information
    System.out.println("Mail Subject:- " + mensage.getSubject());
    System.out.println("Mail From:- " + mensage.getFrom()[0]);
    System.out.println("Mail Content:- " + mensage.getContent().toString());    
    System.out.println("------------------------------------------------------------");
    
    //Faz o download do anexo
    List<File> attachments = new ArrayList<File>();
    for (Message message : temp) {
        Multipart multipart = (Multipart) message.getContent();

        for (int i = 0; i < multipart.getCount(); i++) {
            BodyPart bodyPart = multipart.getBodyPart(i);
            if(!Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition()) &&
                   (bodyPart.getFileName().replace(" ", "").isEmpty())) {
                continue; // dealing with attachments only
            } 
            InputStream is = bodyPart.getInputStream();
            // -- EDIT -- SECURITY ISSUE --
            // do not do this in production code -- a malicious email can easily contain this filename: "../etc/passwd", or any other path: They can overwrite _ANY_ file on the system that this code has write access to!
            File f = new File("/tmp/" + bodyPart.getFileName());
            FileOutputStream fos = new FileOutputStream(f);
            byte[] buf = new byte[4096];
            int bytesRead;
            while((bytesRead = is.read(buf))!=-1) {
                fos.write(buf, 0, bytesRead);
            }
            fos.close();
            attachments.add(f);
        }
    }

 
   inbox.close(true);
   store.close();
 
  } catch (Exception e) {
   e.printStackTrace();
  }
 
 
 
 
 }
}