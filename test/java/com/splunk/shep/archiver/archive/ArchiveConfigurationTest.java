package com.splunk.shep.archiver.archive;

import static org.testng.AssertJUnit.*;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

@Test(groups = { "fast-unit" })
public class ArchiveConfigurationTest {

    ArchiveConfiguration configuration;

    @BeforeMethod(groups = { "fast-unit" })
    public void setUp() {
	configuration = new ArchiveConfiguration();
    }

    public void getArchiveFormat_defaultState_isNotNull() {
	assertNotNull(configuration.getArchiveFormat());
    }

    public void getArchivingRoot_defaultState_isNotNull() {
	assertNotNull(configuration.getArchivingRoot());
    }

    public void getClusterName_defaultState_isNotNull() {
	assertNotNull(configuration.getClusterName());
    }

    public void getServerName_defaultState_isNotNull() {
	assertNotNull(configuration.getServerName());
    }
}