package com.ecnu.rai.counsel.utils;


import java.io.*;

public class CommonUtil {
   private static final int BUFFER_SIZE = 1024 * 8;
   public static String getBody(InputStream inputStream) throws IOException {
   Reader reader = new BufferedReader(new InputStreamReader(inputStream));
   StringWriter writer = new StringWriter();
   int read;
   char[] buf = new char[BUFFER_SIZE];
   while ((read = reader.read(buf)) != -1) {
    writer.write(buf, 0, read);
   }
   return writer.getBuffer().toString();
   }
}
