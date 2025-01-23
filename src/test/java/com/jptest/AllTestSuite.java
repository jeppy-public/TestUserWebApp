package com.jptest;

import org.junit.platform.suite.api.IncludeTags;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@IncludeTags({"service", "repository","report"})
@SelectPackages({"com.jptest"})
public class AllTestSuite {
}
