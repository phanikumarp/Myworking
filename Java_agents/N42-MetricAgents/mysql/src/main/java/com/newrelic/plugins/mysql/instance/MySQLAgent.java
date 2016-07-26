package com.newrelic.plugins.mysql.instance;

import static com.newrelic.plugins.mysql.util.Constants.EQUALS;
import static com.newrelic.plugins.mysql.util.Constants.METRIC_LOG_PREFIX;
import static com.newrelic.plugins.mysql.util.Constants.SPACE;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.newrelic.metrics.publish.Agent;
//import com.newrelic.metrics.publish.util.Logger;
import com.newrelic.plugins.mysql.MetricMeta;
import com.newrelic.plugins.mysql.MySQL;

public class MySQLAgent extends Agent {
	private static final Logger logger = Logger.getLogger(MySQLAgent.class);
	private static final String GUID = "com.newrelic.plugins.mysql.instance";
	private static final String version = "2.0.0";
	public static final String AGENT_DEFAULT_HOST = "175.126.103.49";
	public static final String AGENT_DEFAULT_USER = "root";
	public static final String AGENT_DEFAULT_PASSWD = "root1";
	public static final String AGENT_DEFAULT_PROPERTIES = "";
	public static final String AGENT_DEFAULT_METRICS = "status,newrelic";

	private static Long TIMESTAMP;
	private static final String TAG1 = "host=";
	private static final String TAG2 = "hostip=";
	private static final String TAG3 = "appname=";

	private final String appName;
	private final String hostName;
	private final String hostIp;
	private final String user;
	private final String passwd;
	private String dbhost = "";
	private int dbport;
	private final String properties;
	private final String port;
	private String agentInfo;

	private final Set<String> metrics;
	private String newrelicprefix = "";
	private String statusprefix = "";

	private final Map<String, MetricMeta> metricsMeta = new HashMap<String, MetricMeta>();
	private Map<String, Object> metricCategories = new HashMap<String, Object>();
	private final MySQL m;
	private boolean firstReport = true;

	public MySQLAgent(String appName, String hostName, String hostIp, String user, String passwd, String properties,
			String port, String filePath, String newrelic_prefix, String status_prefix, Set<String> metrics,
			Map<String, Object> metricCategories, String dbhost, Integer dbport) {
		super(GUID, version);

		this.appName = appName;
		this.hostName = hostName;
		this.hostIp = hostIp;
		this.user = user;
		this.passwd = passwd;
		this.properties = properties;
		this.port = port;
		this.metrics = metrics;
		this.statusprefix = status_prefix;
		this.newrelicprefix = newrelic_prefix;
		this.metricCategories = metricCategories;
		this.dbhost = dbhost;
		this.dbport = dbport.intValue();
		this.m = new MySQL();

		createMetaData();

		logger.debug("MySQL Agent initialized: " + formatAgentParams(hostName, hostIp, user, properties, metrics));
	}

	private String formatAgentParams(String appname, String hostIp, String user, String properties,
			Set<String> metrics) {
		StringBuilder builder = new StringBuilder();
		builder.append("appname: ").append(appname).append(" | ");
		builder.append("host: ").append(hostIp).append(" | ");
		builder.append("user: ").append(user).append(" | ");
		builder.append("properties: ").append(properties).append(" | ");
		builder.append("metrics: ").append(metrics).append(" | ");
		return builder.toString();
	}

	public void pollCycle() {
		try (Connection c = this.m.getConnection(this.hostIp, this.user, this.passwd, this.properties, this.port);
				Socket clientSocket = new Socket(this.dbhost, this.dbport);
				PrintWriter out = ((clientSocket.isConnected()) ? new PrintWriter(clientSocket.getOutputStream(), true)
						: null);) {
			if (c == null) {
				return;
			}
			logger.info("Gathering MySQL metrics. " + getAgentInfo());
			Map<String, Float> results = gatherMetrics(c);

			if (out != null) {
				logger.info("Connected with the server : " + clientSocket.getInetAddress().getHostName()
						+ " with port : " + clientSocket.getPort());
				reportMetrics(results, out);

			}
		} catch (UnknownHostException e) {
			logger.error("Server not connected  due to this exception : " + e.getMessage());
		} catch (IOException | SQLException e) {
			logger.error("Server not connected  due to this exception : " + e.getMessage());
		}

		this.firstReport = false;
		logger.info("End of the Mysql Metrics \n");
	}

