package uk.ac.ncl.b3013461.Cloud.Camera;

import java.util.ArrayList;

import com.microsoft.windowsazure.services.servicebus.implementation.SqlFilter;
import com.microsoft.windowsazure.services.servicebus.models.*;

public class SubWithRules 
{
	private final SubscriptionInfo sub;
	private ArrayList<RuleInfo> rules = new ArrayList<RuleInfo>();
	public SubWithRules(String subscriptionName)
	{
		sub = new SubscriptionInfo(subscriptionName);
	}
	public SubscriptionInfo getSub() {
		return sub;
	}
	public ArrayList<RuleInfo> getRules() {
		return rules;
	}
	public void addRule(String ruleName, String rule)
	{
		RuleInfo r = new RuleInfo(ruleName);
		r = r.withSqlExpressionFilter(rule);
		rules.add(r);
	}
}
