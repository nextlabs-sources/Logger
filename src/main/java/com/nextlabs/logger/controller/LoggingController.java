package com.nextlabs.logger.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nextlabs.logger.model.ActivityLog;
import com.nextlabs.logger.model.ApplicationLog;
import com.nextlabs.logger.model.AuditLog;
import com.nextlabs.logger.service.LoggingService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * @author Sarita Sethy
 */

@RestController
public class LoggingController {

	private static final Logger LOG = LogManager.getLogger("com.nextlabs.logger");
	LoggingService loggingService = new LoggingService();

	@RequestMapping(value = "/application", method = RequestMethod.PUT)
	public ResponseEntity<?> createApplicationLog(@RequestBody ApplicationLog log) {
		String syslogMessage = null;
		try {
			syslogMessage = new ObjectMapper().writeValueAsString(log);
		} catch (JsonProcessingException e) {
			LOG.debug(e.getMessage());
			throw new ResponseStatusException(
					HttpStatus.PROCESSING, "Error occurred during request processing", e);
		}
		return loggingService.appLogMessage(syslogMessage, log.getLevel());
	}

	@RequestMapping(value = "/audit", method = RequestMethod.PUT)
	public ResponseEntity<?> createAuditLog(@RequestBody AuditLog log) {
		String syslogMessage = null;
		try {
			syslogMessage = new ObjectMapper().writeValueAsString(log);
		} catch (JsonProcessingException e) {
			LOG.debug(e.getMessage());
			throw new ResponseStatusException(
					HttpStatus.PROCESSING, "Error occurred during request processing", e);

		}
		return loggingService.auditLogMessage(syslogMessage);
	}

	@RequestMapping(value = "/activity", method = RequestMethod.PUT)
	public ResponseEntity<?> createActivityLog(@RequestBody ActivityLog log) {
		String syslogMessage = null;
		try {
			syslogMessage = new ObjectMapper().writeValueAsString(log);
		} catch (JsonProcessingException e) {
			LOG.debug(e.getMessage());
			throw new ResponseStatusException(
					HttpStatus.PROCESSING, "Error occurred during request processing", e);

		}
		return loggingService.activityLogMessage(syslogMessage);
	}
}
