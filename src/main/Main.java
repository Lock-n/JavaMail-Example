package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
 
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
   
   for (int i = 1; i <= messageCount; i++) {
	   Message mensage = inbox.getMessage(i);
	   
	   //Print Last 10 email information
	    System.out.println("Mail Subject:- " + mensage.getSubject());
	    System.out.println("Mail From:- " + mensage.getFrom()[0]);
	    System.out.println("Date: " + mensage.getSentDate());
	    System.out.println("Content:\n" + getTextFromMimeMultipart((MimeMultipart) mensage.getContent()));  
	    System.out.println("------------------------------------------------------------");
	    
   }
 
   inbox.close(true);
   store.close();
 
  } catch (Exception e) {
   e.printStackTrace();
  }
 
 
 
 
 }


 private static String getTextFromMimeMultipart(
      MimeMultipart mimeMultipart)  throws MessagingException, IOException{
      String result = "";
      int count = mimeMultipart.getCount();
      for (int i = 0; i < count; i++) {
          BodyPart bodyPart = mimeMultipart.getBodyPart(i);
          if (bodyPart.isMimeType("text/plain")) 
          {
            //is a text
              result = result + "\n" + bodyPart.getContent();
              break; // without break same text appears twice in my tests
          } else 
            if (bodyPart.isMimeType("text/html")) 
            {
                String html = (String) bodyPart.getContent();
                //to read html and exibit it, jsoup library whould be needed
            }
            else
              if (bodyPart.getContent() instanceof MimeMultipart)
              {
                //is a MimeMultipart
                result = result + getTextFromMimeMultipart((MimeMultipart)bodyPart.getContent());
              }
              else 
              {
                //attachment
                MimeBodyPart part = (MimeBodyPart) mimeMultipart.getBodyPart(i);
                
                if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) 
                {
                    // this part is attachment
                  System.out.println("There is an attachment in the email, whould you like to save it?(Y/N)");
                  BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
                  if(teclado.readLine().trim().toLowerCase().equals("y"))
                  {
                    part.saveFile("c:/Temp/attachments/" + part.getFileName());
                    System.out.println("saved at c:/temp/attachments/" + part.getFileName());
                  }
                }
              }
      }
      return result;
  }
}