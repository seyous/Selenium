package org.brandbank.tests.libs;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;


public class RetryTests implements IRetryAnalyzer  {
    int count = 0;

    public static String FailedModuleName;

    @Override
    public boolean retry(ITestResult result) {
        count++;
        return (count < 3);
    }
}

