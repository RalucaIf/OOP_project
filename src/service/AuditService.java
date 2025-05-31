package service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditService {
    private static AuditService auditService;
    private final FileWriter fileWriter;

    private AuditService(String filePath) {
        try {
            fileWriter = new FileWriter(filePath, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static AuditService getInstance() {
        if(auditService == null) {
            auditService = new AuditService("./Audit.csv");
        }
        return auditService;
    }

    public void writeToCSV(String action){
        LocalDateTime currentTime = LocalDateTime.now();
        String formattedTime = currentTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        try{
            fileWriter.append(action).append(", ").append(formattedTime).append(" \n");
            fileWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        try{
            if (fileWriter != null){
                fileWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
