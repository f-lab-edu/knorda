package dev.cass.knorda.domain.product;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import dev.cass.knorda.domain.member.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "`product`")
public class Product {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_id")
	@Id
	private Integer productId;

	/**
	 * ManyToOne - 다대일 관계를 매핑할 때 사용
	 * name (member_id) 컬럼 값을 가지고, 해당 값을 PK로 사용해서 해당하는 Member 엔티티를 찾아서 붙여줌
	 * FetchType.LAZY - 지연 로딩. 해당 엔티티를 조회할 때, 연관된 엔티티를 조회하지 않고, 실제로 사용할 때 조회
	 * (Product 엔티티를 조회했을 때는 member 엔티티가 null인 상태고, getMember() 메서드를 호출했을 때, 실제로 member 엔티티를 조회)
	 * 이로 인해 발생하는 문제가 N+1 문제 (Product List를 조회했을 때, Product List 조회 시점에 Member List도 조회하게 되어, Member List 조회 쿼리가 N번 발생하는 현상)
	 * 즉시 로딩 (FetchType.EAGER)을 설정하면, 해당 엔티티를 조회할 때 바로 조회하게 되어 N+1문제가 무조건 발생하고
	 * 지연 로딩에서는 엔티티를 조회할 때는 바로 발생하지 않으나, 일대다 관계에서는 연관 엔티티 조회 시 발생할 수 있음
	 * JoinColumn - 외래 키를 매핑할 때 사용
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;

	@Column(name = "name")
	private String name;

	@Column(name = "image_url")
	private String imageUrl;

	@Column(name = "description")
	private String description;

	@Column(name = "is_deleted")
	private boolean isDeleted;

	@CreatedDate
	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "modified_at")
	private LocalDateTime modifiedAt;

	public void delete() {
		this.isDeleted = true;
		this.modifiedAt = LocalDateTime.now();
	}

	public void update(String name, String description) {
		this.name = name;
		this.description = description;
		this.modifiedAt = LocalDateTime.now();
	}

	public void updateImage(String imageUrl) {
		this.imageUrl = imageUrl;
		this.modifiedAt = LocalDateTime.now();
	}
}
