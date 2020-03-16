package gov.usgs.wma.waterdata;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class TimeSeriesRowMapper implements RowMapper<TimeSeries> {

	@Override
	public TimeSeries mapRow(ResultSet rs, int rowNum) throws SQLException {
		TimeSeries timeSeries = new TimeSeries();

		timeSeries.setGroundwaterDailyValueIdentifier(rs.getString("groundwater_daily_value_identifier"));
		timeSeries.setTimeSeriesUniqueId(rs.getString("time_series_unique_id"));
		timeSeries.setMonitoringLocationIdentifier(rs.getString("monitoring_location_identifier"));
		timeSeries.setObservedPropertyId(rs.getString("observered_property_id"));
		timeSeries.setStatisticId(rs.getString("statistic_id"));
		timeSeries.setTimeStep(rs.getDate("time_step"));
		timeSeries.setUnitOfMeasure(rs.getString("unit_of_measure"));
		timeSeries.setResult(rs.getString("result"));
		timeSeries.setApprovals(rs.getString("approvals"));
		timeSeries.setQualifiers(rs.getString("qualifiers"));
		timeSeries.setGrades(rs.getString("grades"));

		return timeSeries;
	}

}