	private Map<String, Float> gatherMetrics(Connection c) {

		Map<String, Float> results = new HashMap<String, Float>();

		Map<String, Object> categories = getMetricCategories();

		Iterator<String> iter = categories.keySet().iterator();

		while (iter.hasNext()) {
			String category = (String) iter.next();

			@SuppressWarnings("unchecked")
			Map<String, String> attributes = (Map<String, String>) categories.get(category);
			if (isReportingForCategory(category)) {
				results.putAll(
						MySQL.runSQL(c, category, (String) attributes.get("SQL"), (String) attributes.get("result")));
			}
		}
		results.putAll(newRelicMetrics(results));
		return results;
	}

	protected Map<String, Float> newRelicMetrics(Map<String, Float> existing) {

		Map<String, Float> derived = new HashMap<String, Float>();

		if (!isReportingForCategory("newrelic")) {
			return derived;
		}
		if (!isReportingForCategory("status")) {
			return derived;
		}
		logger.debug("Adding New Relic derived metrics");

		if (areRequiredMetricsPresent("Reads", existing, new String[] { "status/com_select", "status/qcache_hits" })) {
			derived.put("newrelic/volume_reads", Float.valueOf(((Float) existing.get("status/com_select")).floatValue()
					+ ((Float) existing.get("status/qcache_hits")).floatValue()));
		}
		if (areRequiredMetricsPresent("Writes", existing,
				new String[] { "status/com_insert", "status/com_update", "status/com_delete", "status/com_replace",
						"status/com_insert_select", "status/com_update_multi", "status/com_delete_multi",
						"status/com_replace_select" })) {
			derived.put("newrelic/volume_writes",
					Float.valueOf(((Float) existing.get("status/com_insert")).floatValue()
							+ ((Float) existing.get("status/com_insert_select")).floatValue()
							+ ((Float) existing.get("status/com_update")).floatValue()
							+ ((Float) existing.get("status/com_update_multi")).floatValue()
							+ ((Float) existing.get("status/com_delete")).floatValue()
							+ ((Float) existing.get("status/com_delete_multi")).floatValue()
							+ ((Float) existing.get("status/com_replace")).floatValue()
							+ ((Float) existing.get("status/com_replace_select")).floatValue()));
		}
		if (areRequiredMetricsPresent("Read Throughput", existing, new String[] { "status/bytes_sent" })) {
			derived.put("bytes_reads", (Float) existing.get("status/bytes_sent"));
		}
		if (areRequiredMetricsPresent("Write Throughput", existing, new String[] { "status/bytes_received" })) {
			derived.put("bytes_writes", (Float) existing.get("status/bytes_received"));
		}
		if (areRequiredMetricsPresent("Connection Management", existing,
				new String[] { "status/threads_connected", "status/threads_running", "status/threads_cached" })) {
			Float threads_connected = (Float) existing.get("status/threads_connected");
			Float threads_running = (Float) existing.get("status/threads_running");

			derived.put("newrelic/connections_connected", threads_connected);
			derived.put("newrelic/connections_running", threads_running);
			derived.put("newrelic/connections_cached", (Float) existing.get("status/threads_cached"));

			Float pct_connection_utilization = Float.valueOf(0.0F);
			if (threads_connected.floatValue() > 0.0F) {
				pct_connection_utilization = Float
						.valueOf(threads_running.floatValue() / threads_connected.floatValue() * 100.0F);
			}
			derived.put("newrelic/pct_connection_utilization", pct_connection_utilization);
		}
		if (areRequiredMetricsPresent("InnoDB", existing,
				new String[] { "status/innodb_pages_created", "status/innodb_pages_read", "status/innodb_pages_written",
						"status/innodb_buffer_pool_read_requests", "status/innodb_buffer_pool_reads",
						"status/innodb_data_fsyncs", "status/innodb_os_log_fsyncs" })) {
			derived.put("newrelic/innodb_bp_pages_created", (Float) existing.get("status/innodb_pages_created"));
			derived.put("newrelic/innodb_bp_pages_read", (Float) existing.get("status/innodb_pages_read"));
			derived.put("newrelic/innodb_bp_pages_written", (Float) existing.get("status/innodb_pages_written"));

			Float innodb_read_requests = (Float) existing.get("status/innodb_buffer_pool_read_requests");
			Float innodb_reads = (Float) existing.get("status/innodb_buffer_pool_reads");

			Float pct_innodb_buffer_pool_hit_ratio = Float.valueOf(0.0F);
			if (innodb_read_requests.floatValue() + innodb_reads.floatValue() > 0.0F) {
				pct_innodb_buffer_pool_hit_ratio = Float.valueOf(innodb_read_requests.floatValue()
						/ (innodb_read_requests.floatValue() + innodb_reads.floatValue()) * 100.0F);
			}
			derived.put("newrelic/pct_innodb_buffer_pool_hit_ratio", pct_innodb_buffer_pool_hit_ratio);
			derived.put("newrelic/innodb_fsyncs_data", (Float) existing.get("status/innodb_data_fsyncs"));
			derived.put("newrelic/innodb_fsyncs_os_log", (Float) existing.get("status/innodb_os_log_fsyncs"));
		}
		if (areRequiredMetricsPresent("InnoDB Buffers", existing,
				new String[] { "status/innodb_buffer_pool_pages_total", "status/innodb_buffer_pool_pages_data",
						"status/innodb_buffer_pool_pages_misc", "status/innodb_buffer_pool_pages_dirty",
						"status/innodb_buffer_pool_pages_free" })) {
			Float pages_total = (Float) existing.get("status/innodb_buffer_pool_pages_total");
			Float pages_data = (Float) existing.get("status/innodb_buffer_pool_pages_data");
			Float pages_misc = (Float) existing.get("status/innodb_buffer_pool_pages_misc");
			Float pages_dirty = (Float) existing.get("status/innodb_buffer_pool_pages_dirty");
			Float pages_free = (Float) existing.get("status/innodb_buffer_pool_pages_free");

			derived.put("newrelic/innodb_buffer_pool_pages_clean",
					Float.valueOf(pages_data.floatValue() - pages_dirty.floatValue()));
			derived.put("newrelic/innodb_buffer_pool_pages_dirty", pages_dirty);
			derived.put("newrelic/innodb_buffer_pool_pages_misc", pages_misc);
			derived.put("newrelic/innodb_buffer_pool_pages_free", pages_free);
			derived.put("newrelic/innodb_buffer_pool_pages_unassigned", Float.valueOf(pages_total.floatValue()
					- pages_data.floatValue() - pages_free.floatValue() - pages_misc.floatValue()));
		}
		if (areRequiredMetricsPresent("Query Cache", existing,
				new String[] { "status/qcache_hits", "status/com_select", "status/qcache_free_blocks",
						"status/qcache_total_blocks", "status/qcache_inserts", "status/qcache_not_cached" })) {
			Float qc_hits = (Float) existing.get("status/qcache_hits");
			Float reads = (Float) existing.get("status/com_select");
			Float free = (Float) existing.get("status/qcache_free_blocks");
			Float total = (Float) existing.get("status/qcache_total_blocks");

			derived.put("newrelic/query_cache_hits", qc_hits);
			derived.put("newrelic/query_cache_misses", (Float) existing.get("status/qcache_inserts"));
			derived.put("newrelic/query_cache_not_cached", (Float) existing.get("status/qcache_not_cached"));

			Float pct_query_cache_hit_utilization = Float.valueOf(0.0F);
			if (qc_hits.floatValue() + reads.floatValue() > 0.0F) {
				pct_query_cache_hit_utilization = Float
						.valueOf(qc_hits.floatValue() / (qc_hits.floatValue() + reads.floatValue()) * 100.0F);
			}
			derived.put("newrelic/pct_query_cache_hit_utilization", pct_query_cache_hit_utilization);

			Float pct_query_cache_memory_in_use = Float.valueOf(0.0F);
			if (total.floatValue() > 0.0F) {
				pct_query_cache_memory_in_use = Float.valueOf(100.0F - free.floatValue() / total.floatValue() * 100.0F);
			}
			derived.put("newrelic/pct_query_cache_memory_in_use", pct_query_cache_memory_in_use);
		}
		if (areRequiredMetricsPresent("Temp Tables", existing,
				new String[] { "status/created_tmp_tables", "status/created_tmp_disk_tables" })) {
			Float tmp_tables = (Float) existing.get("status/created_tmp_tables");
			Float tmp_tables_disk = (Float) existing.get("status/created_tmp_disk_tables");

			Float pct_tmp_tables_written_to_disk = Float.valueOf(0.0F);
			if (tmp_tables.floatValue() > 0.0F) {
				pct_tmp_tables_written_to_disk = Float
						.valueOf(tmp_tables_disk.floatValue() / tmp_tables.floatValue() * 100.0F);
			}
			derived.put("newrelic/pct_tmp_tables_written_to_disk", pct_tmp_tables_written_to_disk);
		}
		if (isReportingForCategory("slave")) {
			if (areRequiredMetricsPresent("newrelic/replication_lag", existing,
					new String[] { "slave/seconds_behind_master" })) {
				derived.put("newrelic/replication_lag", (Float) existing.get("slave/seconds_behind_master"));
			}
			if (areRequiredMetricsPresent("newrelic/replication_status", existing,
					new String[] { "slave/slave_io_running", "slave/slave_sql_running" })) {
				int slave_io_thread_running = ((Float) existing.get("slave/slave_io_running")).intValue();
				int slave_sql_thread_running = ((Float) existing.get("slave/slave_sql_running")).intValue();

				Float replication_status = Float.valueOf(1.0F);
				if (slave_io_thread_running + slave_sql_thread_running == 2) {
					replication_status = Float.valueOf(0.0F);
				}
				derived.put("newrelic/replication_status", replication_status);
			}
			if (areRequiredMetricsPresent("newrelic/slave_relay_log_bytes", existing,
					new String[] { "slave/relay_log_pos" })) {
				derived.put("newrelic/slave_relay_log_bytes", (Float) existing.get("slave/relay_log_pos"));
			}
			if (areRequiredMetricsPresent("newrelic/master_log_lag_bytes", existing,
					new String[] { "slave/read_master_log_pos", "slave/exec_master_log_pos" })) {
				derived.put("newrelic/master_log_lag_bytes",
						Float.valueOf(((Float) existing.get("slave/read_master_log_pos")).floatValue()
								- ((Float) existing.get("slave/exec_master_log_pos")).floatValue()));
			}
		} else {
			derived.put("newrelic/replication_lag", Float.valueOf(0.0F));
			derived.put("newrelic/replication_status", Float.valueOf(0.0F));
			derived.put("newrelic/slave_relay_log_bytes", Float.valueOf(0.0F));
			derived.put("newrelic/master_log_lag_bytes", Float.valueOf(0.0F));
		}
		return derived;
	}

