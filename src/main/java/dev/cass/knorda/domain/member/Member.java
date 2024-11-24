package dev.cass.knorda.domain.member;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import dev.cass.knorda.global.util.EncryptUtils;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Getter - Lombok의 어노테이션. 모든 엔티티 변수에 대해 Getter를 자동으로 생성
 * Setter - Lombok의 어노테이션. 모든 엔티티 변수에 대해 Setter를 자동으로 생성
 * Builder - Lombok의 어노테이션. 모든 엔티티 변수에 대해 Builder 패턴을 사용하는 생성자 자동 생성
 * AllArgsConstructor - Lombok의 어노테이션. 모든 엔티티 변수를 매개 변수로 받는 생성자 자동 생성
 * NoArgsConstructor - Lombok의 어노테이션. 매개 변수가 없는 생성자 자동 생성
 * - Getter/Setter 어노테이션으로 생성자를 생성하지 않으려면 Access Level을 NONE으로 설정하면 된다 (@Getter(AccessLevel.NONE), @Setter(AccessLevel.NONE))
 * <p>
 * Entity - JPA의 어노테이션. 해당 클래스를 엔티티 클래스로 지정
 * Table - JPA의 어노테이션. 엔티티 클래스와 매핑할 테이블의 세부 정보를 지정 (테이블명, 카탈로그, 스키마, 인덱스 등)
 * - 일부 데이터베이스에서는 스키마/카탈로그를 사용해서 사용자에 따라 데이터베이스의 테이블을 분리한다고 한다
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "`member`")
public class Member {
	/**
	 * GeneratedValue - JPA의 어노테이션. 엔티티의 기본 키를 자동 생성하는 방법을 지정
	 * - IDENTITY: 기본 키 생성을 데이터베이스에 위임
	 * - SEQUENCE: 데이터베이스의 시퀀스를 사용하여 기본 키 생성 (유일한 값을 생성하는 객체인 Sequence를 사용해서, 엔티티를 persist할 때마다 시퀀스를 증가시킨 값을 기본 키로 사용)
	 * - TABLE: 키 생성 테이블을 사용하여 기본 키 생성
	 * - AUTO: 데이터베이스 벤더에 따라 IDENTITY, SEQUENCE, TABLE 중 하나를 선택
	 * <p>
	 * Column - JPA의 어노테이션. 엔티티 클래스의 변수를 데이터베이스 테이블의 컬럼과 매핑
	 * - name: 데이터베이스 테이블의 컬럼명
	 * - insertable: 엔티티를 데이터베이스에 저장할 때, 해당 컬럼을 포함할지 여부
	 * - updatable: 엔티티를 데이터베이스에 저장할 때, 해당 컬럼을 업데이트할지 여부
	 * - table: 해당 컬럼을 포함하는 테이블, 하나의 엔티티에서 여러 테이블을 매핑할 경우 사용
	 * (이하의 속성은 ddl-auto 속성이 create 또는 create-drop으로 설정된 경우에, ddl을 생성할 때 사용됨)
	 * - nullable: 해당 컬럼이 null이 될 수 있는지 여부
	 * - unique: 해당 컬럼의 값이 고유한지 여부
	 * - length: 해당 컬럼의 길이 (varchar 타입에서)
	 * - columnDefinition: 해당 컬럼의 데이터베이스 정의
	 * - precision: 해당 컬럼의 정밀도 (BigDecimal, BigInteger 타입에서)
	 * - scale: 해당 컬럼의 스케일 (BigDecimal 타입에서)
	 * <p>
	 * Id - JPA의 어노테이션. 해당 변수가 Primary Key 임을 지정
	 * <p>
	 * CreatedDate - Spring Data JPA의 어노테이션. 엔티티가 생성되어 저장될 때, 시간을 자동으로 저장
	 * LastModifiedDate - Spring Data JPA의 어노테이션. 엔티티가 수정되어 저장될 때, 시간을 자동으로 저장
	 */
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	@Id
	private Integer memberId;

	@Column(name = "member_name")
	private String memberName;

	@Setter(AccessLevel.NONE)
	@Column(name = "password")
	private String password;

	@Column(name = "last_logged_in_at")
	private LocalDateTime lastLoggedInAt;

	@Column(name = "is_deleted")
	private Boolean isDeleted;

	@Column(name = "description")
	private String description;

	@CreatedDate
	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "modified_at")
	private LocalDateTime modifiedAt;

	@Column(name = "modified_by")
	private String modifiedBy;

	public void setPassword(String password) {
		this.password = EncryptUtils.encryptSHA256(password);
	}

	public boolean isPasswordMatch(String password) {
		return this.password.equals(EncryptUtils.encryptSHA256(password));
	}

	public void update(String description) {
		this.description = description;
		this.modifiedAt = LocalDateTime.now();
	}

	public void update(String description, String modifiedBy) {
		this.modifiedBy = modifiedBy;
		this.update(description);
	}

	public void delete() {
		this.isDeleted = true;
		this.modifiedAt = LocalDateTime.now();
	}

	public void delete(String modifiedBy) {
		this.modifiedBy = modifiedBy;
		this.delete();
	}

	public void login() {
		this.lastLoggedInAt = LocalDateTime.now();
	}
}