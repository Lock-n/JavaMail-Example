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
  //seta as properties 
  Properties properties = new Properties();
  //O s de imaps é para -Secured, pode ser usado só imap
  properties.put("mail.store.protocol", "imaps");
  //endereço do host
  properties.put("mail.imaps.host", "imap.gmail.com");
  //porta do host
  properties.put("mail.imaps.port", "993");
 
  //properties.put("mail.imaps.timeout", "10000");
 
  try {
 
   //cria uma session
   Session session = Session.getDefaultInstance(properties, null);
   Store store = session.getStore("imaps");
 
   System.out.println("Conexão iniciada......");
   //Tenta conectar com o servidor IMAP
   store.connect(email_id, password);
   System.out.println("Conexão pronta:)");
 
 
   //acessa a pasta inbox
   Folder inbox = store.getFolder("inbox");
   //seta o formato de readonly
   inbox.open(Folder.READ_ONLY);
 
 
   //email count
   int messageCount = inbox.getMessageCount();
   System.out.println("Qtd de mensagens no INBOX:- " + messageCount);
   
   for (int i = 1; i <= messageCount; i++) {
	   Message mensage = inbox.getMessage(i);
	    //printa as informações do email
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
              break;
          } else 
            if (bodyPart.isMimeType("text/html")) 
            {
                String html = (String) bodyPart.getContent();
            }
            else
              if (bodyPart.getContent() instanceof MimeMultipart)
              {
                //é um multipart
                result = result + getTextFromMimeMultipart((MimeMultipart)bodyPart.getContent());
              }
              else 
              {
                //anexo
                MimeBodyPart part = (MimeBodyPart) mimeMultipart.getBodyPart(i);
                
                if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) 
                {
                    // essa parte é o anexo
                  System.out.println("Esse email tem um anexo, você quer salva-lo?(S/N)");
                  BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));
                  if(teclado.readLine().trim().toLowerCase().equals("s"))
                  {
                    part.saveFile("c:/Temp/attachments/" + part.getFileName());
                    System.out.println("salvo em c:/temp/attachments/" + part.getFileName());
                  }
                }
              }
      }
      return result;
  }
}