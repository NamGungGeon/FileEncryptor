import java.io.*;
import java.math.BigDecimal;
import java.util.Random;

public class FileEncryptor {
	
    //this class is never instantiate
    private FileEncryptor(){}
    
    //encryptLevel value is must 1~5
    //default value is 1
    private static int encryptLevel=5;
    
    //File Encrypt
    //Encrypting Success is true, fail is false
    //key value generally have System.currentTimemillis()'s return value 
    static boolean encrypt(File file, long key){
    	if(file.exists()==false || file.isDirectory()==true){
    		return false;
    	}else{
    		InputStream inputStream=null;
    		try {
				inputStream=new FileInputStream(file);
			} catch (FileNotFoundException e) {
				//Error Log print area
				//Can't make stream
				return false;
			}
    		
    		//make stream of encrypted file
    		String fileName=file.getName();
    		String filePath=null;
    		filePath=file.getAbsolutePath().substring(0, file.getAbsolutePath().indexOf(fileName));

    		
    		String resultFileName=fileName+"_encrypt";

			File encryptFile=new File(filePath+resultFileName);
			if(encryptFile.exists()==true){
				//Already file having same file name is existing
				try {
					inputStream.close();
				} catch (IOException e1) {
					//Error Log print area
					//Can't close stream
				}
				return false;
			}
			
    		OutputStream fileMaker=null;
    		try {
				fileMaker=new FileOutputStream(encryptFile);
			} catch (FileNotFoundException e) {
				//Error Log print area
				//Can't make output stream
				try {
					inputStream.close();
				} catch (IOException e1) {
					//Error Log print area
					//Can't close stream
				}
				return false;
			}
    		
    		//read data from file
    		//and write data after encrypting
    		final int bufferSize=(int)Math.pow(2, 12);
    		byte buffer[]=new byte[bufferSize];
    		
    		long fileSize=file.length();
    		long encryptSize=fileSize;
    		switch(encryptLevel){
    		case 1:
    			encryptSize/=10;
    			break;
    		case 2:
    			encryptSize/=7;
    			break;
    		case 3:
    			encryptSize/=5;
    			break;
    		case 4:
    			encryptSize/=3;
    			break;
    		case 5:
    			break;
    		}

    		byte encryptValue=(byte) (key%10);
    		while(true){
    			if(encryptValue==0){
    				encryptValue=(byte) ((key/10)%10);
    			}else{
    				break;
    			}
    		}
    		
    		int readSize=0;
    		while(true){
        		try {
					readSize=inputStream.read(buffer, 0, buffer.length-1);
				} catch (IOException e) {
					//Error Log print area
					//Can't read data
					try {
						inputStream.close();
						fileMaker.close();
					} catch (IOException e1) {
						//Error Log print area
						//Can't close stream
					}
					return false;
				}
        		if(readSize<=0){
        			break;
        		}
        		try{
        			//Encrpyt head of file, not all of file
        			if(encryptSize>0){
            			boolean temp=false;
            			for(int i=0; i<readSize; i++){
            				if(temp){
            					buffer[i]=(byte) (buffer[i]+encryptValue);
            					temp=false;
            				}else{
            					buffer[i]=(byte) (buffer[i]-encryptValue);
            					temp=true;
            				}
            			}
            			encryptSize-=readSize;
        			}
					fileMaker.write(buffer, 0, readSize-1);
        		}catch(IOException e){
					//Error Log print area
					//Can't write data
        			try {
						inputStream.close();
						fileMaker.close();
					} catch (IOException e1) {
						//Error Log print area
						//Can't close stream
					}
        			return false;
        		}
    		}
    		
    		//End Reading
    		try {
				inputStream.close();
			} catch (IOException e1) {
				//Error Log print area
				//Can't close stream
				return false;
			}
    		
    		//Add Garbage value and Save Key value
    		DataOutputStream temp=new DataOutputStream(fileMaker);
    		try {
    			Random ran=new Random();
				for(int i=0; i<(int)(Math.pow(2, 10))*100; i++){
					temp.writeLong(ran.nextLong());
				}
				temp.writeLong(fileSize);
				temp.writeInt(encryptLevel);
				temp.writeLong(key);
				temp.close();
			} catch (IOException e1) {
				//Error Log print area
				//Can't close stream
				try {
					fileMaker.close();
				} catch (IOException e3) {
					//Error Log print area
					//Can't close stream
				}
    			return false;
			}
    		
    		//End encrypting
            try {
				fileMaker.close();
			} catch (IOException e) {
				//Error Log print area
				//Can't close stream
				return false;
			}
			//System.out.println("Success Encrypting!!");
            return true;
    	}
    }

    //File Decrypt
    //Encrypting Success is true, fail is false
    //key value generally have System.currentTimemillis()'s return value 
    static boolean decrypt(File file, long key){
    	return false;
    }

	public static int getEncryptLevel() {
		return encryptLevel;
	}

	public static void setEncryptLevel(int encryptLevel) throws EncryptLevelException{
		if(encryptLevel<1 || encryptLevel>5){
			throw new EncryptLevelException();
		}
		FileEncryptor.encryptLevel = encryptLevel;
	}
}
