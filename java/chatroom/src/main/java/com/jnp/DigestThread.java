package com.jnp;
import java.io.*;
import java.security.*;
import javax.xml.bind.*; // for DatatypeConverter; requires Java 6 or JAXB 1.0

public class DigestThread extends Thread {

  private String filename;

  public DigestThread(String filename) {
   this.filename = filename;
  }

  @Override
  public void run() {
    try {
      FileInputStream in = new FileInputStream(filename);
      MessageDigest sha = MessageDigest.getInstance("SHA-256");
      DigestInputStream din = new DigestInputStream(in, sha);
      int ch;
      while ((ch = din.read()) != -1) {
          System.out.println("ch:" + ch);
      }
      din.close();
      byte[] digest = sha.digest();
      
      StringBuilder result = new StringBuilder(filename);
      result.append(": ");
      result.append(DatatypeConverter.printHexBinary(digest));
      System.out.println(result);
    } catch (IOException ex) {
      System.err.println(ex);
    } catch (NoSuchAlgorithmException ex) {
      System.err.println(ex);
    }
  }
  
  public static void main(String[] args) {
    String[] filenames = new String[]{"D:\\data\\jstck.log"};
    for (String filename : filenames) {
      Thread t = new DigestThread(filename);
      t.start();
    }
  }
}