
package org.springframework.samples.petclinic.configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
public class DozerMapperConfiguration {

	@Bean
	public Mapper mapper(@Value(value = "classpath*:/DozerMapping.xml") Resource[] resourceArray) throws IOException {
		List<String> mappingFileUrlList = new ArrayList<>();
		for (Resource resource : resourceArray)
			mappingFileUrlList.add(String.valueOf(resource.getURL()));
		DozerBeanMapper dozerBeanMapper = new DozerBeanMapper();
		dozerBeanMapper.setMappingFiles(mappingFileUrlList);
		return dozerBeanMapper;
	}

}
