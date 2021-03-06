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
package com.splunk.shuttl.server.mbeans;

import static org.testng.Assert.*;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.splunk.shuttl.testutil.TUtilsMBean;

@Test(groups = { "fast-unit" })
public class JMXSplunkMBeanTest {

	private JMXSplunkMBean mBean;

	@BeforeMethod
	public void setUp() {
		mBean = JMXSplunk.createWithConfFile(TUtilsMBean
				.createEmptyConfInNamespace("splunkConf"));
	}

	public void getHost_afterSettingHost_getsHost() {
		assertNull(mBean.getHost());
		mBean.setHost("host");
		assertEquals("host", mBean.getHost());
	}

	public void getPort_afterSettingPort_getsPort() {
		assertNull(mBean.getPort());
		mBean.setPort("1234");
		assertEquals("1234", mBean.getPort());
	}

	public void getUsername_afterSettingUsername_getsUsername() {
		assertNull(mBean.getUsername());
		mBean.setUsername("username");
		assertEquals("username", mBean.getUsername());
	}

	public void getPassword_afterSettingPassword_getsPassword() {
		assertNull(mBean.getPassword());
		mBean.setPassword("password");
		assertEquals("password", mBean.getPassword());
	}
}
