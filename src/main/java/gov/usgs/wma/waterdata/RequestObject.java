package gov.usgs.wma.waterdata;

public class RequestObject {

	// This lambda will ingest a time series unique id per invocation
	private String uniqueId;

	public String getUniqueId() {
		return uniqueId;
	}
	public void setUniqueId(String uniqueId) {
		this.uniqueId = uniqueId;
	}
}