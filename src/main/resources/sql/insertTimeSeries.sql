insert into
	groundwater_daily_value
(
	groundwater_daily_value_identifier
	,time_series_unique_id
	,monitoring_location_id
	,monitoring_location_identifier
	,observered_property_id
	,observered_property_name
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
	,(select monitoring_location_id from monitoring_location where monitoring_location_identifier = ?) foo
	,?
	,?
	,(select parm_nm from parm where parm_cd = ?) bar
	,?
	,?
	,(select stat_nm from stat where stat_cd = ?) baz
	,?
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