	public void reportMetrics(Map<String, Float> results, PrintWriter out) {
		int count = 0;
		StringBuilder putCommand;
		logger.debug("Collected " + Integer.valueOf(results.size()) + " MySQL metrics. " + getAgentInfo());
		logger.debug(results);

		Iterator<String> iter = results.keySet().iterator();
		while (iter.hasNext()) {
			String key = ((String) iter.next()).toLowerCase();
			Float val = (Float) results.get(key);
			MetricMeta md = getMetricMeta(key);
			if (md != null) {
				logger.debug(
						METRIC_LOG_PREFIX + key.replace("status/", statusprefix).replace("newrelic/", newrelicprefix)
								+ SPACE + md + EQUALS + val);

				String mName = key.replace("\"", "").replace("status/", this.statusprefix).replace("newrelic/",
						this.newrelicprefix);

				putCommand = new StringBuilder();
				TIMESTAMP = Long.valueOf(System.currentTimeMillis()) / 1000;
				putCommand.append("put" + " ");
				putCommand.append(mName + " ");
				putCommand.append(TIMESTAMP + " ");
				putCommand.append(val + " ");
				putCommand.append(TAG3 + appName + " ");
				putCommand.append(TAG1 + hostName + " ");
				putCommand.append(TAG2 + hostIp + "\n");
				out.write(putCommand.toString());
				logger.info(putCommand.toString());
				out.flush();
				if (md.isCounter()) {
					reportMetric(key, md.getUnit(), md.getCounter().process(val));
				} else {
					reportMetric(key, md.getUnit(), val);
				}
			} else if (this.firstReport) {
				logger.debug("Not reporting identified metric " + key);
			}
		}
		out.close();

		logger.debug("Reported to New Relic " + Integer.valueOf(count) + " metrics. " + getAgentInfo());
	}

