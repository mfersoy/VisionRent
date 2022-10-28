package com.visionrent.domain;

import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@MappedSuperclass
public class BaseEntity {
   
	@Version
	private Long version;
}
