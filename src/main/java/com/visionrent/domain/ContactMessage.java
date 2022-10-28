package com.visionrent.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor


@Entity
@Table(name="t_cmessage")
public class ContactMessage extends BaseEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(length = 50,nullable = false)
	private String name;
	
	@Column(length = 50, nullable = false)
	private String subject;
	
	@Column(length = 200,nullable = false)
	private String body;
	
	@Column(length = 50,nullable = false)
	private String email;
	
}