	boolean isReportingForCategory(String metricCategory) {
		return this.metrics.contains(metricCategory);
	}

	private String getAgentInfo() {
		if (this.agentInfo == null) {
			this.agentInfo = ("Agent Name: " + hostName + " Agent Version: " + version);
		}
		return this.agentInfo;
	}

	private void createMetaData() {
		Map<String, Object> categories = getMetricCategories();

		Iterator<String> iter = categories.keySet().iterator();
		while (iter.hasNext()) {
			String category = (String) iter.next();

			@SuppressWarnings("unchecked")
			Map<String, String> attributes = (Map<String, String>) categories.get(category);
			String valueMetrics = (String) attributes.get("value_metrics");
			if (valueMetrics != null) {
				Set<String> metrics = new HashSet<String>(
						Arrays.asList(valueMetrics.toLowerCase().replaceAll(" ", "").split(",")));
				for (String s : metrics) {
					addMetricMeta(category + "/" + s, new MetricMeta(false));
				}
			}
			String counterMetrics = (String) attributes.get("counter_metrics");
			if (counterMetrics != null) {
				Set<String> metrics = new HashSet<String>(
						Arrays.asList(counterMetrics.toLowerCase().replaceAll(" ", "").split(",")));
				for (String s : metrics) {
					addMetricMeta(category + "/" + s, new MetricMeta(true));
				}
			}
		}
		addMetricMeta("newrelic/volume_reads", new MetricMeta(true, "Queries/Second"));
		addMetricMeta("newrelic/volume_writes", new MetricMeta(true, "Queries/Second"));
		addMetricMeta("newrelic/bytes_reads", new MetricMeta(true, "Bytes/Second"));
		addMetricMeta("newrelic/bytes_writes", new MetricMeta(true, "Bytes/Second"));
		addMetricMeta("newrelic/connections_connected", new MetricMeta(false, "Connections"));
		addMetricMeta("newrelic/connections_running", new MetricMeta(false, "Connections"));
		addMetricMeta("newrelic/connections_cached", new MetricMeta(false, "Connections"));
		addMetricMeta("newrelic/innodb_bp_pages_created", new MetricMeta(true, "Pages/Second"));
		addMetricMeta("newrelic/innodb_bp_pages_read", new MetricMeta(true, "Pages/Second"));
		addMetricMeta("newrelic/innodb_bp_pages_written", new MetricMeta(true, "Pages/Second"));
		addMetricMeta("newrelic/query_cache_hits", new MetricMeta(true, "Queries/Seconds"));
		addMetricMeta("newrelic/query_cache_misses", new MetricMeta(true, "Queries/Seconds"));
		addMetricMeta("newrelic/query_cache_not_cached", new MetricMeta(true, "Queries/Seconds"));
		addMetricMeta("newrelic/replication_lag", new MetricMeta(false, "Seconds"));
		addMetricMeta("newrelic/replication_status", new MetricMeta(false, "State"));
		addMetricMeta("newrelic/pct_connection_utilization", new MetricMeta(false, "Percent"));
		addMetricMeta("newrelic/pct_innodb_buffer_pool_hit_ratio", new MetricMeta(false, "Percent"));
		addMetricMeta("newrelic/pct_query_cache_hit_utilization", new MetricMeta(false, "Percent"));
		addMetricMeta("newrelic/pct_query_cache_memory_in_use", new MetricMeta(false, "Percent"));
		addMetricMeta("newrelic/pct_tmp_tables_written_to_disk", new MetricMeta(false, "Percent"));
		addMetricMeta("newrelic/innodb_fsyncs_data", new MetricMeta(true, "Fsyncs/Second"));
		addMetricMeta("newrelic/innodb_fsyncs_os_log", new MetricMeta(true, "Fsyncs/Second"));
		addMetricMeta("newrelic/slave_relay_log_bytes", new MetricMeta(true, "Bytes/Second"));
		addMetricMeta("newrelic/master_log_lag_bytes", new MetricMeta(true, "Bytes/Second"));

		addMetricMeta("status/aborted_clients", new MetricMeta(true, "Connections/Second"));
		addMetricMeta("status/aborted_connects", new MetricMeta(true, "Connections/Second"));
		addMetricMeta("status/bytes_sent", new MetricMeta(true, "Bytes/Second"));
		addMetricMeta("status/bytes_received", new MetricMeta(true, "Bytes/Second"));

		addMetricMeta("status/com_select", new MetricMeta(true, "Selects/Second"));
		addMetricMeta("status/com_insert", new MetricMeta(true, "Inserts/Second"));
		addMetricMeta("status/com_insert_select", new MetricMeta(true, "Inserts/Second"));
		addMetricMeta("status/com_update", new MetricMeta(true, "Updates/Second"));
		addMetricMeta("status/com_update_multi", new MetricMeta(true, "Updates/Second"));
		addMetricMeta("status/com_delete", new MetricMeta(true, "Deletes/Second"));
		addMetricMeta("status/com_delete_multi", new MetricMeta(true, "Deletes/Second"));
		addMetricMeta("status/com_replace", new MetricMeta(true, "Replaces/Second"));
		addMetricMeta("status/com_replace_select", new MetricMeta(true, "Replaces/Second"));

		addMetricMeta("status/slow_queries", new MetricMeta(true, "Queries/Second"));
		addMetricMeta("status/created_tmp_tables", new MetricMeta(true, "Queries/Second"));
		addMetricMeta("status/created_tmp_disk_tables", new MetricMeta(true, "Queries/Second"));
		addMetricMeta("status/innodb_buffer_pool_pages_flushed", new MetricMeta(true, "Pages/Second"));

		addMetricMeta("newrelic/innodb_buffer_pool_pages_clean", new MetricMeta(false, "Pages"));
		addMetricMeta("newrelic/innodb_buffer_pool_pages_dirty", new MetricMeta(false, "Pages"));
		addMetricMeta("newrelic/innodb_buffer_pool_pages_misc", new MetricMeta(false, "Pages"));
		addMetricMeta("newrelic/innodb_buffer_pool_pages_free", new MetricMeta(false, "Pages"));
		addMetricMeta("newrelic/innodb_buffer_pool_pages_unassigned", new MetricMeta(false, "Pages"));

		addMetricMeta("status/innodb_data_fsyncs", new MetricMeta(true, "Fsyncs/Second"));
		addMetricMeta("status/innodb_os_log_fsyncs", new MetricMeta(true, "Fsyncs/Second"));
		addMetricMeta("status/innodb_os_log_written", new MetricMeta(true, "Bytes/Second"));

		addMetricMeta("status/qcache_free_blocks", new MetricMeta(false, "Blocks"));
		addMetricMeta("status/qcache_free_memory", new MetricMeta(false, "Bytes"));
		addMetricMeta("status/qcache_hits", new MetricMeta(true, "Queries/Second"));
		addMetricMeta("status/qcache_inserts", new MetricMeta(true, "Queries/Second"));
		addMetricMeta("status/qcache_lowmem_prunes", new MetricMeta(true, "Queries/Second"));
		addMetricMeta("status/qcache_not_cached", new MetricMeta(true, "Queries/Second"));
		addMetricMeta("status/qcache_queries_in_cache", new MetricMeta(false, "Queries"));
		addMetricMeta("status/qcache_total_blocks", new MetricMeta(false, "Blocks"));

		addMetricMeta("innodb_status/history_list_length", new MetricMeta(false, "Pages"));
		addMetricMeta("innodb_status/queries_inside_innodb", new MetricMeta(false, "Queries"));
		addMetricMeta("innodb_status/queries_in_queue", new MetricMeta(false, "Queries"));
		addMetricMeta("innodb_status/checkpoint_age", new MetricMeta(false, "Bytes"));

		addMetricMeta("master/position", new MetricMeta(true, "Bytes/Second"));
		addMetricMeta("slave/relay_log_pos", new MetricMeta(true, "Bytes/Second"));
	}

	private void addMetricMeta(String key, MetricMeta mm) {
		this.metricsMeta.put(key.toLowerCase(), mm);
	}

	private MetricMeta getMetricMeta(String key) {
		if ((key.startsWith("innodb_mutex/")) && (!this.metricsMeta.containsKey(key))) {
			addMetricMeta(key, new MetricMeta(true, "Operations/Second"));
		}
		return (MetricMeta) this.metricsMeta.get(key.toLowerCase());
	}

	private boolean areRequiredMetricsPresent(String category, Map<String, Float> map, String... keys) {
		for (String key : keys) {
			if (!map.containsKey(key)) {
				if (this.firstReport) {
					logger.debug(" Not reporting on '" + category + "' due to missing data field '" + key + "'");
				}
				return false;
			}
		}
		return true;
	}

	public String getComponentHumanLabel() {
		return this.hostName;
	}

	public Map<String, Object> getMetricCategories() {
		return this.metricCategories;
	}

	@Override
	public String getAgentName() {
		return "";
	}

}
