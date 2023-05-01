package com.danjetech.cyberguard.controller;
import com.danjetech.cyberguard.enums.TaskStatus;
import com.danjetech.cyberguard.service.ClamAVService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/clamav")
@RequiredArgsConstructor
@Api(value = "My API", tags = {"My API"})
public class ClamAVController {

  // Inject ClamAVService
  private final ClamAVService clamAVService;

  @GetMapping("/status")
  public ResponseEntity<Map<String, String>> getStatus() {
    try {
      String status = clamAVService.getStatus();
      return new ResponseEntity<>(Collections.singletonMap("status", status), HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity<>(Collections.singletonMap("status", "Error getting ClamAV status"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/start")
  public ResponseEntity<Map<String, String>> startDaemon() {
    try {
      clamAVService.startDaemon();
      return new ResponseEntity<>(Collections.singletonMap("message", "ClamAV daemon started successfully"), HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity<>(Collections.singletonMap("message", "Error starting ClamAV daemon"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/stop")
  public ResponseEntity<Map<String, String>> stopDaemon() {
    try {
      clamAVService.stopDaemon();
      return new ResponseEntity<>(Collections.singletonMap("message", "ClamAV daemon stopped successfully"), HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity<>(Collections.singletonMap("message", "Error stopping ClamAV daemon"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/update_clam")
  public ResponseEntity<Map<String, String>> updateClam() {
    try {
      clamAVService.updateClam();
      return new ResponseEntity<>(Collections.singletonMap("message", "updated successfully"), HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity<>(Collections.singletonMap("message", "Error updating ClamAV daemon"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/scan_system")
  public ResponseEntity<Map<String, String>> scanSystem() {
    try {
      UUID taskId = UUID.randomUUID();
      clamAVService.scanSystem(taskId);
      return new ResponseEntity<>(Collections.singletonMap("message", "Scan ID:"+ taskId), HttpStatus.ACCEPTED);
    } catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity<>(Collections.singletonMap("message", "Error scanning"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/scan_path")
  public ResponseEntity<Map<String, String>> scanPath(@RequestBody String path) {
    try {
      UUID taskId = UUID.randomUUID();
      clamAVService.scanPath(path, taskId);
      return new ResponseEntity<>(Collections.singletonMap("message", "Scan ID:"+ taskId), HttpStatus.ACCEPTED);
    } catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity<>(Collections.singletonMap("message", "Error scanning"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/scan_status/{taskId}")
  public ResponseEntity<String> getScanTaskStatus(@PathVariable UUID taskId) {
    TaskStatus status = clamAVService.getTaskStatus(taskId);
    if (status == TaskStatus.RUNNING) {
      return ResponseEntity.ok().body("Task " + taskId.toString() + " is still running.");
    } else if (status == TaskStatus.COMPLETE) {
      String result = clamAVService.getTaskResult(taskId);
      return ResponseEntity.ok().body("Task " + taskId.toString() + " is complete with result: " + result);
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}
