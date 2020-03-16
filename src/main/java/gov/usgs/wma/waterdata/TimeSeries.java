package gov.usgs.wma.waterdata;


import java.sql.Date;
import java.util.Objects;


public class TimeSeries {
	String groundwaterDailyValueIdentifier;
	String timeSeriesUniqueId;
	String monitoringLocationIdentifier;
	String observedPropertyId;
	String statisticId;
	Date timeStep;
	String unitOfMeasure;
	String result;
	String approvals;
	String qualifiers;
	String grades;

	public String getGroundwaterDailyValueIdentifier() {
		return groundwaterDailyValueIdentifier;
	}

	public void setGroundwaterDailyValueIdentifier(String groundwaterDailyValueIdentifier) {
		this.groundwaterDailyValueIdentifier = groundwaterDailyValueIdentifier;
	}

	public String getTimeSeriesUniqueId() {
		return timeSeriesUniqueId;
	}

	public void setTimeSeriesUniqueId(String timeSeriesUniqueId) {
		this.timeSeriesUniqueId = timeSeriesUniqueId;
	}

	public String getMonitoringLocationIdentifier() {
		return monitoringLocationIdentifier;
	}

	public void setMonitoringLocationIdentifier(String monitoringLocationIdentifier) {
		this.monitoringLocationIdentifier = monitoringLocationIdentifier;
	}

	public String getObservedPropertyId() {
		return observedPropertyId;
	}

	public void setObservedPropertyId(String observedPropertyId) {
		this.observedPropertyId = observedPropertyId;
	}

	public String getStatisticId() {
		return statisticId;
	}

	public void setStatisticId(String statisticId) {
		this.statisticId = statisticId;
	}

	public Date getTimeStep() {
		return timeStep;
	}

	public void setTimeStep(Date timeStep) {
		this.timeStep = timeStep;
	}

	public String getUnitOfMeasure() {
		return unitOfMeasure;
	}

	public void setUnitOfMeasure(String unitOfMeasure) {
		this.unitOfMeasure = unitOfMeasure;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getApprovals() {
		return approvals;
	}

	public void setApprovals(String approvals) {
		this.approvals = approvals;
	}

	public String getQualifiers() {
		return qualifiers;
	}

	public void setQualifiers(String qualifiers) {
		this.qualifiers = qualifiers;
	}

	public String getGrades() {
		return grades;
	}

	public void setGrades(String grades) {
		this.grades = grades;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TimeSeries that = (TimeSeries) o;
		return Objects.equals(groundwaterDailyValueIdentifier, that.groundwaterDailyValueIdentifier) &&
				Objects.equals(timeSeriesUniqueId, that.timeSeriesUniqueId) &&
				Objects.equals(monitoringLocationIdentifier, that.monitoringLocationIdentifier) &&
				Objects.equals(observedPropertyId, that.observedPropertyId) &&
				Objects.equals(statisticId, that.statisticId) &&
				Objects.equals(timeStep, that.timeStep) &&
				Objects.equals(unitOfMeasure, that.unitOfMeasure) &&
				Objects.equals(result, that.result) &&
				Objects.equals(approvals, that.approvals) &&
				Objects.equals(qualifiers, that.qualifiers) &&
				Objects.equals(grades, that.grades);
	}

	@Override
	public int hashCode() {
		return Objects.hash(
				groundwaterDailyValueIdentifier,
				timeSeriesUniqueId,
				monitoringLocationIdentifier,
				observedPropertyId,
				statisticId,
				timeStep,
				unitOfMeasure,
				result,
				approvals,
				qualifiers,
				grades
		);
	}
}
