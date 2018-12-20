package com.myrpc;

import com.myrpc.autoconfigure.MyRpcAutoConfiguration;
import com.myrpc.boot.config.ApplicationConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyRpcAutoConfiguration.class)
public class BootConfigTest {

	@Autowired
	private ApplicationConfig applicationConfig;

	@Test
	public void getApplicationConfig(){
        System.out.print(applicationConfig);
	}
}
