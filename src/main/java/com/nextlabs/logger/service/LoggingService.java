package com.nextlabs.logger.service;

import com.nextlabs.logger.model.LogResponse;
import com.nextlabs.logger.util.LogUtility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class LoggingService {
	private static final Logger APP_LOG = LogManager.getLogger("com.nextlabs.application");
	private static final Logger AUDIT_LOG = LogManager.getLogger("com.nextlabs.audit");
	private static final Logger ACTIVITY_LOG = LogManager.getLogger("com.nextlabs.activity");

	public ResponseEntity<LogResponse> appLogMessage(String message, String logType) {
		LogUtility.LogLevel level = LogUtility.LogLevel.valueOf(logType);
		//syslog-ng removes all the text which comes before first colon including colon ':'
		StringBuilder builder = new StringBuilder(":");
		builder.append(message);

		try {
			switch (level) {
			case ERROR:
				APP_LOG.error(builder.toString());
				break;
			case DEBUG:
				APP_LOG.debug(builder.toString());
				break;
			case INFO:
				APP_LOG.info(builder.toString());
				break;
			case WARN:
				APP_LOG.warn(builder.toString());
				break;
			default:
				APP_LOG.trace(builder.toString());
				break;

			}
			LogResponse response = new LogResponse(HttpStatus.CREATED, "Message has been successfully saved.");
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			LogResponse response = new LogResponse(HttpStatus.BAD_REQUEST, "Error occurred while saving the log." + e.getMessage());
			return ResponseEntity.ok(response);
		}

	}

	public ResponseEntity<LogResponse> auditLogMessage(String message) {
		//syslog-ng removes all the text which comes before first colon including colon ':'
		StringBuilder builder = new StringBuilder(":");
		builder.append(message);
		try {
			AUDIT_LOG.info(builder.toString());
			LogResponse response = new LogResponse(HttpStatus.CREATED, "Message has been successfully saved.");
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			LogResponse response = new LogResponse(HttpStatus.BAD_REQUEST, "Error occurred while saving the log. " + e.getMessage());
			return ResponseEntity.ok(response);
		}
	}

	public ResponseEntity<LogResponse> activityLogMessage(String message) {
		//syslog-ng removes all the text which comes before first colon including colon ':'
		StringBuilder builder = new StringBuilder(":");
		builder.append(message);
		try {
			ACTIVITY_LOG.info(builder.toString());
			LogResponse response = new LogResponse(HttpStatus.CREATED, "Message has been successfully saved.");
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			LogResponse response = new LogResponse(HttpStatus.BAD_REQUEST, "Error occurred while saving the log. " + e.getMessage());
			return ResponseEntity.ok(response);
		}

	}
}
