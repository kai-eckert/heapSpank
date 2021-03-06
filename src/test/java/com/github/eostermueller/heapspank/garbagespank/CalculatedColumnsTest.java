package com.github.eostermueller.heapspank.garbagespank;

import static org.junit.Assert.*;

import org.junit.Test;

import com.github.eostermueller.heapspank.garbagespank.metricprovider.AbstractJStatMetricProvider;
import com.github.eostermueller.heapspank.garbagespank.metricprovider.JStatMetricProvider;

public class CalculatedColumnsTest {

	private static final String TEST_PREFIX = "T_";
	
	private static final String TEST_LINE = 
			//"COL1  COL2\n" +
		    "235   22153\n";

	@Test
	public void canFormatForPageDataExtractor() {

		AbstractJStatMetricProvider metricNames = new AbstractJStatMetricProvider() {
			public String[] getJStatMetricNames() {
				return new String[]{ "COL1", "COL2" };
			}
			@Override
			public String getColumnEnhanced(boolean skipCalculatedColumns, JStatLine current,
					JStatLine previous, long mesasurementIntervalInMilliSeconds, int index) {
				return current.getRawColumnData()[index];
			}
			@Override
			public boolean metricsAvailableOnFirstJStatRun() {
				return false;
			}
			@Override
			public JVMVersion[] getSupportedVersions() {
				return new JVMVersion[]{JVMVersion.v1_8};
			}
			@Override
			public JStatOption getJStatOption() {
				return JStatOption.gc;
			}
		};
		
		JStatLine line = new JStatLine(TEST_PREFIX, metricNames, TEST_LINE, 0);
		String pageDataExtractorFormat = line.getPageDataExtractorFormat();
		
		String[] rows = pageDataExtractorFormat.split("\n");
		assertEquals("had two two columnss, was expecting to get to test output rows", 2, rows.length);
		
		boolean col1Tested = false;
		boolean col2Tested = false;
		
		if (rows[0].contains("COL1")) {
			assertEquals("jstat row was not formatted correctly for the JMeter Plugin Page Data Extractor",
					"T_COL1=235<BR>",
					rows[0]);
			col1Tested = true;
		} else {
			assertEquals("jstat row was not formatted correctly for the JMeter Plugin Page Data Extractor",
					"T_COL1=235<BR>",
					rows[1]);
			col1Tested = true;
		}
			

		if (rows[0].contains("COL2")) {
			assertEquals("jstat row was not formatted correctly for the JMeter Plugin Page Data Extractor",
					"T_COL2=22153<BR>",
					rows[0]);
			col2Tested = true;
		} else {
			assertEquals("jstat row was not formatted correctly for the JMeter Plugin Page Data Extractor",
					"T_COL2=22153<BR>",
					rows[1]);
			col2Tested = true;
		}
		
		assertTrue(col1Tested);
		assertTrue(col2Tested);
		
	}

}
