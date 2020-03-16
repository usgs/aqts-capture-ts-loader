insert into
	groundwater_daily_value
(
	groundwater_daily_value_identifier
	,time_series_unique_id
	,monitoring_location_id
	,monitoring_location_identifier
	,observered_property_id -- parm_cd in parm table
	,observered_property_name -- parm_nm in parm table
	,observered_property_reference
	,statistic_id
	,statistic
	,statistic_reference
	,time_step
	,unit_of_measure_id
	,unit_of_measure
	,unit_of_measure_reference
	,result
	,nil_reason
	,approvals
	,qualifiers
	,grades
)
values (
	?
	,?
	,NULL
	,?
	,?
	,NULL
	,NULL
	,?
	,NULL
	,NULL
	,?
	,NULL
	,?
	,NULL
	,?
	,NULL
	,?::jsonb
	,?::jsonb
	,?::jsonb
)
