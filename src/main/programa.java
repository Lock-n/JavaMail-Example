import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;

public class programa {
	
	public static void check(String host, String storeType, String user,
	      String password) 
   {
      try 
      {

	      //create properties field
	      Properties properties = new Properties();
	
	      //properties.put is a method that receives two strings, one is the name of the parameter that will be
	      //added, and the other is the parameter
	      properties.put("mail.pop3.host", host);
	      properties.put("mail.pop3.port", "995");
	      properties.put("mail.pop3.starttls.enable", "true");
	      Session emailSession = Session.getDefaultInstance(properties);
	  
	      //create the POP3 store object and connect with the pop server
	      Store store = emailSession.getStore("pop3s");
	
	      store.connect(host, user, password);
	
	      //create the folder object and open it
	      
	      /*Folder[] fs = store.getDefaultFolder().list();
	      
	      for(Folder fd:fs)
	    	    System.out.println(">> "+fd.getName());*/
	      
	      Folder emailFolder = store.getFolder("INBOX");
	      emailFolder.open(Folder.READ_ONLY);
	
	      // retrieve the messages from the folder in an array and print it
	      Message[] messages = emailFolder.getMessages();
	      System.out.println("number of e-mails: " + messages.length);
	
	      for (int i = 0, n = messages.length; i < n; i++) 
	      {
	         Message message = messages[i];
	         System.out.println("---------------------------------");
	         System.out.println("Email Number " + (i + 1));//number of the email
	         System.out.println("Subject: " + message.getSubject());// Subject of the email
	         System.out.println("From: " + message.getFrom()[0]);// who sent the email
	         System.out.println("Date: " + message.getSentDate());// when the email was sent
	         System.out.println("Content:\n" + getTextFromMimeMultipart((MimeMultipart) message.getContent()));
	      }
	
	      //close the store and folder objects
	      emailFolder.close(false);
	      store.close();
	
      } catch (NoSuchProviderException e) {
         e.printStackTrace();
      } catch (MessagingException e) {
         e.printStackTrace();
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
				        		part.saveFile("c:/temp/attachments/" + part.getFileName());
				        		System.out.println("saved at c:/temp/attachments/" + part.getFileName());
				        	}
				        }
			        }
	    }
	    return result;
	}

   public static void main(String[] args) {

      String host = "pop.gmail.com";// host server, in the case of pop 3 for gmail: pop.gmail.com
      String mailStoreType = "pop3";//kind of protocol used to store
      String username = "valle.drik11@gmail.com";// email of whom the messages will be shown
      String password = "CACHORRO123";// password of the email

      check(host, mailStoreType, username, password);

   }

}
