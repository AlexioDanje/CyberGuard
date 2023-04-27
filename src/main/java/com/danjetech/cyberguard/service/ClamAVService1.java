package com.danjetech.cyberguard.service;

import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

@Service
public class ClamAVService1 {

    public String getStatus() throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder("clamdscan", "--version");
        return executeCommand(processBuilder);
    }

    public void startDaemon() throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder("sudo", "systemctl", "start", "clamav-daemon");
        executeCommand(processBuilder);
    }

    public void stopDaemon() throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder("sudo", "systemctl", "stop", "clamav-daemon");
        executeCommand(processBuilder);
    }

    public String scanSystem() throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder("sudo", "clamscan", "-r", "/");
        return executeCommand(processBuilder);
    }

    public String scanPath(String path) throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder("sudo", "clamscan", "-r", path);
        return executeCommand(processBuilder);
    }

    public void updateClam() throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder("sudo", "freshclam");
        executeCommand(processBuilder);
    }

    private String executeCommand(ProcessBuilder processBuilder) throws Exception {
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line + "\n");
        }
        int exitVal = process.waitFor();
        if (exitVal == 0) {
            return output.toString();
        } else {
            throw new Exception("Command execution failed with exit code " + exitVal);
        }
    }
}
