package com.server;


import java.io.*;
import java.net.Socket;

/**
 * Created by wzc on 14/12/16.
 */
public class Processor extends Thread{
    private Socket socket;
    private InputStream in;
    private PrintStream out;
    public  final static String  WebRoot = "/Users/wzc/IdeaProjects/webserver/htdocs/";
    public Processor(Socket socket) {
        this.socket = socket;
        try {
            in =  socket.getInputStream();
            out = new PrintStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void run() {
        String filename = parse(in);
        System.out.println("-------filename--------"+filename);
        sendFile(filename);
    }

    public String  parse(InputStream inputstream){
        BufferedReader br = new BufferedReader(new InputStreamReader(inputstream));
        String filename = null;
        try {
            String httpMessage = br.readLine();
            System.out.println("httpMessage"+httpMessage);
            String[] content = httpMessage.split(" ");
            if(content.length!=3){
                this.sendErrorMessage(400,"Client query error");
                return null;
            }
            System.out.println("codeState---"+content[0]+",contentFilename----"+content[1]+",versionNo----"+content[2]);
            filename = content[1];
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filename;
    }

    public void sendErrorMessage(int ErrorNo,String message){
        out.println("HTTP/1.1"+ErrorNo+message);
        out.println("content-type : text/html");
        out.println();
        out.println("<html>");
        out.println("<title>Error Message");
        out.println("</title>");
        out.println("<body>");
        out.println("<h1>ErrorCode"+ErrorNo+"Error Message"+message+"</h1>");
        out.println("</body>");
        out.println("</html>");

    }
    public void  sendFile(String filename){
        File file = new File(Processor.WebRoot+filename);
        if(!file.exists()){
            sendErrorMessage(404,"File not found!");
            return;
        }
        try {
            InputStream in = new FileInputStream(file);
            byte[] content = new byte[(int)file.length()];
            in.read(content);
            out.write(content);
            out.flush();
            out.close();
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
