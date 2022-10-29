package com.visionrent.domain;

import com.visionrent.domain.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Table(name="t_role")
@Entity
public class Role extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Enumerated(EnumType.STRING)
	@Column(length=20)
	private RoleType type;

	@Override
	public String toString() {
		return "Role [type=" + type + "]";
	}
	
	
	
}
