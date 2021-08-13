package de.unistuttgart.fakesolomon;

import java.time.LocalDateTime;

/**
 * Alert as implemented by Solomon
 * 
 * @author maumau
 *
 */
public class Alert {
	double actualValue;
	double actualPeriod;
    String alertName;
    String alertDescription;
    LocalDateTime alertTime;
    String sloId;
    String sloName;
    String triggeringTargetName;
    String gropiusProjectId;
    String gropiusComponentId;
    String issueId;
    
	public String getAlertName() {
		return alertName;
	}
	public String getAlertDescription() {
		return alertDescription;
	}
	public LocalDateTime getAlertTime() {
		return alertTime;
	}
	public String getSloId() {
		return sloId;
	}
	public String getSloName() {
		return sloName;
	}
	public String getTriggeringTargetName() {
		return triggeringTargetName;
	}
	public String getGropiusProjectId() {
		return gropiusProjectId;
	}
	public String getGropiusComponentId() {
		return gropiusComponentId;
	}
	public double getActualValue() {
		return actualValue;
	}
	public double getActualPeriod() {
		return actualPeriod;
	}
	public void setActualValue(double actualValue) {
		this.actualValue = actualValue;
	}
	public void setActualPeriod(double actualPeriod) {
		this.actualPeriod = actualPeriod;
	}
	public void setAlertName(String alertName) {
		this.alertName = alertName;
	}
	public void setAlertDescription(String alertDescription) {
		this.alertDescription = alertDescription;
	}
	public void setAlertTime(LocalDateTime alertTime) {
		this.alertTime = alertTime;
	}
	public void setSloId(String sloId) {
		this.sloId = sloId;
	}
	public void setSloName(String sloName) {
		this.sloName = sloName;
	}
	public void setTriggeringTargetName(String triggeringTargetName) {
		this.triggeringTargetName = triggeringTargetName;
	}
	public void setGropiusProjectId(String gropiusProjectId) {
		this.gropiusProjectId = gropiusProjectId;
	}
	public void setGropiusComponentId(String gropiusComponentId) {
		this.gropiusComponentId = gropiusComponentId;
	}
	public String getIssueId() {
		return issueId;
	}
	public void setIssueId(String issueId) {
		this.issueId = issueId;
	} 
}