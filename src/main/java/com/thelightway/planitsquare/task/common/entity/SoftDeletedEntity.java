package com.thelightway.planitsquare.task.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@MappedSuperclass
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class SoftDeletedEntity extends AuditBaseEntity{
	@Column(name = "active")
	private boolean active = true;

	public void changeDeleteStatus(boolean active) {
		this.active = active;
	}
}
