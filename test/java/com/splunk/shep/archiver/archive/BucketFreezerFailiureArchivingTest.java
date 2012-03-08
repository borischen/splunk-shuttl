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
package com.splunk.shep.archiver.archive;

import static com.splunk.shep.testutil.UtilsFile.*;
import static org.mockito.Mockito.*;
import static org.testng.AssertJUnit.*;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.http.client.HttpClient;
import org.mockito.ArgumentCaptor;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.splunk.shep.archiver.archive.recovery.FailedBucketTransfers;
import com.splunk.shep.archiver.model.Bucket;
import com.splunk.shep.testutil.UtilsBucket;
import com.splunk.shep.testutil.UtilsMockito;

/**
 * Fixture: BucketFreezer gets HttpStatus codes from HttpClient that means that
 * archiving bucket failed.
 */
@Test(groups = { "fast" })
public class BucketFreezerFailiureArchivingTest {

    BucketFreezer bucketFreezer;
    File failedBucketsLocation;
    File safeLocation;
    FailedBucketTransfers failedBucketTransfers;

    @BeforeMethod(groups = { "fast" })
    public void setUp_internalServerErrorHttpClientBucketFreezer() {
	safeLocation = createTempDirectory();
	failedBucketTransfers = mock(FailedBucketTransfers.class);
	failedBucketsLocation = createTempDirectory();
	HttpClient failingHttpClient = UtilsMockito
		.createInternalServerErrorHttpClientMock();
	bucketFreezer = new BucketFreezer(safeLocation.getAbsolutePath(),
		failingHttpClient, failedBucketTransfers);
    }

    @AfterMethod(groups = { "fast" })
    public void tearDown() {
	FileUtils.deleteQuietly(failedBucketsLocation);
	FileUtils.deleteQuietly(safeLocation);
    }

    public void freezeBucket_internalServerError_moveBucketWithFailedBucketTransfers()
	    throws IOException {
	Bucket failedBucket = UtilsBucket.createTestBucket();
	bucketFreezer.freezeBucket(failedBucket.getIndex(), failedBucket
		.getDirectory().getAbsolutePath());

	ArgumentCaptor<Bucket> bucketCaptor = ArgumentCaptor
		.forClass(Bucket.class);

	// Verification
	verify(failedBucketTransfers, times(1)).moveFailedBucket(
		bucketCaptor.capture());
	Bucket capturedBucket = bucketCaptor.getValue();
	assertEquals(failedBucket.getIndex(), capturedBucket.getIndex());
	assertEquals(failedBucket.getName(), capturedBucket.getName());
	assertEquals(failedBucket.getFormat(), capturedBucket.getFormat());
    }

}
