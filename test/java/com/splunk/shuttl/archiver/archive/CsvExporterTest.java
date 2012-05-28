// Copyright (C) 2011 Splunk Inc.
//
// Splunk Inc. licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License.  You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.splunk.shuttl.archiver.archive;

import static com.splunk.shuttl.testutil.TUtilsFile.*;
import static java.util.Arrays.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.splunk.shuttl.archiver.model.Bucket;
import com.splunk.shuttl.testutil.TUtilsBucket;

@Test(groups = { "fast-unit" })
public class CsvExporterTest {

    private CsvExporter csvExporter;
    private SplunkExportTool exportTool;
    private GetsBucketsCsvFile getsBucketsCsvFile;
    private Bucket bucket;
    private ShellExecutor shellExecutor;
    private Map<String, String> emptyMap;

    @BeforeMethod
    public void setUp() {
	exportTool = mock(SplunkExportTool.class);
	getsBucketsCsvFile = mock(GetsBucketsCsvFile.class);
	shellExecutor = mock(ShellExecutor.class);
	csvExporter = new CsvExporter(exportTool, getsBucketsCsvFile, shellExecutor);

	bucket = TUtilsBucket.createTestBucket();
	emptyMap = Collections.<String, String> emptyMap();
    }

    public void exportBucketToCsv_givenRuntimeExportToolAndOutputDir_callsRuntimeInTheCorrectWay()
	    throws IOException {
	when(exportTool.getExecutableCommand()).thenReturn("/exporttool/path");
	when(exportTool.getEnvironmentVariables()).thenReturn(emptyMap);
	when(getsBucketsCsvFile.getCsvFile(bucket)).thenReturn(createTestFile());
	String bucketPath = bucket.getDirectory().getAbsolutePath();

	String[] command = new String[] { "/exporttool/path", bucketPath,
		"/csv/path", "-csv" };
	when(shellExecutor.executeCommand(emptyMap, asList(command)))
		.thenReturn(0);

	csvExporter.exportBucketToCsv(bucket);
    }

    @SuppressWarnings("unchecked")
    @Test(groups = { "fast-unit" }, expectedExceptions = { CsvExportFailedException.class })
    public void exportBucketToCsv_nonZeroExitStatus_throwCsvExportFailedException()
	    throws IOException, InterruptedException {
	when(shellExecutor.executeCommand(anyMap(), anyList()))
		.thenReturn(1);

	when(exportTool.getExecutableCommand()).thenReturn("/exporttool/path");
	when(getsBucketsCsvFile.getCsvFile(bucket)).thenReturn(
		new File("/dummy/file"));
	csvExporter.exportBucketToCsv(bucket);
    }

    @SuppressWarnings("unchecked")
    @Test(groups = { "fast-unit" }, expectedExceptions = { CsvExportFailedException.class })
    public void exportBucketToCsv_csvFileDoesNotExistAfterExport_throwCsvExportFailedException() {
	File nonExistantCsvFile = createTestFile();
	when(getsBucketsCsvFile.getCsvFile(bucket)).thenReturn(nonExistantCsvFile);

	when(shellExecutor.executeCommand(anyMap(), anyList()))
		.thenReturn(0);
	when(exportTool.getExecutableCommand()).thenReturn("/exporttool/path");

	FileUtils.deleteQuietly(nonExistantCsvFile);
	csvExporter.exportBucketToCsv(bucket);
    }
}
