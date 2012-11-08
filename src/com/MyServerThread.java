package com;


 import java.io.DataInputStream;  
 import java.io.DataOutputStream;  
 import java.io.File;  
 import java.io.FileInputStream;  
 import java.io.IOException;  
 import java.io.InputStream;  
 import java.io.RandomAccessFile;  
 import java.net.Socket;  
 import java.nio.channels.FileChannel;  
 import java.nio.channels.FileLock;  
 import java.util.Map;  
 import java.util.StringTokenizer;  
  
   
   
 public class MyServerThread extends Thread {  
   
    private Socket socket;  
   
     public MyServerThread(Socket socket ) {  
         this.socket = socket;  
         this.start();
    }  
     @Override  
     public void run() {
      
    	 	System.out.println("run");
            DataInputStream dataInputStream = null;  
             DataOutputStream dataOutputStream = null;  
             RandomAccessFile randomAccessFile = null;  
             FileLock fileLock = null;
             FileChannel fileChannel =null;
              
             boolean isInfoSubmission = false;  
//            Document docInfoSubmission = null;  
             Double totalSize = 0.0;  
//            DocProperty docProperty = null;  
           
             try {
                 dataInputStream = new DataInputStream(socket.getInputStream());  
                 dataOutputStream = new DataOutputStream(socket.getOutputStream());   
                  
                 //读取名称  
                 String fileName = dataInputStream.readUTF();  
                  
                 String fileSize = dataInputStream.readUTF();  
                 
//                 File f = new File(this.getClass().getResource("").getPath()); 
//                 System.out.println(f.getPath().substring(0,f.getPath().indexOf("WEB-INF"))+"upload/"); 
                 
                 //检测上传文件是否存在  
//                 String FilePath = f.getPath().substring(0,f.getPath().indexOf("WEB-INF"))+"upload";  //可使用配置文件形式将路径写清楚  
                 String filePath = ConstantsConfig.filePath;
                 StringTokenizer st = new StringTokenizer(filePath.toString(),"/");     
                 String   toAddPath = st.nextToken()+"/";     
                String   toTestPath = toAddPath;     
                while(st.hasMoreTokens()){
                     toAddPath = st.nextToken()+"/";     
                      toTestPath += toAddPath;     
                      File inbox   =   new File(toTestPath);     
                       if(!inbox.exists()) {  
                          inbox.mkdir();     
                       }  
                 }    
                   
                         //检测上传位置  
                         File file = new File( filePath + "/" + fileName);  
                long position = 0;  
                  
                 if(file.exists()){
                        position = file.length();  
                 }  
                   
                //通知客户端已传大小  
                 dataOutputStream.writeUTF(String.valueOf(position));  
                 dataOutputStream.flush();  
       
                 byte[] buffer = null;  
                int read = 0;  
                   
                 while(true){
                    //检测上传位置  
                    file = new File( filePath + "/" + fileName);
                    position = 0;
                     if(file.exists()){
                         position = file.length();  
                    }  
                       
                        //rw代表写流(随机读写）  
                     randomAccessFile  = new RandomAccessFile(file,"rw");  
                       
                     fileLock = null;  
                     fileChannel = null;   
                     fileChannel = randomAccessFile.getChannel();  
                     fileLock = fileChannel.tryLock();  
                      
                     //拿到了文件锁,写入数据 
                    if(fileLock != null){  
                        randomAccessFile.seek(position);  
                          
                     read = 0;  
                         buffer = new byte[1024];  
                         read = dataInputStream.read(buffer);  
                        randomAccessFile.write(buffer,0,read);  
                        if(fileLock != null){  
                             fileLock.release();  
                            fileLock = null;  
                         }  
                         if(randomAccessFile != null){  
                            randomAccessFile.close();  
                             randomAccessFile = null;  
                         }  
                    }     
                      
                     //检测已上传的大小  
                       file = new File( filePath + "/" + fileName);  
                    position = 0;  
                    if(file.exists()){  
                             position = file.length();  
                     }  
//                     System.out.println("文件  " + fileName + "  已传输  " + String.valueOf(position)+ "字节");  
                       
                     
                     //判断文件是否传输完成  
                     if(position >= Long.parseLong(fileSize)){  
                          //文件传输完成  
                           System.out.println("文件  " + fileName + "  已传输完毕，总大小为" + String.valueOf(position) + "字节");  
                             break ;  
                     }else{  
                             //通知客户端已传大小  
                        dataOutputStream.writeUTF(String.valueOf(position));
                         dataOutputStream.flush();  
                     }
                }   // END WHILE  
                  
                //跳出while循环，即已结束文件上传，则终止socket通信  
                
                  
            } catch (Exception e) {  
                // TODO Auto-generated catch block  
                e.printStackTrace();  
            }finally{
            	if(fileLock!=null){
            		
            	}
            	try {
            		if(fileChannel!=null){
                		fileChannel.close();
                	}
                	if(randomAccessFile!=null){
                		randomAccessFile.close();
                	}
					dataInputStream.close();
					 dataOutputStream.close();  
	                socket.close(); 
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
            }
    }  
 }  


