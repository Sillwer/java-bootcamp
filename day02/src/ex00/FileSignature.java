package ex00;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class FileSignature {
    static private OutputStream logStream = null;
    static final HashMap<String, byte[]> signature = new HashMap<>();

    static void setupSignature(String filePath) throws FileNotFoundException, Exception {
        try (FileReader fileReader = new FileReader(filePath);
                Scanner scanner = new Scanner(fileReader)) {
            while (scanner.hasNextLine()) {
                String line = null;
                try {
                    line = scanner.nextLine();
                    if (line.isEmpty()) {
                        continue;
                    }

                    String[] linePart = line.split(",");
                    if (linePart.length < 2 || (linePart[0].isEmpty()) || linePart[1].isEmpty()) {
                        throw new Exception();
                    }

                    String name = linePart[0].trim();

                    String[] sequenceStr = linePart[1].trim().split("\\s+");
                    byte[] sequence = new byte[sequenceStr.length];
                    for (int i = 0; i < sequenceStr.length; i++) {
                        sequence[i] = (byte) Integer.parseInt(sequenceStr[i], 16);
                    }

                    signature.put(name, sequence); // повтор перепишет прошлую запись
                } catch (Exception e) {
                    logPrintf("Skip wrong signature record: [%s]\n", line);
                }
            }
        }

        if (signature.isEmpty()) {
            throw new Exception("Error: file with signatures list is empty");
        }
    }

    /**
     * @return null - if signature UNDEFINED
     */
    static String defineSignature(String filePath)
            throws FileNotFoundException, SecurityException, Exception {
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {

            ArrayList<Byte> fileBytes = new ArrayList<>();

            for (Map.Entry<String, byte[]> sign : signature.entrySet()) {
                String signatureName = sign.getKey();
                byte[] signatureBytes = sign.getValue();

                boolean defined = true;
                for (int i = 0; i < signatureBytes.length; i++) {
                    if (i == fileBytes.size()) {
                        fileBytes.add((byte) fileInputStream.read());
                    }

                    if (signatureBytes[i] != fileBytes.get(i)) {
                        defined = false;
                        break;
                    }
                }

                if (defined) {
                    return signatureName;
                }
            }
        }
        return null;
    }

    static void setLogStream(OutputStream outputStream) {
        logStream = outputStream;
    }

    static private void logPrintf(String str, Object... args) {
        if (logStream == null) {
            return;
        }

        try {
            logStream.write(String.format(str, args).getBytes());
        } catch (Exception e) {
            System.out.printf("В системе логирования ошибка: %s\n", e.getMessage());
        }
    }

    static void printSignatures() {
        if (signature.isEmpty()) {
            System.out.println("Signatures list is empty");
        } else {
            System.out.println("Signatures:");
            for (Map.Entry<String, byte[]> entry : signature.entrySet()) {
                System.out.printf("%-8s [ ", entry.getKey());
                for (byte b : entry.getValue()) {
                    System.out.printf("%X ", b);
                }
                System.out.println("]");
            }
        }
    }
}
