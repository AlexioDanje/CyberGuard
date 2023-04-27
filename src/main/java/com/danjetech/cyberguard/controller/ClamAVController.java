package com.danjetech.cyberguard.controller;// Import necessary libraries and modules
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/clamav")
public class ClamAVController {

  // Inject ClamAVService
  @Autowired
  private ClamAVService clamAVService;

  @GetMapping("/status")
  public ResponseEntity<String> getStatus() {
    try {
      String status = clamAVService.getStatus();
      return new ResponseEntity<>(status, HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity<>("Error getting ClamAV status", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/start")
  public ResponseEntity<String> startDaemon() {
    try {
      clamAVService.startDaemon();
      return new ResponseEntity<>("ClamAV daemon started successfully", HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity<>("Error starting ClamAV daemon", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/stop")
  public ResponseEntity<String> stopDaemon() {
    try {
      clamAVService.stopDaemon();
      return new ResponseEntity<>("ClamAV daemon stopped successfully", HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity<>("Error stopping ClamAV daemon", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/scan_system")
  public ResponseEntity<String> scanSystem() {
    try {
      String scanResult = clamAVService.scanSystem();
      return new ResponseEntity<>(scanResult, HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity<>("Error scanning system", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/scan_path")
  public ResponseEntity<String> scanPath(@RequestBody String path) {
    try {
      String scanResult = clamAVService.scanPath(path);
      return new ResponseEntity<>(scanResult, HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity<>("Error scanning path", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @PostMapping("/update_clam")
  public ResponseEntity<String> updateClam() {
    try {
      clamAVService.updateClam();
      return new ResponseEntity<>("ClamAV virus definition database updated successfully", HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
      return new ResponseEntity<>("Error updating ClamAV virus definition database", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
