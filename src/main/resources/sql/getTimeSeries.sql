select
    groundwater_daily_value_identifier,
    time_series_unique_id,
    monitoring_location_identifier,
    observered_property_id, -- TODO need to update aqts-capture-db schema to 'observed' rather than 'observered'
    statistic_id,
    time_step,
    unit_of_measure,
    result,
    approvals::text,
    qualifiers::text,
    grades::text
from
    groundwater_statistical_daily_value
where
    time_series_unique_id = ?
