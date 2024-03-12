package vee.rck.suites;

import org.junit.platform.suite.api.ExcludePackages;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

@Suite
@SelectPackages("vee.rck")
@ExcludePackages("vee.rck.suites")
@SuiteDisplayName("All Integration Tests")
public class AllTests {
}
