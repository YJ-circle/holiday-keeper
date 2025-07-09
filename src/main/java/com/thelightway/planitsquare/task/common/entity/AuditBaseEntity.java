package com.thelightway.planitsquare.task.common.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

/**
 * Entity 생성일, 수정일을 관리하는 Audit Entity 입니다.
 */
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class AuditBaseEntity {

	@CreatedDate
	@Column(name = "created_time", updatable = false)
	private LocalDateTime createdTime;

	@LastModifiedDate
	@Column(name = "updated_time")
	private LocalDateTime updatedTime;
}
