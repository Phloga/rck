package de.vee.rck.suites;

import org.junit.platform.suite.api.ExcludePackages;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SelectPackages("de.vee.rck")
@ExcludePackages("de.vee.rck.suites")
@SuiteDisplayName("All Integration Tests")
public class AllTests {
}
