package com.splunk.shep.archiver.fileSystem;

import org.testng.AssertJUnit;
import org.testng.annotations.Test;

@Test(groups = { "fast-unit" })
public class FileOverwriteExceptionTest {
    
    public void fileOverwriteExceptionTest() {
	AssertJUnit.assertNotNull(new FileOverwriteException());
  }
}