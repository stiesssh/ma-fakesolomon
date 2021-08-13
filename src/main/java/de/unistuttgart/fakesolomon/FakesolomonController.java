package de.unistuttgart.fakesolomon;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.unistuttgart.fakesolomon.FlatSloRule.ComparisonOperator;
import de.unistuttgart.fakesolomon.FlatSloRule.PresetOption;
import de.unistuttgart.fakesolomon.FlatSloRule.StatisticsOption;

@RestController
public class FakesolomonController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	public FakesolomonController(@Value("${backend.url}") String coreAlertUri) {
		template = new RestTemplate();
		this.coreAlertUri = coreAlertUri;
	}

	@Value("${backend.url}")
	private final String coreAlertUri;
	private final RestTemplate template;

	// this is fake after all...
	private final String projectId = "5ece9e3bb52c5001";// "5e8cc17ed645a00c";
	private final String linkIssueId = "5ece9ee7acac5014";
	private final String providerCompId = "5ece9ebcc4ec5011";
	private final String providerIfaceId = "5ece9f111d6c5019";

	/**
	 * Get the rules (fake)
	 * 
	 * @throws NoMatchingSLOException
	 */
	@GetMapping("/rules/kubernetes")
	public List<FlatSloRule> getRules() {
		List<FlatSloRule> rules = new ArrayList<>();

		// time units in seconds

		{
			// alert: provider_avail_avg_slo
			// expr: 'avg_over_time(probe_success[1m]) < 0.8'
			FlatSloRule rule = new FlatSloRule(projectId, providerCompId, providerIfaceId, "provider_avail_avg_slo",
					0.8, 60);
			rule.setPresetOptions(PresetOption.AVAILABILITY);
			rule.setStatisticsOption(StatisticsOption.AVG);
			rule.setComparisonOperator(ComparisonOperator.GREATER_OR_EQUAL);

			rules.add(rule);
		}

		{
			// alert: provider_avail_slo
			// expr: probe_success == 0 for: 10s
			FlatSloRule rule = new FlatSloRule(projectId, providerCompId, providerIfaceId, "provider_avail_slo", 1, 10);
			rule.setPresetOptions(PresetOption.AVAILABILITY);
			rule.setStatisticsOption(StatisticsOption.AVG);
			rule.setComparisonOperator(ComparisonOperator.GREATER_OR_EQUAL);

			rules.add(rule);
		}

		{
			// alert: provider_avail_avg_slo
			// expr: 'avg_over_time(probe_success[1m]) < 0.8'
			FlatSloRule rule = new FlatSloRule(projectId, providerCompId, providerIfaceId, "provider_respT_q2_slo", 1.5,
					10);
			rule.setPresetOptions(PresetOption.RESPONSE_TIME);
			rule.setStatisticsOption(StatisticsOption.AVG);
			rule.setComparisonOperator(ComparisonOperator.LESS);

			rules.add(rule);
		}

		return rules;
	}

	/**
	 * receives alerts from prometheus. s
	 * 
	 * @param prometheusAlert
	 */
	@PostMapping("/api/v2/alerts")
	public void prometheusPost(@RequestBody String prometheusAlert) {
		logger.info("receive Alert : " + prometheusAlert);
		// get alert name.
		Alert alert = parse(prometheusAlert);
		alert.setGropiusProjectId(projectId);
		alert.setGropiusComponentId(providerCompId);
		alert.setIssueId(linkIssueId);

		template.postForObject(coreAlertUri, alert, Void.class);
	}

	protected Alert parse(String prom) {
		Alert alert = new Alert();
		ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		try {
			JsonNode root = mapper.readTree(prom);
			
			JsonNode startAt = root.findPath("startsAt"); // "startsAt":"2021-06-07T12:15:43.759Z", --> parse to date.. 
			JsonNode value = root.findPath("value");	
			JsonNode name = root.findPath("alertname");
			
			alert.setAlertName(name.textValue());
			alert.setSloName(name.textValue());
			alert.setSloId(name.textValue());
			alert.setActualValue(value.asDouble());
			
			LocalDateTime date = LocalDateTime.parse(startAt.textValue(), DateTimeFormatter.ISO_DATE_TIME);
			alert.setAlertTime(date);
			
			
			Instant then = date.toInstant(ZoneOffset.UTC);
			Instant now = Instant.now();
			Instant diff = now.minus(then.getEpochSecond(), ChronoUnit.SECONDS);
			long duration = diff.getEpochSecond();

			alert.setActualPeriod(duration);
			
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return alert;
	}
}
