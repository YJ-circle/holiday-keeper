package com.thelightway.planitsquare.task.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 소프트 삭제를 위한 Base Entity입니다.
 * active 값이
 * 	true이면 활성 상태를,
 * 	false이면 비활성 상태를 의미합니다.
 * 일반적으로 active 값이 true인 값만 서비스에서 조회가 가능합니다.
 */
@MappedSuperclass
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class SoftDeletedEntity extends AuditBaseEntity {
	@Column(name = "active")
	private boolean active = true;

	public void changeActiveStatus(boolean active) {
		this.active = active;
	}
}
