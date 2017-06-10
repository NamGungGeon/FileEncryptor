import java.io.File;

public class Example {
	public static void main(String args[]){
		long key=System.currentTimeMillis();
		
		FileEncryptor.encrypt(new File("C:\\Users\\Windows10.DESKTOP-UE9LE7G\\Desktop\\test.avi"), key);
		
		FileEncryptor.decrypt(new File("C:\\Users\\Windows10.DESKTOP-UE9LE7G\\Desktop\\test.avi_encrypt"), key);
		
	}
}
