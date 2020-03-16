package gov.usgs.wma.waterdata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class TransformDao {
	private static final Logger LOG = LoggerFactory.getLogger(TransformDao.class);

	@Autowired
	@Qualifier("jdbcTemplateTransform")
	protected JdbcTemplate jdbcTemplate;

	@Value("classpath:sql/getTimeSeries.sql")
	protected Resource selectQuery;

	public List<TimeSeries> getTimeSeries(String timeSeriesUniqueId) {
		List<TimeSeries> rtn = Arrays.asList();
		try {
			String sql = new String(FileCopyUtils.copyToByteArray(selectQuery.getInputStream()));
			rtn = jdbcTemplate.query(
					sql,
					new TimeSeriesRowMapper(),
					timeSeriesUniqueId
			);
		} catch (EmptyResultDataAccessException e) {
			LOG.info("Couldn't find {} - {} ", timeSeriesUniqueId, e.getLocalizedMessage());
		} catch (IOException e) {
			LOG.error("Unable to get SQL statement", e);
			throw new RuntimeException(e);
		}

		// Maybe we'll want to know what was selected?
		return rtn;
	}
}
