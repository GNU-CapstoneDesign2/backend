package org.duckdns.petfinderapp.domain.post.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.duckdns.petfinderapp.domain.post.enums.PetType;
import org.duckdns.petfinderapp.domain.post.enums.PostState;
import org.duckdns.petfinderapp.domain.user.entity.User;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "dtype", discriminatorType = DiscriminatorType.STRING)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@SuperBuilder
public class PostCommon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = true)
    private User user;

    @Column(name = "create_at",
            nullable = false,
            updatable = false)
    @CreationTimestamp
    private LocalDateTime createAt;

    @Column(insertable = false, updatable = false)
    private String dtype;

    @Column(nullable = false)
    private LocalDateTime date;

    @Column(length = 255, nullable = false)
    private String address;

    @Column(nullable = false)
    @Embedded
    private Coordinates coordinates;

    @Enumerated(EnumType.STRING)
    @Column(name = "pet_type", nullable = false)
    private PetType petType;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostState state;

    @Builder.Default
	@OneToMany(mappedBy = "common", cascade = {CascadeType.PERSIST,
		CascadeType.REMOVE}, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Image> images = new ArrayList<>();

    public void update(String address, PetType petType, String content, Coordinates coordinates) {
        this.address = address;
        this.petType = petType;
        this.content = content;
        this.coordinates = coordinates;
    }

	public void update(LocalDateTime data, String address, Coordinates coordinates, PetType petType, String content,
		PostState state) {
		this.date = data;
		this.address = address;
		this.coordinates = coordinates;
		this.petType = petType;
		this.content = content;
		this.state = state;
	}

    public void clearImages() {
        for (Image image : this.images) {
            image.setCommon(null);
        }
        this.images.clear(); // 리스트 비우기
    }

    public void updateImage(List<Image> newimg) {
        clearImages(); // 기존 이미지 관계 제거
        for (Image image : newimg) {
            addImage(image);
        }
    }

    public void addImage(Image image) {
        this.images.add(image);

		if (image.getCommon() != this)
            image.setCommon(this);
    }
}
