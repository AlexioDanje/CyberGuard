package com.danjetech.cyberguard.service;

import com.danjetech.cyberguard.enums.TaskStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ClamAVService {
    @Value("${clamav.statusCommand}")
    private String statusCommand;
    @Value("${clamav.startDaemon}")
    private String startDaemon;
    @Value("${clamav.stopDaemon}")
    private String stopDaemon;
    @Value("${clamav.scanSystem}")
    private String scanSystem;
    @Value("${clamav.scanPath}")
    private String scanPath;
    @Value("${clamav.updateClam}")
    private String updateClam;

    private Map<UUID, TaskStatus> taskStatuses = new ConcurrentHashMap<>();
    private Map<UUID, String> taskResults = new ConcurrentHashMap<>();

    public String getStatus() throws Exception {
        String[] commands = statusCommand.split(" ");
        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            String target = "Active: ";
            if (line.contains(target)) {
                return line.trim().substring(target.length());
            }
        }
        process.waitFor();
        throw new Exception("Command execution failed");
    }

    public void startDaemon() throws Exception {
        String[] commands = startDaemon.split(" ");
        ProcessBuilder processBuilder = new ProcessBuilder( commands);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        process.waitFor();
    }

    public void stopDaemon() throws Exception {
        String[] commands = stopDaemon.split(" ");
        ProcessBuilder processBuilder = new ProcessBuilder( commands);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        process.waitFor();
    }

    public void updateClam() throws Exception {
        String[] commands = updateClam.split(" ");
        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        process.waitFor();
    }

    @Async
    public void scanSystem(UUID taskId) throws Exception {
        String[] commands = scanSystem.split(" ");
        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        runProcess(processBuilder, taskId);
    }

    @Async
    public void scanPath(String path, UUID taskId) throws Exception {
        String[] pathArray = scanPath.split(" ");
        List<String> commands = new ArrayList<>(Arrays.asList(pathArray));
        commands.add(path);
        ProcessBuilder processBuilder = new ProcessBuilder(commands);
        runProcess(processBuilder, taskId);
    }

    private void runProcess(ProcessBuilder processBuilder, UUID taskId) throws IOException, InterruptedException {
        processBuilder.redirectErrorStream(true);
        Process process = processBuilder.start();
        taskStatuses.put(taskId, TaskStatus.RUNNING);

        InputStream inputStream = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        StringBuilder result = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.contains(" FOUND")) {
                result.append(line, 0, line.indexOf(":"));
            }
        }
        process.waitFor();
        taskResults.put(taskId, result.toString());
        taskStatuses.put(taskId, TaskStatus.COMPLETE);
    }

    public TaskStatus getTaskStatus(UUID taskId) {
        return taskStatuses.getOrDefault(taskId, TaskStatus.NOT_FOUND);
    }

    public String getTaskResult(UUID taskId) {
        return taskResults.getOrDefault(taskId, "Scan result not found.");
    }
}
