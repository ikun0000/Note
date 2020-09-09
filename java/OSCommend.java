package org.example.process;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class OSCommend {

    public static void main(String[] args) {
        OSCommend.execute("dir");
    }

    public static void execute(String command) {
        try {
            Process process = new ProcessBuilder(command.split(" ")).start();

            BufferedReader stdout =
                    new BufferedReader(new InputStreamReader(process.getInputStream(), "GBK"));
            String result = null;
            while ((result = stdout.readLine()) != null) {
                System.out.println(result);
            }

            BufferedReader stderr =
                    new BufferedReader(new InputStreamReader(process.getErrorStream(), "GBK"));
            while ((result = stderr.readLine()) != null) {
                System.out.println(result);
            }

        } catch (Exception e) {
            if (!command.startsWith("CMD /C")) {
                execute("CMD /C " + command);
            } else {
                e.printStackTrace();
            }
        }
    }
}
