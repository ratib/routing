package routing.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

public class SerializationHelper<T> {

	public void serialize(T obj, File file) {
		System.out.println("Serializing Object to File");
		try {
			FileOutputStream fileOut = new FileOutputStream(file);
			ObjectOutputStream outStream = new ObjectOutputStream(fileOut);
			outStream.writeObject(obj);
			outStream.close();
			fileOut.close();
		} catch (IOException i) {
			i.printStackTrace();
		}
		System.out.println("Serializing Object to File: finished");
	}

	public T deserialize(File file) {
		System.out.println("deserializing File to object");
		T obj = null;
		try {
			FileInputStream fileIn = new FileInputStream(file);

			ObjectInputStream in = new ObjectInputStream(fileIn);
			obj = (T) in.readObject();
			in.close();
			fileIn.close();
		} catch (IOException i) {
			i.printStackTrace();
		} catch (ClassNotFoundException c) {
			System.out.println(" File not found");
			c.printStackTrace();
		}
		System.out.println("Deserializing File to Object finished");
		return obj;
	}

	public void serialize(T obj, OutputStream os) {
		System.out.println("Serializing object to File using OutputStream");
		try {
			ObjectOutputStream outStream = new ObjectOutputStream(os);
			outStream.writeObject(obj);
			outStream.close();
			os.close();
		} catch (IOException i) {
			i.printStackTrace();
		}
		System.out.println("Serializing object to File using OutputStream: finished");
	}

	public T deserialize(InputStream ins) {
		System.out.println("deserializing InputStream to object");
		T obj = null;
		try {

			ObjectInputStream in = new ObjectInputStream(ins);
			obj = (T) in.readObject();
			in.close();
			ins.close();

		} catch (IOException i) {
			i.printStackTrace();
		} catch (ClassNotFoundException c) {
			System.out.println(" File not found");
			c.printStackTrace();
		}
		System.out.println("deserializing InputStream to object finished");
		return obj;
	}

}
